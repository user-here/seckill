package org.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.internal.StringUtil;
import org.example.seckill.pojo.TOrder;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.pojo.TUser;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        // TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            // model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        // model.addAttribute("user", user);
        GoodsVo goodsVoByGoodsID = goodsService.findGoodsVoByGoodsID(goodsId);
        // 商品秒杀库存不够 返回秒杀失败
        if (goodsVoByGoodsID.getStockCount() < 1) {
            // model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 每个用户同类商品只能秒杀一次 直接从redis中取

        // 创建订单 ---> 普通订单 + 秒杀订单
        TOrder order = orderService.seckill(user, goodsVoByGoodsID); // 通过用户以及用户想要秒杀的食品id来构建订单
        // model.addAttribute("order", order);
        // model.addAttribute("goods", goodsVoByGoodsID);
        return RespBean.success(order);
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
        });
    }
}
