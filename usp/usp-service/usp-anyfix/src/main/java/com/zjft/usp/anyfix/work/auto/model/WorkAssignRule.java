package com.zjft.usp.anyfix.work.auto.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自动派单规则
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_assign_rule")
public class WorkAssignRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;
    /**
     * 人员列表
     */
    private String userList;

    /**
     * 派单半径
     */
    private Integer distance;

    /**
     * 等待认领时间
     */
    private Integer waiting;

    /**
     * 匹配工程师技能
     */
    private Integer skilled;


}
