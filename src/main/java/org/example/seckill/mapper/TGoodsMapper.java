package org.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.seckill.pojo.TGoods;
import org.example.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author Olivine
* @description 针对表【t_goods】的数据库操作Mapper
* @createDate 2025-03-18 17:23:47
* @Entity generator.pojo.TGoods
*/
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsID(Long goodsID);
}




