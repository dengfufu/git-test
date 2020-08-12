package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款申请
 *
 * @author: CK
 * @create: 2020-05-27 11:00
 */
public final class RefundApplyEnum {

    /**
     * 审批结果
     */
    @AllArgsConstructor
    @Getter
    public enum ApproveResult {
        agree(1, "同意"),
        disagree(0, "不同意");
        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (ApproveResult typeEnum : ApproveResult.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 退款申请状态
     */
    @AllArgsConstructor
    @Getter
    public enum Status {
        create(100, "申请创建"),
        cancel(101, "申请取消"),
        approve_success(200, "审核通过"),
        approve_fail(201, "审核不通过"),
        refund_in_process(300, "退款中"),
        refund_success(400, "退款成功"),
        refund_fail(401, "退款失败");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (Status typeEnum : Status.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }
}
