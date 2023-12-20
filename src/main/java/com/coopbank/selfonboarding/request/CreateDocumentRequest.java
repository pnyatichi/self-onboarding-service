package com.coopbank.selfonboarding.request;
import com.coopbank.selfonboarding.request.createDocumentData.documentDatas;
import lombok.Data;

@Data
public class CreateDocumentRequest {
	public String cabinetName;
	public String parentFolderIndex;
	public String documentName;
	public String document;
	private String accNumber;
	private String custNumber;
	private String idNumberNumber;
	private String accName;
	private documentDatas documentDatas;
}
