package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class ContactDetail {
    private String contactMethod;
    private String phoneNumberCountryCode;
    private String contactType;
    private String emailID;
    private String phoneNo;
}

