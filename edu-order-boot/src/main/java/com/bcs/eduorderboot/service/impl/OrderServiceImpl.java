package com.bcs.eduorderboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcs.eduorderboot.entity.UserCourseOrder;
import com.bcs.eduorderboot.mapper.OrderMapper;
import com.bcs.eduorderboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    // 保存订单
    public void saveOrder(String orderNo, Integer userId, Integer courseId, Integer activityCourseId, String sourceType) {
        UserCourseOrder order = new UserCourseOrder();

        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCourseId(courseId);
        order.setActivityCourseId(activityCourseId);
        order.setSourceType(sourceType);
        order.setIsDel(0);
        order.setStatus(20);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.insert(order);
    }

    // 查询某个用户的全部订单
    public List<UserCourseOrder> getOrdersByUserId(String userId) {
        QueryWrapper<UserCourseOrder> qw = new QueryWrapper();
        qw.eq("user_id", userId);
        return orderMapper.selectList(qw);
    }

}
