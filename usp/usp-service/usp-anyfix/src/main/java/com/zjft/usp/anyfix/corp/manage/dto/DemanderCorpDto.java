package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.uas.dto.CorpRegistry;
import lombok.Data;

@Data
public class DemanderCorpDto  extends CorpRegistry {

    private Long id;

    private String description;
}
