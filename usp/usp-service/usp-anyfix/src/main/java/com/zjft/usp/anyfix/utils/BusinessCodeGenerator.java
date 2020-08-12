package com.zjft.usp.anyfix.utils;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.redis.template.RedisRepository;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;

/**
 * code生成器
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-14 19:35
 **/
public class BusinessCodeGenerator {

    private RedisRepository redisRepository;
    private int startCode;

    public BusinessCodeGenerator(RedisRepository redisRepository, int startCode) {
        this.redisRepository = redisRepository;
        this.startCode = startCode;
    }

    /**
     * 生成工单号
     *
     * @return
     */
    public String getWorkCode(){
        String key = "work_code_" + DateUtil.date().toDateStr();
        return this.getCode(key);
    }

    /**
     * 生成委托商结算单号
     *
     * @param corpId
     * @param keyPrefix
     * @param codePrefix
     * @return
     */
    public String getSettleDemanderCode(Long corpId, String keyPrefix, String codePrefix) {
        if (StringUtils.isEmpty(keyPrefix)) {
            keyPrefix = "settle_demander_code";
        }
        if (StringUtils.isEmpty(codePrefix)) {
            codePrefix = "SD";
        }
        String key = keyPrefix + "_" + corpId + "_" + DateUtil.date().toDateStr();
        return codePrefix + this.getCode(key);
    }

    /**
     * 生成物品寄送单号
     *
     * @return
     */
    public String getGoodsPostNoCode(){
        String key = "goods_post_" + DateUtil.date().toDateStr();
        return this.getCode(key);
    }

    private String getCode(String key) {
        boolean isExist = this.redisRepository.exists(key);
        long code = this.redisRepository.incr(key);
        // 每天第一次生成code，则设置一天的过期时间
        if(!isExist){
            // 86400s是一天，此处多加十分钟冗余，即86400+600=87000
            // 需要转化为毫秒：87000 * 1000
            this.redisRepository.setExpire(key, 87000000L);
        }
        String today = DateUtil.today().substring(2);
        String date = today.replaceAll("-", "");
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return date + decimalFormat.format(code);
    }

}
