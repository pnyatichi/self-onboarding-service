package com.coopbank.selfonboarding.request;

import lombok.Data;

@Data
public class ConnectCabinetRequest {
	public String cabinetName;
	public String userName;
	public String userPassword;
}
