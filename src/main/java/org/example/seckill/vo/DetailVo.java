package org.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seckill.pojo.TUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private TUser user;
    private GoodsVo goodsVo;
    private int seckillStatus;
    private int remainSeconds;
}
