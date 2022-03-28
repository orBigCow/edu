package com.bcs.educourseboot.controller;

import com.bcs.educourseboot.entity.CourseDTO;
import com.bcs.educourseboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 获取所有课程
    @GetMapping("/getAllCourse")
    public List<CourseDTO> getAllCourse() {
        /*// 创建一个20个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                courseService.getAllCourse();
            }
        });*/
        List<CourseDTO> list = courseService.getAllCourse();
        return list;
    }

    /*@GetMapping("getCourseByUserId/{userid}")
    public List<Course> getCourseByUserId( @PathVariable("userid") String userid ) {
        List<Course> list = courseService.getCourseByUserId(userid);
        return list;
    }*/

    // 通过id获取课程信息
    @GetMapping("/getCourseById/{courseid}")
    public CourseDTO getCourseById(@PathVariable("courseid")Integer courseid) {
        CourseDTO courseDTO = courseService.getCourseById(courseid);
        return courseDTO;
    }

    // 根据用户id获取用户购买过的所有课程
    @GetMapping("/getMyCourseByUserId")
    public List<CourseDTO> getMyCourseByUserId(Integer userid) {
        List<CourseDTO> list = courseService.getMyCourseByUserId(userid);
        return list;
    }



}
