package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckill.mapper.TUserMapper;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TUserService;
import org.example.seckill.vo.LoginVo;
import org.example.seckill.vo.RespBean;
import org.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public RespBean doLogin(LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (username == null || password == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        TUser user = tUserMapper.selectById(username);
        if (null == user) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        return RespBean.success();
    }
}




