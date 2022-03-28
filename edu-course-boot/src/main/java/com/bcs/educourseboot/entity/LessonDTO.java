package com.bcs.educourseboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程节内容(CourseLesson)实体类
 *
 * @author BaochaoSu
 * @since 2022-02-22 10:35:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LessonDTO implements Serializable {

    // 每个课时对应的视频
    private CourseMedia courseMedia;

    private static final long serialVersionUID = -53276437234241520L;
    /**
     * id
     */
    private String id;
    /**
     * 课程id
     */
    private Integer courseId;
    /**
     * 章节id
     */
    private Integer sectionId;
    /**
     * 课时主题
     */
    private String theme;
    /**
     * 课时时长(分钟)
     */
    private Integer duration;
    /**
     * 是否免费
     */
    private Integer isFree;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    private Integer isDel;
    /**
     * 排序字段
     */
    private Integer orderNum;
    /**
     * 课时状态,0-隐藏，1-未发布，2-已发布
     */
    private Integer status;
}

