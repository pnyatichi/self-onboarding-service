package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class SendEmailRequest {
	
	public String OperationDate;
	public String From;
	public String To;
	public String Subject;
	public String SendDate;
	public String Message;
	public String AttachmentData;
	public String AttachmentName;

}
