package org.example.seckill.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.seckill.pojo.SeckillMessage;
import org.example.seckill.pojo.TSeckillOrder;
import org.example.seckill.pojo.TUser;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.service.TOrderService;
import org.example.seckill.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private ObjectMapper ObjectMapper;
    @Autowired
    private TGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        SeckillMessage seckillMessage = ObjectMapper.convertValue(message, SeckillMessage.class);
        TUser user = seckillMessage.getUser();
        Long goodId = seckillMessage.getGoodId();
        GoodsVo goodsVoByGoodsID = goodsService.findGoodsVoByGoodsID(goodId);
        if (goodsVoByGoodsID.getStockCount() < 1) {
            return;
        }
        // 判断是否重复抢购
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TSeckillOrder seckillOrder = (TSeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodId);
        if (seckillOrder != null) {
            return;
        }
        orderService.seckill(user, goodsVoByGoodsID);
    }
}
