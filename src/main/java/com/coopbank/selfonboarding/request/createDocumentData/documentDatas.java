package com.coopbank.selfonboarding.request.createDocumentData;

import java.util.List;

import lombok.Data;

@Data
public class documentDatas {
    private String dataDefIndex;
    private String dataDefName;
    private List<documentData> documentData;
}
