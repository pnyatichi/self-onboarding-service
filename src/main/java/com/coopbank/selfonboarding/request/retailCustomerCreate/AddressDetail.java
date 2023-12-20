package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class AddressDetail {
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String county;
    private String townOrCity;
    private String countryCode;
    private String postalCode;
    private String startDate;
    private String endDate;
    private String addressProofIndicator;
    private String addressVerifiedInd;
}