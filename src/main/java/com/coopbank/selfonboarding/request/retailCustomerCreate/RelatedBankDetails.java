package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class RelatedBankDetails {
    private String relationshipType;
    private String bankID;
    private String branchID;
    private String productType;
    private String accountNumber;
    private String relationshipStartdate;
    private String reasonForContinuing;
}
