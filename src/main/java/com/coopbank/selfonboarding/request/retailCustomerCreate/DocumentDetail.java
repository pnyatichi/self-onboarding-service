package com.coopbank.selfonboarding.request.retailCustomerCreate;

import lombok.Data;

@Data
public class DocumentDetail {
	 private String preferredUniqueIdIndicator;
	    private String documentTypeCode;
	    private String documentCategory;
	    private String countryOfIssue;
	    private String issueAuthority;
	    private String documentReferenceNumber;
	    private String isDocumentVerified;
	    private String documentIssuedDate;
	    private String documentExpiryDate;
	    private String placeOfIssue;
	    private String idIssuedOrganisation;

}
