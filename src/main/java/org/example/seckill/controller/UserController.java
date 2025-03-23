package org.example.seckill.controller;

import org.example.seckill.pojo.TUser;
import org.example.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

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
}
