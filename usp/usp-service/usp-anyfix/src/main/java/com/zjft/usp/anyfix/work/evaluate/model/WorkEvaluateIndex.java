package com.zjft.usp.anyfix.work.evaluate.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户评价指标表
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_evaluate_index")
public class WorkEvaluateIndex extends Model<WorkEvaluateIndex> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @TableId("work_id")
    private Long workId;

    /**
     * 评价指标
     */
    @TableId("evaluate_id")
    private Integer evaluateId;

    /**
     * 评价值
     */
    private Integer score;


}
