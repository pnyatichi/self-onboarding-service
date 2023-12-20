package com.coopbank.selfonboarding.request;

import com.coopbank.selfonboarding.request.retailCustomerCreate.AddressDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.ContactDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DemographicInfo;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DocumentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.EmploymentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.InstituteDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.KYCDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.PersonalPartyBasicDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelatedBankDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelationshipDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.TaxDetails;


import lombok.Data;

@Data
public class RetailCustomerCreate {
	private PersonalPartyBasicDetails personalPartyBasicDetails;
    private AddressDetails addressDetails;
    private ContactDetails contactDetails;
    private DocumentDetails documentDetails;
    private RelationshipDetails relationshipDetails;
    private RelatedBankDetails relatedBankDetails;
    private InstituteDetails instituteDetails;
    private DemographicInfo demographicInfo;
    private EmploymentDetails employmentDetails;
    private KYCDetails kycDetails;
    private TaxDetails taxDetails;

}
