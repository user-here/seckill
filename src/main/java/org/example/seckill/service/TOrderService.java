package org.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckill.pojo.TOrder;
import org.example.seckill.pojo.TSeckillGoods;
import org.example.seckill.pojo.TUser;
import org.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @author cadda smith
* @description 针对表【t_order】的数据库操作Service
* @createDate 2025-03-20 22:37:41
*/
public interface TOrderService extends IService<TOrder> {

    TOrder seckill(TUser user, GoodsVo goodsVoByGoodsID);
}
