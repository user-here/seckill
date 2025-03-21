package org.example.seckill.controller;

import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private TGoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, TUser user) {
        model.addAttribute("user", user); // 将user添加到model中 视图层可以通过${user}的方式访问user对象
        Object o = goodsService.findGoodsVo();
        model.addAttribute("goodsList", o);
        return "goodsList"; // 返回goodsList.html
    }

    @RequestMapping("/toDetail/{goodsID}")
    public String toDetail(Model model, TUser user, @PathVariable Long goodsID) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsID(goodsID);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        // 秒杀状态
        int seckillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            // 秒杀未开始
            seckillStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("goods", goodsService.findGoodsVoByGoodsID(goodsID));
        return "goodsDetail";
    }
}
