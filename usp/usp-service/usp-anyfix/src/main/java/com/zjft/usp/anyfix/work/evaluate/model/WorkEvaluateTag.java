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
 * 客户评价标签表
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_evaluate_tag")
public class WorkEvaluateTag extends Model<WorkEvaluateTag> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @TableId("work_id")
    private Long workId;

    /**
     * 评价标签
     */
    @TableId("tag_id")
    private Integer tagId;


}
