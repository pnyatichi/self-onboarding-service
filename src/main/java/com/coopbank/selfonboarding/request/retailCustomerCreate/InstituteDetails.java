package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class InstituteDetails {
    private String instituteUniversity;
    private String qualification;
    private String registrationNo;
    private String enrolmentStatus;
    private String courseStartDate;
    private String courseEndDate;
    private String certificationDate;
}