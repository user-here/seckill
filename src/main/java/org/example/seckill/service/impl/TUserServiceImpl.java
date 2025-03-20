package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.seckill.exception.GlobalException;
import org.example.seckill.mapper.TUserMapper;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TUserService;
import org.example.seckill.utils.CookieUtil;
import org.example.seckill.utils.UUIDUtil;
import org.example.seckill.vo.LoginVo;
import org.example.seckill.vo.RespBean;
import org.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author Olivine
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2025-03-18 17:29:58
*/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
        implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        TUser user = tUserMapper.selectById(username);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成Cookie
        String cookieValue = UUIDUtil.uuid();
        // 将用户信息放到Redis中
        redisTemplate.opsForValue().set("user:" + cookieValue, user);
        // request.getSession().setAttribute(cookieValue, user);
        CookieUtil.setCookie(request, response, "userTicket", cookieValue);
        return RespBean.success();
    }

    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (userTicket == null) {
            return null;
        }
        TUser user = (TUser)redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}




