package com.zjft.usp.pay.business.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CorpAccountDto {
    Long corpId;
    Long userId;
    List<Integer> protocolIds;
}
