package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class CustomerIDRequest {
	
	public String uniqueIdentifierType;
	public String uniqueIdentifierValue;

}
