package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckill.mapper.TOrderMapper;
import org.example.seckill.pojo.TOrder;
import org.example.seckill.pojo.TSeckillGoods;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TOrderService;
import org.example.seckill.service.TSeckillGoodsService;
import org.example.seckill.service.TSeckillOrderService;
import org.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author cadda smith
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2025-03-20 22:37:41
*/
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder>
    implements TOrderService {

    @Autowired
    private TSeckillGoodsService seckillGoodsService;
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private TSeckillOrderService seckillOrderService;

    @Override
    public TOrder seckill(TUser user, GoodsVo goodsVoByGoodsID) {
        // 通过seckillGoodsService拿到参加秒杀的商品
        TSeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goodsVoByGoodsID.getId()));
        // 秒杀商品的库存 -1
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        // 更新数据库
        seckillGoodsService.updateById(seckillGoods);
        // 生成订单
        TOrder order = new TOrder();
        // order.setId();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVoByGoodsID.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVoByGoodsID.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单 ---> 关联order
        TSeckillOrder seckillOrder = new TSeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVoByGoodsID.getId());
        seckillOrderService.save(seckillOrder);
        return order;
    }
}




