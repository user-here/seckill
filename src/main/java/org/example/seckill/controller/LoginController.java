package org.example.seckill.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.seckill.service.TUserService;
import org.example.seckill.vo.LoginVo;
import org.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private TUserService tUserService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * 登录
     * @param loginVo
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        return tUserService.doLogin(loginVo, request, response);
    }
}
