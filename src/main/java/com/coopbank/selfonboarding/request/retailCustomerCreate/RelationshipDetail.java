package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class RelationshipDetail {
    private String relatedEntityType;
    private String relatedEntity;
    private String relatedIternalPartyID;
    private String relationshipType;
    private String relationshipCategory;
    private String percentageOfShareHolding;
    private String guardCode;
    private String shareHolderType;
}
