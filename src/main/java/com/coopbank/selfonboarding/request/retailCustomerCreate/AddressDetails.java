package com.coopbank.selfonboarding.request.retailCustomerCreate;

import java.util.List;

import lombok.Data;

@Data
public class AddressDetails {
    private String prefferedAddressType;
    private List<AddressDetail> addressDetail;
}
