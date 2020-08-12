package com.zjft.usp.anyfix.work.review.dto;

import com.zjft.usp.anyfix.work.review.model.WorkReview;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户回访dto
 *
 * @author ljzhu
 **/
@ApiModel("客户回访")
@Getter
@Setter
public class WorkReviewDto extends WorkReview {

    private String corpName;
    private String userName;

}
