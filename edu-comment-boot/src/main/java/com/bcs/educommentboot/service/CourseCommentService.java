package com.bcs.educommentboot.service;

import com.bcs.educommentboot.entity.CourseComment;
import java.util.List;

/**
 * 课程留言表(CourseComment)表服务接口
 *
 * @author BaochaoSu
 * @since 2022-02-22 21:18:30
 */
public interface CourseCommentService {

    /**
     *  保存留言
     * @param courseComment  留言内容对象
     * @return   返回受影响的行数
     */
    Integer saveComment(CourseComment courseComment);

    /**
     *   查询某个课程的留言
     * @param courseId  课程id
     * @param offset  数据偏移数
     * @param pageSize  每页显示条数
     * @return   返回查询到的留言集合
     */
    List<CourseComment> getCourseCommentList(Integer courseId, Integer offset, Integer pageSize);

    /**
     *   某个用户给某一条评论点赞
     * @param commentId  评论id
     * @param userId  用户id
     * @return   1：成功    0：失败
     */
    Integer saveFavorite(Integer commentId, Integer userId);

    /**
     *   某个用户对某一条评论取消了点赞
     * @param commentId  评论id
     * @param userId  用户id
     * @return  1：成功   0：失败
     */
    Integer cancelFavorite(Integer commentId, Integer userId);

}
