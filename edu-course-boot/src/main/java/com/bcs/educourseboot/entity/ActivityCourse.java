package com.bcs.educourseboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;

/**
 * 活动课程表(ActivityCourse)实体类
 *
 * @author BaochaoSu
 * @since 2022-02-22 14:04:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityCourse implements Serializable {
    private static final long serialVersionUID = -70865365079793569L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 课程ID
     */
    private Integer courseId;
    /**
     * 活动开始时间
     */
    private Date beginTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 活动价格
     */
    private Double amount;
    /**
     * 库存值
     */
    private Integer stock;
    /**
     * 状态 0未上架 10已上架
     */
    private Integer status;
    /**
     * 逻辑删除 0未删除 1删除
     */
    private Integer isDel;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;

}

