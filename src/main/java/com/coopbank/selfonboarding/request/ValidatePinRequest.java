package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class ValidatePinRequest {
	public String IdNumber;
	public String TaxPayerType;

}
