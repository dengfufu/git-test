package com.zjft.usp.anyfix.work.assign.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 派单工程师表
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_assign_engineer")
public class WorkAssignEngineer extends Model<WorkAssignEngineer> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 派单主键
     */
    @TableId("assign_id")
    private Long assignId;

    /**
     * 工程师ID
     */
    @TableId("engineer_id")
    private Long engineerId;

    public WorkAssignEngineer() {

    }
    public WorkAssignEngineer(Long assignId, Long engineerId) {
        this.assignId = assignId;
        this.engineerId = engineerId;
    }
}
