package org.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TOrderService;
import org.example.seckill.service.TSeckillOrderService;
import org.example.seckill.vo.GoodsVo;
import org.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private TSeckillOrderService seckillOrderService;
    @Autowired
    private TOrderService orderService;


    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, TUser user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVoByGoodsID = goodsService.findGoodsVoByGoodsID(goodsId);
        if (goodsVoByGoodsID.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "seckillFail";
        }
        return "";
    }

}
