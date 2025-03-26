package org.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckill.pojo.TSeckillOrder;

/**
* @author cadda smith
* @description 针对表【t_seckill_order】的数据库操作Service
* @createDate 2025-03-20 22:37:41
*/
public interface TSeckillOrderService extends IService<TSeckillOrder> {

    Long getSeckillResult(Long id, Long goodsId);
}
