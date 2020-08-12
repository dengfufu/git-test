package com.zjft.usp.uas.user.service;

import com.zjft.usp.uas.user.dto.UserDefinedSettingDto;
import com.zjft.usp.uas.user.model.UserDefinedSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 用户自定义配置 服务类
 * </p>
 *
 * @author minji
 * @since 2020-05-13
 */
public interface UserDefinedSettingService extends IService<UserDefinedSetting> {


    List<UserDefinedSettingDto> listUserDefinedSetting(Long userId);

    void merge(UserDefinedSettingDto userDefinedSettingDto);
}
