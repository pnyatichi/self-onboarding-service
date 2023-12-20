package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class IprsRequest {
	
	public String logdata;
    public String pass;
    public String doc_type;
    public String doc_number;
    public String serial_number;

}
