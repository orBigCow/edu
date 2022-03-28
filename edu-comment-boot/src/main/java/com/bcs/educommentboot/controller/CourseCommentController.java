package com.bcs.educommentboot.controller;

import com.bcs.educommentboot.entity.CourseComment;
import com.bcs.educommentboot.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CourseCommentController {

    @Autowired
    private CourseCommentService courseCommentService;

    /**
     *   插入一条评论
     * @param courseId  课程id
     * @param userId  用户id
     * @param userName  用户名
     * @param comment  评论内容
     * @return  返回受影响行数
     */
    @RequestMapping("/saveComment")
    public Integer saveComment(Integer courseId, Integer userId, String userName, String comment) {
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        courseComment.setSectionId(0);
        courseComment.setLessonId(0);
        courseComment.setUserId(userId);
        courseComment.setUserName(userName);
        courseComment.setComment(comment);
        courseComment.setLastOperator(userId);
        courseComment.setType(0);
        courseComment.setParentId(0);
        Integer result = courseCommentService.saveComment(courseComment);
        return result;
    }

    /**
     *   分页查询当前课程所有的留言
     * @param courseId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/getCourseCommentList/{courseId}/{pageIndex}/{pageSize}")
    public List<CourseComment> getCourseCommentList(@PathVariable("courseId") Integer courseId, @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
        List<CourseComment> courseCommentList = courseCommentService.getCourseCommentList(courseId, (pageIndex - 1) * 20, pageSize);
        return courseCommentList;
    }

    /**
     *   点赞
     * @param commentId  评论id
     * @param userId  用户id
     * @return  返回1 点赞成功
     */
    @RequestMapping("/saveFavorite/{commentId}/{userId}")
    public Integer saveFavorite(@PathVariable("commentId") Integer commentId, @PathVariable("userId") Integer userId) {
        Integer result = courseCommentService.saveFavorite(commentId, userId);
        return result;
    }

    /**
     *   取消赞
     * @param commentId  评论id
     * @param userId  用户id
     * @return   返回1代表取消赞成功   返回0表示取消赞失败
     */
    @RequestMapping("/cancelFavorite/{commentId}/{userId}")
    public Integer cancelFavorite(@PathVariable("commentId") Integer commentId, @PathVariable("userId") Integer userId) {
        Integer result = courseCommentService.cancelFavorite(commentId, userId);
        return result;
    }
}
