package org.example.seckill.controller;

import jakarta.servlet.http.HttpSession;
import org.example.seckill.pojo.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.attribute.UserPrincipal;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @RequestMapping("/toList")
    public String toList(HttpSession session, Model model, @CookieValue("userTicket") String userTicket) {
        if (userTicket == null) {
            return "login";
        }
        if (session == null) {
            return "login";
        }
        // userTicket中存放的是SessionID
        TUser user = (TUser) session.getAttribute(userTicket);
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goodsList";
    }
}
