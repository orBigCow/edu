package com.bcs.educommentboot.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcs.educommentboot.entity.CourseComment;
import com.bcs.educommentboot.entity.CourseCommentFavoriteRecord;
import com.bcs.educommentboot.mapper.CourseCommentFavoriteRecordMapper;
import com.bcs.educommentboot.mapper.CourseCommentMapper;
import com.bcs.educommentboot.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 课程留言表(CourseComment)表服务实现类
 *
 * @author BaochaoSu
 * @since 2022-02-22 21:18:33
 */
@Service
public class CourseCommentServiceImpl implements CourseCommentService {

    @Autowired
    private CourseCommentMapper courseCommentMapper;

    @Autowired
    private CourseCommentFavoriteRecordMapper courseCommentFavoriteRecordMapper;

    // 插入一条评论
    @Override
    public Integer saveComment(CourseComment courseComment) {
        return courseCommentMapper.insert(courseComment);
    }

    // 查询课程相关的所有评论信息
    @Override
    public List<CourseComment> getCourseCommentList(Integer courseId, Integer offset, Integer pageSize) {
        return courseCommentMapper.getCourseCommentList(courseId, offset, pageSize);
    }

    // 点赞
    @Override
    public Integer saveFavorite(Integer commentId, Integer userId) {
        QueryWrapper<CourseCommentFavoriteRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        queryWrapper.eq("user_id", userId);
        Integer count = courseCommentFavoriteRecordMapper.selectCount(queryWrapper);
        IPage<CourseCommentFavoriteRecord> iPage = new Page<>();
        CourseCommentFavoriteRecord favoriteRecord = new CourseCommentFavoriteRecord();
        favoriteRecord.setIsDel(0);
        Integer result1;
        Integer result2;
        if (count == 0) {
            // 没有点过赞  新增一条点赞信息
            favoriteRecord.setUserId(userId);
            favoriteRecord.setCommentId(commentId);
            favoriteRecord.setCreateTime(new Date());
            favoriteRecord.setUpdateTime(new Date());
            result1 = courseCommentFavoriteRecordMapper.insert(favoriteRecord);
        } else {
            // 点过赞  更新点赞的情况 把is_del设为0
            result1 = courseCommentFavoriteRecordMapper.update(favoriteRecord, queryWrapper);
        }
        // 评论的点赞数增加1
        result2 = courseCommentMapper.updateCommentLikeCount(1, commentId);
        if (result1 == 0 || result2 == 0) {
            throw new RuntimeException("点赞失败！");
        }
        return 1;
    }

    @Override  // 取消赞
    public Integer cancelFavorite(Integer commentId, Integer userId) {
        QueryWrapper<CourseCommentFavoriteRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        queryWrapper.eq("user_id", userId);
        CourseCommentFavoriteRecord favoriteRecord = new CourseCommentFavoriteRecord();
        favoriteRecord.setIsDel(1);
        Integer result1 = courseCommentFavoriteRecordMapper.update(favoriteRecord, queryWrapper);
        Integer result2 = courseCommentMapper.updateCommentLikeCount(-1, commentId);
        if (result1 == 0 || result2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }


}
