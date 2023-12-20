package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class KYCDetails {
    private String kycStatus;
    private String kycDate;
    private String SubmittedForKYCIndicator;
    private String kycRecertificationDate;
    private String MainSourceOfFunds;
    private String OtherSourceOfFunds;
    private String OtherBankName;
    private String PreferredCommunicationLanguage;
    private String CustomerRating;
}