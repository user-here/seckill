package org.example.seckill.controller;

import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private TGoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, TUser user) {
        model.addAttribute("user", user); // 将user添加到model中 视图层可以通过${user}的方式访问user对象
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList"; // 返回goodsList.html
    }
}
