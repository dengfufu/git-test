package com.zjft.usp.uas.protocol.service;

import com.zjft.usp.uas.protocol.model.ProtocolDef;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 协议元数据  服务类
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
public interface ProtocolDefService extends IService<ProtocolDef> {

    List<ProtocolDef> listByModule(String module);
}
