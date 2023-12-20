package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class TaxDetails {
    private String TaxDeductionTable;
    private String TaxCountry;
    private String TaxExempt;
    private String ForeignTaxReportingIndicator;
    private String ForeignTaxReportinStatus;
    private String ForeignTaxReportingCountry;
    private String ForeignTaxReportingReviewDate;
}