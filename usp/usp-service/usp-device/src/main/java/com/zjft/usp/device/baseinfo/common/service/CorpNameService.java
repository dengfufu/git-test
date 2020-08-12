package com.zjft.usp.device.baseinfo.common.service;

import java.util.List;
import java.util.Map;

public interface CorpNameService {

    Map<Long,String> corpIdNameMap(List<Long> corpIdList);
}
