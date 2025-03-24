package org.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.seckill.pojo.TUser;
import org.example.seckill.vo.LoginVo;
import org.example.seckill.vo.RespBean;

/**
* @author Olivine
* @description 针对表【t_user】的数据库操作Service
* @createDate 2025-03-18 17:29:58
*/
public interface TUserService extends IService<TUser> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
