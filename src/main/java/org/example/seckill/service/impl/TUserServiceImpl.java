package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckill.mapper.TUserMapper;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TUserService;
import org.springframework.stereotype.Service;

/**
* @author Olivine
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2025-03-18 17:29:58
*/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
    implements TUserService {

}




