package com.zjft.usp.anyfix.work.auto.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 派单模式配置
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_assign_mode")
public class WorkAssignMode  extends Model<WorkAssignMode> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 服务商
     */
    private Long serviceCorp;

    /**
     * 自动派单模式，11=派给设备负责工程师、12=派给小组、13=距离优先、14=派给网点所有人
     */
    private Integer autoMode;

    /**
     * 人工派单模式
     */
    private Integer manualMode;

    /**
     * 自动派单规则
     */
    private Long assignRule;

    /**
     * 自动派单规则
     */
    private Long conditionId;


}
