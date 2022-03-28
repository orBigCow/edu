package com.bcs.eduadboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcs.eduadboot.entity.PromotionAd;
import com.bcs.eduadboot.mapper.AdMapper;
import com.bcs.eduadboot.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdMapper adMapper;

    // 根据广告位id查询所有的广告
    @Override
    public List<PromotionAd> getAdsBySpaceId(Integer sid) {
        QueryWrapper<PromotionAd> qw = new QueryWrapper<>();
        qw.eq("space_id", sid);
        return adMapper.selectList(qw);
    }
}
