package com.bcs.educommentboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcs.educommentboot.entity.CourseComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 课程留言表(CourseComment)表数据库访问层
 *
 * @author BaochaoSu
 * @since 2022-02-22 21:18:24
 */
@Component
public interface CourseCommentMapper extends BaseMapper<CourseComment> {

    /**
     *   查询某个课程的留言
     * @param courseId  课程id
     * @param offset  数据偏移数
     * @param pageSize  每页显示条数
     * @return   返回查询到的留言集合
     */
    @Select({"SELECT * " +
            "FROM course_comment \n" +
            "WHERE is_del = 0\n" +
            "AND course_id = #{courseId}\n" +
            "ORDER BY is_top DESC , like_count DESC , create_time DESC\n" +
            "LIMIT #{offset}, #{pageSize}"})
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id" , property = "favoriteRecords", many = @Many(select = "com.bcs.educommentboot.mapper.CourseCommentFavoriteRecordMapper.getFavoriteRecords"))
    })    List<CourseComment> getCourseCommentList(@Param("courseId") Integer courseId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);


    /**
     *   更新某一条评论的点赞数
     * @param count  -1：取消赞(减少一个赞)    1：点赞(增加一个赞)
     * @param commentId  评论id
     * @return  返回受影响的行数
     */
    @Update("update course_comment set like_count = like_count + #{count} where id = #{commentId}")
    Integer updateCommentLikeCount(@Param("count") Integer count, @Param("commentId") Integer commentId);

}

