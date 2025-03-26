package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckill.mapper.TSeckillOrderMapper;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.service.TSeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
* @author cadda smith
* @description 针对表【t_seckill_order】的数据库操作Service实现
* @createDate 2025-03-20 22:37:41
*/
@Service
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder>
    implements TSeckillOrderService {

    @Autowired
    private TSeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取秒杀结果
     * @param id
     * @param goodsId
     * @return 秒杀结果，-1：秒杀失败，0：排队中，>0：成功，秒杀产品id
     */
    @Override
    public Long getSeckillResult(Long id, Long goodsId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TSeckillOrder seckillOrder = (TSeckillOrder) valueOperations.get("order:" + id + ":" + goodsId);
        if (seckillOrder != null) {
            return seckillOrder.getGoodsId();
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)){
            return -1L;
        } else {
            return 0L;
        }
    }
}




