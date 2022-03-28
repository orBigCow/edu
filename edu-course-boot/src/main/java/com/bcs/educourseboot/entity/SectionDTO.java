package com.bcs.educourseboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author BaochaoSu
 * @since 2022-02-22 10:35:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SectionDTO implements Serializable {

    private List<LessonDTO> courseLessons;  // 章节对应的课时

    private static final long serialVersionUID = 342915894061191510L;
    /**
     * id
     */
    private String id;
    /**
     * 课程id
     */
    private Integer courseId;
    /**
     * 章节名
     */
    private String sectionName;
    /**
     * 章节描述
     */
    private String description;
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
    private Integer isDe;
    /**
     * 排序字段
     */
    private Integer orderNum;
    /**
     * 状态，0:隐藏；1：待更新；2：已发布
     */
    private Integer status;

}

