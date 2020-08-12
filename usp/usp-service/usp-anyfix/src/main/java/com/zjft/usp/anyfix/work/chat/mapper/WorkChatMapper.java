package com.zjft.usp.anyfix.work.chat.mapper;

import com.zjft.usp.anyfix.work.chat.model.WorkChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author linzerun
 * @since 2020-03-09
 */
public interface WorkChatMapper extends BaseMapper<WorkChat> {

    Integer selectMaxOrder(Long workId);

}
