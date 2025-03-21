package org.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckill.mapper.TGoodsMapper;
import org.example.seckill.pojo.TGoods;
import org.example.seckill.service.TGoodsService;
import org.example.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Olivine
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2025-03-18 17:23:47
*/
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements TGoodsService {

    @Autowired
    private TGoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsID(Long goodsID) {
        return goodsMapper.findGoodsVoByGoodsID(goodsID);
    }
}




