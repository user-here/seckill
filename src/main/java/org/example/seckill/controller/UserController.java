package org.example.seckill.controller;

import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TUserService;
import org.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TUserService userService;
    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean getUser(TUser user) {
        return RespBean.success(user);
    }

    @RequestMapping("/changepassword")
    public RespBean changePassword(String password, @CookieValue("userTicket") String userTicket) {
        return userService.updatePassword(userTicket, password, null, null);
    }
}
