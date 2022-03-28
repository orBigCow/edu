package com.bcs.eduadboot.service;

import com.bcs.eduadboot.entity.PromotionAd;

import java.util.List;

public interface AdService {

    List<PromotionAd> getAdsBySpaceId(Integer sid);

}
