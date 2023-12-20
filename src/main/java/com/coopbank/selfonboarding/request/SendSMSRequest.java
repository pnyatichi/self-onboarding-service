package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class SendSMSRequest {

	public String reqmessageID;
	public String reqmessage;
	public String reqmsisdn;
	public String requsername;
	public String reqpassword;
	public String reqencryped;
	public String reqclientID;
}
