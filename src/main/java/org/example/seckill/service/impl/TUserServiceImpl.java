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

import java.util.concurrent.TimeUnit;

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
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        TUser user = tUserMapper.selectById(username);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成Cookie
        String cookieValue = UUIDUtil.uuid();
        // 将用户信息放到Redis中
        redisTemplate.opsForValue().set("user:" + cookieValue, user, 180, TimeUnit.MINUTES);
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

    /**
     * 更新密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {

        TUser user = getUserByCookie(userTicket, request, response);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.USER_NOT_EXIST);
        }
        // 旁路模式 先更新数据库
        user.setPassword(password);
        int result = tUserMapper.updateById(user);
        // 然后删缓存
        if (1 == result) {
            // 删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_ERROR);
    }
}




