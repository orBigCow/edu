package com.bcs.eduadboot.controller;

import com.bcs.eduadboot.entity.PromotionAd;
import com.bcs.eduadboot.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ad")
@CrossOrigin
public class AdController {

    @Autowired
    private AdService adService;

    @GetMapping("getAdsBySpaceId/{spaceId}")
    public List<PromotionAd> getAdsBySpaceId(@PathVariable("spaceId") Integer spaceId){
        List<PromotionAd> list = adService.getAdsBySpaceId(spaceId);
        return list;
    }

}
