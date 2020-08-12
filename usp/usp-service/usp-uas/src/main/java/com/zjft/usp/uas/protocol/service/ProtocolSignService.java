package com.zjft.usp.uas.protocol.service;

import com.zjft.usp.uas.protocol.dto.ProtocolCheckDto;
import com.zjft.usp.uas.protocol.model.ProtocolSign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户签订协议  服务类
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
public interface ProtocolSignService extends IService<ProtocolSign> {

    /**
     * 签订协议
     *
     * @param corpId
     * @param operator
     * @param protocolIds
     */
    void signToB(Long corpId, Long operator, List<Integer> protocolIds);

    /**
     * 签订协议
     *
     * @param userId
     * @param protocolIds
     */
    void signToC(Long userId, List<Integer> protocolIds);

    /**
     * 检查用户是否需要重新签订协议
     *
     * @param userId
     * @return
     */
    List<ProtocolCheckDto> checkSign(Long userId, Long corpId, String module);

}
