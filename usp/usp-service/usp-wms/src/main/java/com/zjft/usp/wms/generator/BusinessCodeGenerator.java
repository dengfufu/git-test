package com.zjft.usp.wms.generator;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.redis.template.RedisRepository;

import java.text.DecimalFormat;

/**
 * 业务可显示的code生成器
 *
 * @author jfzou
 * @version 1.0
 * @date 2019/11/20 1:44 下午
 **/
public class BusinessCodeGenerator {

    private RedisRepository redisRepository;
    private int startCode;

    public BusinessCodeGenerator(RedisRepository redisRepository, int startCode) {
        this.redisRepository = redisRepository;
        this.startCode = startCode;
    }

    /**
     * 生成入库单可显示编号
     *
     * @param corpId     公司ID
     * @param codePrefix 大类对应的系统编号前缀如RK
     * @return
     */
    public String getIncomeWareCode(Long corpId, String codePrefix) {
        if (codePrefix == null || codePrefix.length() <= 0) {
            codePrefix = "RK";
        }
        return codePrefix + this.getBusinessCode(corpId, "income_ware_");
    }

    /**
     * 生成入库单可显示分组号
     *
     * @param corpId          公司ID
     * @param codeGroupPrefix 大类对应的分组号、批次号前缀如B
     * @return
     */
    public String getIncomeGroupCode(Long corpId, String codeGroupPrefix) {
        if (codeGroupPrefix == null || codeGroupPrefix.length() <= 0) {
            codeGroupPrefix = "B";
        }
        return codeGroupPrefix + this.getBusinessCode(corpId, "income_group_");
    }

    /**
     * 生成出库单可显示编号
     *
     * @param corpId     公司ID
     * @param codePrefix 大类对应的系统编号前缀如CK
     * @return
     */
    public String getOutcomeWareCode(Long corpId, String codePrefix) {
        if (codePrefix == null || codePrefix.length() <= 0) {
            codePrefix = "CK";
        }
        return codePrefix + this.getBusinessCode(corpId, "outcome_ware_");
    }

    /**
     * 生成发货单可显示编号
     *
     * @param corpId 公司ID
     * @return
     */
    public String getConsignCode(Long corpId, String codePreFix) {
        if (codePreFix == null || codePreFix.length() <= 0) {
            codePreFix = "S";
        }
        return codePreFix + this.getBusinessCode(corpId, "outcome_group_");
    }

    /**
     * 生成出库单可显示分组号
     *
     * @param corpId          公司ID
     * @param codeGroupPrefix 大类对应的分组号、批次号前缀如B
     * @return
     */
    public String getOutcomeGroupCode(Long corpId, String codeGroupPrefix) {
        if (codeGroupPrefix == null || codeGroupPrefix.length() <= 0) {
            codeGroupPrefix = "B";
        }
        return codeGroupPrefix + this.getBusinessCode(corpId, "outcome_group_");
    }

    /**
     * 生成调拨单可显示编号
     *
     * @param corpId     公司ID
     * @param codePrefix 大类对应的系统编号前缀如TB
     * @return
     */
    public String getTransWareCode(Long corpId, String codePrefix) {
        if (codePrefix == null || codePrefix.length() <= 0) {
            codePrefix = "TB";
        }
        return codePrefix + this.getBusinessCode(corpId, "trans_ware_");
    }

    /**
     * 生成调拨单可显示分组号
     *
     * @param corpId          公司ID
     * @param codeGroupPrefix 大类对应的分组号、批次号前缀如B
     * @return
     */
    public String getTransGroupCode(Long corpId, String codeGroupPrefix) {
        if (codeGroupPrefix == null || codeGroupPrefix.length() <= 0) {
            codeGroupPrefix = "B";
        }
        return codeGroupPrefix + this.getBusinessCode(corpId, "trans_group_");
    }

    private String getBusinessCode(Long corpId, String keyPrefix) {
        String key = corpId.toString() + keyPrefix + DateUtil.date().toDateStr();
        long code = this.redisRepository.incr(key);
        String date = DateUtil.today().replaceAll("-", "");
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return date + decimalFormat.format(code);
    }
}
