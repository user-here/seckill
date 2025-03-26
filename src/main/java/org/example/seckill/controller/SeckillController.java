package org.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.seckill.pojo.SeckillMessage;
import org.example.seckill.pojo.TOrder;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.pojo.TUser;
import org.example.seckill.rabbitmq.MQSender;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TOrderService;
import org.example.seckill.service.TSeckillOrderService;
import org.example.seckill.vo.GoodsVo;
import org.example.seckill.vo.RespBean;
import org.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private ObjectMapper objectMapper;
    private HashMap<Long, Boolean> emptyStockMap = new HashMap<>();

    /**
     * 秒杀 通过用户id和商品id来构建订单
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSeckill(Model model, TUser user, Long goodsId) {
        // 用户是否已经登录
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVoByGoodsID = goodsService.findGoodsVoByGoodsID(goodsId);
        // 商品秒杀库存不够 返回秒杀失败
        if (goodsVoByGoodsID.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
        // 每个用户同类商品只能秒杀一次
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "seckillFail";
        }
        // 创建订单 ---> 普通订单 + 秒杀订单
        TOrder order = orderService.seckill(user, goodsVoByGoodsID); // 通过用户以及用户想要秒杀的食品id来构建订单
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVoByGoodsID);
        return "orderDetail";
    }

    /**
     * 秒杀 通过用户id和商品id来构建订单
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(TUser user, Long goodsId) {
        // 用户是否已经登录
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 判断是否重复抢购
        TSeckillOrder seckillOrder = (TSeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        // 通过JVM本地缓存库存信息 减少和redis的交互
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 如果没有重复抢购 允许购买 让redis中的库存数量-1
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if (stock < 0) {
            // 如果库存为负数 则回滚库存数量
            emptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        try {
            // 发送秒杀到消息队列
            mqSender.sendSeckillMessage(objectMapper.writeValueAsString(seckillMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return RespBean.success(0);
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId：成功  -1：秒杀失败  0： 排队中
     */
    @GetMapping("/result")
    @ResponseBody
    public RespBean getSeckillResult(TUser user, Long goodsId) {
        if (user == null || goodsId < 0) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        Long orderId = seckillOrderService.getSeckillResult(user.getId(), goodsId);
        return RespBean.success(orderId);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化执行的时候的方法 将商品库存信息直接加载到redis中
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }
        goodsVoList.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
