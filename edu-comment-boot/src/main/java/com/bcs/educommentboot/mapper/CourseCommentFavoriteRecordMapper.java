package com.bcs.educommentboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcs.educommentboot.entity.CourseCommentFavoriteRecord;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CourseCommentFavoriteRecordMapper extends BaseMapper<CourseCommentFavoriteRecord> {

    @Select("SELECT * FROM course_comment_favorite_record WHERE comment_id = #{commentId} and is_del = 0")
    List<CourseCommentFavoriteRecord> getFavoriteRecords(Integer commentId);
}
