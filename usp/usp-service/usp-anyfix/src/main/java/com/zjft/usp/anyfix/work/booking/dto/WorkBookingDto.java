package com.zjft.usp.anyfix.work.booking.dto;

import com.zjft.usp.anyfix.work.booking.model.WorkBooking;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 预约时间dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-26 08:59
 **/
@ApiModel("预约时间")
@Getter
@Setter
public class WorkBookingDto extends WorkBooking {
}
