package com.bcs.educourseboot.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "edu-order-boot", path = "order")
public interface OrderRemoteService {

    @GetMapping("/getCourseIdsByUserId")
    List<Integer> getCourseIdsByUserId(@RequestParam("userId") Integer userId);

}
