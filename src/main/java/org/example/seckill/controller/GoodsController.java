package org.example.seckill.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpRequest;
import java.nio.file.attribute.UserPrincipal;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private TUserService userService;

    @RequestMapping("/toList")
    public String toList(Model model, TUser user) {

        model.addAttribute("user", user);
        return "goodsList";
    }
}
