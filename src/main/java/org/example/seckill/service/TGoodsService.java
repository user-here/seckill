package org.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckill.pojo.TGoods;
import org.example.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author Olivine
* @description 针对表【t_goods】的数据库操作Service
* @createDate 2025-03-18 17:23:47
*/
public interface TGoodsService extends IService<TGoods> {
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsID(Long goodsID);
}
