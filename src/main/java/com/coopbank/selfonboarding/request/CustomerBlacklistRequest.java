package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class CustomerBlacklistRequest {
	
	public String IdentificationType;
	public String IdentificationNumber;

}
