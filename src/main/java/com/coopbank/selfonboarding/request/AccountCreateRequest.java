package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class AccountCreateRequest {
	public String schemeCode;
	public String product;
	public String branchSortCode;
	public String currency;
	public String customerCode;
	public String sectorCode;
	public String subsectorCode;
	public String purposeOfAccount;
	public String wHTTaxIndicator;
	public String aROCode;
	public String dSOCode;
	public String statementMode;
	public String statementFrequency;
	public String statementMedium;
	public String startDate;
	public String weekDay;
	public String holidayStatus;
	public String businessEconomicCode;
	public String sourceOfFunds;
	public String productSegment;
}
