package com.zjft.usp.uas.user.mapper;

import com.zjft.usp.uas.user.model.UserDefinedSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户自定义配置 Mapper 接口
 * </p>
 *
 * @author minji
 * @since 2020-05-13
 */
public interface UserDefinedSettingMapper extends BaseMapper<UserDefinedSetting> {

    void merge(UserDefinedSetting entity);

}
