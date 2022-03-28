package com.bcs.educourseboot.service;

import com.bcs.educourseboot.entity.Course;
import com.bcs.educourseboot.entity.CourseDTO;

import java.util.List;

public interface CourseService {

    /**
     * 查询全部课程信息
     * @return
     */
    List<CourseDTO> getAllCourse();

    /**
     * 查询已登录用户购买的全部课程信息
     * @return
     */
    // List<Course> getCourseByUserId(String userId);

    /**
     * 查询某门课程的详细信息
     * @param courseid 课程编号
     * @return
     */
    CourseDTO getCourseById(Integer courseid);

    /**
     * 通过用户id获取用户购买的所有课程
     * @param userId  用户id
     * @return
     */
    List<CourseDTO> getMyCourseByUserId(Integer userId);

}
