package com.coopbank.selfonboarding.request.retailCustomerCreate;

import java.util.List;

import lombok.Data;

@Data
public class ContactDetails {
    private String preferredEmail;
    private String preferredPhone;
    private List<ContactDetail> contactDetail;
}
