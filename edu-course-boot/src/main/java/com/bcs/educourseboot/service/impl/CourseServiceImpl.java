package com.bcs.educourseboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcs.educourseboot.entity.*;
import com.bcs.educourseboot.mapper.*;
import com.bcs.educourseboot.remote.OrderRemoteService;
import com.bcs.educourseboot.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderRemoteService orderRemoteService;

    // 查询所有课程
    @Override
    public List<CourseDTO> getAllCourse() {
        //将redis内存中的序列化的集合名称用String重新命名（增加可读性）
        RedisSerializer rs = new StringRedisSerializer();
        redisTemplate.setKeySerializer(rs);

        // 先从redis中查找全部课程
        List<CourseDTO> list = (List<CourseDTO>)redisTemplate.opsForValue().get("allCourse");
        // 双层检测锁   防止缓存穿透
        if (list == null) {
            synchronized (this) {
                list = (List<CourseDTO>)redisTemplate.opsForValue().get("allCourse");
                if (list == null) {
                    // 如果redis中没有数据，就查询数据库
                    List<Course> courseList = getInitCourse(null);
                    list = new ArrayList<CourseDTO>();
                    for (Course course : courseList) {
                        CourseDTO courseDTO = new CourseDTO();
                        BeanUtils.copyProperties(course, courseDTO);
                        // 设置老师
                        setTeacher(courseDTO);
                        setTopTwoLesson(courseDTO);
                        list.add(courseDTO);
                    }
                    // 将数据库查询的数据保存到redis，生命周期为10分钟
                    redisTemplate.opsForValue().set("allCourse", list, 10, TimeUnit.MINUTES);
                }
            }
        }
        return list;
    }

    /*@Override
    public List<Course> getCourseByUserId(String userId) {
        return courseMapper.getCourseByUserId(userId);
    }*/

    // 根据课程id查询对应的课程信息
    @Override
    public CourseDTO getCourseById(Integer courseid) {
        CourseDTO courseDTO = new CourseDTO();
        Course course = courseMapper.selectById(courseid);
        BeanUtils.copyProperties(course, courseDTO);
        // 关联老师
        setTeacher(courseDTO);
        // 获得课程对应的章节信息
        List<SectionDTO> sectionDTOList = getSection(courseDTO);
        courseDTO.setCourseSections(sectionDTOList);
        return courseDTO;
    }

    // 根据用户id查询用户购买的所有课程
    @Override
    public List<CourseDTO> getMyCourseByUserId(Integer userId) {
        List<Integer> courseIds = orderRemoteService.getCourseIdsByUserId(userId);
        if (courseIds.size() == 0) {
            return null;
        }
        List<Course> courseList = getInitCourse(courseIds);
        System.out.println(courseList);
        List<CourseDTO> list = new ArrayList<CourseDTO>();
        for (Course course : courseList) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            // 设置老师
            setTeacher(courseDTO);
            setTopTwoLesson(courseDTO);
            list.add(courseDTO);
        }
        return list;
    }

    // 设置老师
   public void setTeacher(CourseDTO courseDTO) {
       QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("course_id", courseDTO.getId());
       queryWrapper.eq("is_del", 0);
       Teacher teacher = teacherMapper.selectOne(queryWrapper);
       courseDTO.setTeacher(teacher);
   }

    // 设置前两小节课
    public void setTopTwoLesson(CourseDTO courseDTO) {
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseDTO.getId());
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("status", 2);
        queryWrapper.orderByAsc("section_id");
        queryWrapper.orderByAsc("order_num");
        queryWrapper.last("limit 0, 2");
        List<CourseLesson> topTwoLessons = lessonMapper.selectList(queryWrapper);
        courseDTO.setTopTwoLesson(topTwoLessons);
    }

   // 获得课程列表
   public List<Course> getInitCourse(List<Integer> ids) {
       QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
       if (ids != null) {
           queryWrapper.in("id", ids);
       }
       queryWrapper.eq("is_del", 0);
       queryWrapper.eq("status", 1);
       queryWrapper.orderByAsc("sort_num");
       return courseMapper.selectList(queryWrapper);
   }

    // 获得课程相关的章节
    public List<SectionDTO> getSection(CourseDTO courseDTO) {
        // 查询课程关联的所有章节
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseDTO.getId());
        queryWrapper.eq("is_del", 0);  // 未删除
        queryWrapper.eq("status", 2);  // 已发布
        queryWrapper.orderByAsc("order_num");
        List<CourseSection> courseSections = sectionMapper.selectList(queryWrapper);
        ArrayList<SectionDTO> sectionDTOList = new ArrayList<>();
        // 为每个章节关联课时
        for (CourseSection courseSection : courseSections) {
            SectionDTO sectionDTO = new SectionDTO();
            BeanUtils.copyProperties(courseSection, sectionDTO);
            // 查询每个章节下的所有课时
            queryWrapper.clear();   // 清空查询条件
            queryWrapper.eq("course_id", courseDTO.getId());
            queryWrapper.eq("section_id", courseSection.getId());
            queryWrapper.eq("is_del", 0);
            queryWrapper.eq("status", 2);
            queryWrapper.orderByDesc("order_num");
            List<CourseLesson> courseLessonList = lessonMapper.selectList(queryWrapper);
            ArrayList<LessonDTO> lessonDTOList = new ArrayList<>();
            // 为每个课时关联视频
            for (CourseLesson courseLesson : courseLessonList) {
                LessonDTO lessonDTO = new LessonDTO();
                BeanUtils.copyProperties(courseLesson, lessonDTO);
                // 为每个课时设置视频
                setMedia(lessonDTO);
                lessonDTOList.add(lessonDTO);
            }
            sectionDTO.setCourseLessons(lessonDTOList);
            sectionDTOList.add(sectionDTO);
        }
        return sectionDTOList;
    }

    // 设置视频
    public void setMedia(LessonDTO lessonDTO) {
        QueryWrapper<CourseMedia> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lesson_id", lessonDTO.getId());
        queryWrapper.eq("is_del", 0);
        CourseMedia courseMedia = mediaMapper.selectOne(queryWrapper);
        lessonDTO.setCourseMedia(courseMedia);
    }


}
