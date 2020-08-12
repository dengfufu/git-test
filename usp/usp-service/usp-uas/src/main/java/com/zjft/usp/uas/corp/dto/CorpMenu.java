package com.zjft.usp.uas.corp.dto;

import java.util.List;
import lombok.Data;

@Data
public class CorpMenu {
    String name;

    String url;

    List<CorpMenu> children;
}
