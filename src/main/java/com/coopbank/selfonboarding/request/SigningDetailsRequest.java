package com.coopbank.selfonboarding.request;


import lombok.Data;
import com.coopbank.selfonboarding.request.SigningDetailsData.SignatoryDetails;

@Data
public class SigningDetailsRequest {
	public String accountNumber;
	public String bankCode;
	public String customerName;
	private SignatoryDetails SignatoryDetails;
}
