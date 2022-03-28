package com.bcs.eduorderboot.controller;

import com.bcs.eduorderboot.entity.UserCourseOrder;
import com.bcs.eduorderboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/saveOrder")
    public String saveOrder(Integer userId, Integer courseId, Integer activityCourseId, String sourceType){
        String orderNo = UUID.randomUUID().toString();
        orderService.saveOrder(orderNo, userId, courseId, activityCourseId, sourceType);
        return orderNo;
    }

    @GetMapping("/getCourseIdsByUserId")
    public List<Integer> getCourseIdsByUserId(String userId){
        List<UserCourseOrder> ordersByUserId = orderService.getOrdersByUserId(userId);
        List<Integer> list = new ArrayList<>();
        for (UserCourseOrder userCourseOrder : ordersByUserId) {
            Integer courseId = userCourseOrder.getCourseId();
            list.add(courseId);
        }
        return list;
    }


}
