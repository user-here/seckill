package org.example.seckill.controller;

import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TSeckillGoodsService;
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
    private TSeckillGoodsService seckillGoodsService;


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
        return "";

    }

}
