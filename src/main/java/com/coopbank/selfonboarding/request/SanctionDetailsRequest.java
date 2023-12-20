package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class SanctionDetailsRequest {
	
	public String MinMatchScore;
	public String CustomerType;
	public String FullName;
	public String FirstName;
	public String MiddleName;
	public String LastName;
	public String IdentificationDocType;
	public String IdentificationDocNo;
	public String Nationality;
	public String DateOfBirth;
	public String CountryOfBirth;

}
