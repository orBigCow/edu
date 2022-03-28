package com.bcs.eduorderboot.service;

import com.bcs.eduorderboot.entity.UserCourseOrder;

import java.util.List;

public interface OrderService {

    /***
     * 生成订单
     * @param orderNo 订单编号
     * @param userId 用户编号
     * @param courseId 课程编号
     * @param activityCourseId 活动课程编号
     * @param sourceType 订单来源类型 */
    void saveOrder(String orderNo, Integer userId, Integer courseId, Integer activityCourseId, String sourceType);

    /**
     *   查询用户所属的订单
     * @param userId  用户id
     * @return   返回用户所属的订单
     */
    List<UserCourseOrder> getOrdersByUserId(String userId);

}
