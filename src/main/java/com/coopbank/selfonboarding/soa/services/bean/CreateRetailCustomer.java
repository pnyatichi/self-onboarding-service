package com.coopbank.selfonboarding.soa.services.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.coopbank.selfonboarding.request.RetailCustomerCreate;
import com.coopbank.selfonboarding.request.retailCustomerCreate.AddressDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.AddressDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.ContactDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.ContactDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DemographicInfo;
import com.coopbank.selfonboarding.request.retailCustomerCreate.DocumentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.EmploymentDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.InstituteDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.KYCDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.PersonalPartyBasicDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelatedBankDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelationshipDetail;
import com.coopbank.selfonboarding.request.retailCustomerCreate.RelationshipDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.TaxDetails;

import com.coopbank.selfonboarding.util.CommonMethods;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateRetailCustomer {
	
	
    public  static SOAPMessage postCreateRetailCustomer(SOAPMessage soapRequest,String retailCustomerCreateEndpoint,String soaPassword) throws MalformedURLException,
    IOException {
SOAPMessage soapResponse = null;
try {
    System.setProperty("java.protocol.handler.pkgs", "sun.net.www.protocol");
    System.setProperty("javax.net.ssl.trustStore", "KeyStore.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", soaPassword);
    System.setProperty("javax.net.ssl.keyStore", "KeyStore.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", soaPassword);
    System.setProperty("javax.net.ssl.keyStoreType", "JKS");

    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    CommonMethods.doTrustToCertificates();
    String url =  retailCustomerCreateEndpoint;
    //System.out.println("\n--------------------------------- SOAP Response & URL ---------------------------------");
//    log.info("request is == " + soapRequest +" url == "+url);
    //System.out.println("\n--------------------------------- SOAP Response & URL ---------------------------------");
    
   // System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
    soapResponse = soapConnection.call(soapRequest, url);
    CommonMethods.createSoapResponse(soapResponse);
    //System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
    soapConnection.close();
} catch (Exception e) {
    e.printStackTrace();
}
return soapResponse;

}
    
    public static SOAPMessage createRetailCustomerSOAPRequest(RetailCustomerCreate retailCustomerCreate, String retailCustomerCreateEndpoint, String soaUsername, String soaPassword, String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();

            String soapenv = "soapenv";
            String mes = "mes";
            String com = "com";
            String post = "post";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(soapenv, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, "urn://co-opbank.co.ke/CommonServices/Data/Message/MessageHeader");
            envelope.addNamespaceDeclaration(com, "urn://co-opbank.co.ke/CommonServices/Data/Common");
            envelope.addNamespaceDeclaration(post, "urn://co-opbank.co.ke/BS/Customer/RetailCustomerCreate/Post.1.0");

            SOAPHeader header = envelope.getHeader();
            SOAPElement requestHeader = header.addChildElement("RequestHeader", mes);
            SOAPElement creationTimestamp = requestHeader.addChildElement("CreationTimestamp", com);
            SOAPElement correlationID = requestHeader.addChildElement("CorrelationID", com);
            SOAPElement messageID = requestHeader.addChildElement("MessageID", mes);
            SOAPElement credentials = requestHeader.addChildElement("Credentials", mes);
            SOAPElement systemCode = credentials.addChildElement("SystemCode", mes);
            SOAPElement userName = credentials.addChildElement("Username", mes);
            SOAPElement password = credentials.addChildElement("Password", mes);
            SOAPElement realm = credentials.addChildElement("Realm", mes);
            SOAPElement bankid = credentials.addChildElement("BankID", mes);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
            userName.addTextNode(soaUsername);
            password.addTextNode(soaPassword);
            realm.addTextNode("cc");
            bankid.addTextNode("01");



            SOAPBody soapBody = envelope.getBody();
         // ...
            SOAPElement retailCustomerCreateRq = soapBody.addChildElement("RetailCustomerCreateRq", post);

            PersonalPartyBasicDetails personalPartyBasicDetails = retailCustomerCreate.getPersonalPartyBasicDetails();
            
            
            SOAPElement personalPartyBasicDetailsElem = retailCustomerCreateRq.addChildElement("PersonalPartyBasicDetails", post);

            // Create and add child elements for PersonalPartyBasicDetails
            personalPartyBasicDetailsElem.addChildElement("CustomerType", post).addTextNode("INDV");
            personalPartyBasicDetailsElem.addChildElement("NationalIdentifier", post).addTextNode(personalPartyBasicDetails.getIdNumber());
            personalPartyBasicDetailsElem.addChildElement("FirstName", post).addTextNode(personalPartyBasicDetails.getFirstName());
            personalPartyBasicDetailsElem.addChildElement("MiddleName", post).addTextNode(personalPartyBasicDetails.getMiddleName());
            personalPartyBasicDetailsElem.addChildElement("LastName", post).addTextNode(personalPartyBasicDetails.getLastName());
            personalPartyBasicDetailsElem.addChildElement("FullName", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("PreferredName", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("NickName", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ShortName", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("Title", post).addTextNode(personalPartyBasicDetails.getSalutation());
            personalPartyBasicDetailsElem.addChildElement("Gender", post).addTextNode(personalPartyBasicDetails.getGender());
            personalPartyBasicDetailsElem.addChildElement("Region", post).addTextNode("01");
            personalPartyBasicDetailsElem.addChildElement("DateOfBirth", post).addTextNode(personalPartyBasicDetails.getDob());
            personalPartyBasicDetailsElem.addChildElement("CivilStatus", post).addTextNode("MARRD");
            personalPartyBasicDetailsElem.addChildElement("SeniorCitizen", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ResidentialStatus", post).addTextNode("R005");
            personalPartyBasicDetailsElem.addChildElement("CreditScore", post).addTextNode("OTHER");
            personalPartyBasicDetailsElem.addChildElement("DefaultChannel", post).addTextNode("SMS");
            personalPartyBasicDetailsElem.addChildElement("MarketingConsentObtained", post).addTextNode("Y");
            personalPartyBasicDetailsElem.addChildElement("EbankingEnabled", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ContactPersonId", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("NativeLanguageCode", post).addTextNode("KES");
            personalPartyBasicDetailsElem.addChildElement("TaxPayerOrIDNumber", post).addTextNode(personalPartyBasicDetails.getKraPin());
            personalPartyBasicDetailsElem.addChildElement("CountryOfCitizenship", post).addTextNode("IN");
            personalPartyBasicDetailsElem.addChildElement("CountryOfResidence", post).addTextNode("IN");
            personalPartyBasicDetailsElem.addChildElement("PlaceOfBirth", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("Nationality", post).addTextNode("IN");
            personalPartyBasicDetailsElem.addChildElement("HighRiskCountry", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("MotherMaidenName", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("PhysicalState", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("PermanentDisabled", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("CustomerLanguage", post).addTextNode("INFENG");
            personalPartyBasicDetailsElem.addChildElement("MinorIndicator", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("StaffIndicator", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("StaffID", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("NonResidentIndicator", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("NRBecomingDate", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("TaxDeductionTable", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("PrimaryServiceCenter", post).addTextNode("1001");
            personalPartyBasicDetailsElem.addChildElement("EnableAlerts", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("RelationshipManagerId", post).addTextNode("JMBINDA");
            personalPartyBasicDetailsElem.addChildElement("AccessOwnerSegment", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("Segment", post).addTextNode("Minor");
            personalPartyBasicDetailsElem.addChildElement("SubSegment", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("AffiliateToBank", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("InsiderToBank", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("InsiderRoleIndicator", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ReferredBy", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ReferralType", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("ComplexTransactionsExpected", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("Income", post).addTextNode("10000");
            personalPartyBasicDetailsElem.addChildElement("Priority", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("RelationshipOpeningDate", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("SectorCode", post).addTextNode("RE02");
            personalPartyBasicDetailsElem.addChildElement("SubSectorCode", post).addTextNode("RE0201");
            String pepAssociate = retailCustomerCreate.getPersonalPartyBasicDetails().getPepAssociate();
            String pepAssociateVal = null;
            if (pepAssociate.equals("Y")) {
            	pepAssociateVal = "HIGH";	
    	    }else {
    	    	pepAssociateVal = "LOW";
    	    }
            personalPartyBasicDetailsElem.addChildElement("RiskRating", post).addTextNode(pepAssociateVal);
            personalPartyBasicDetailsElem.addChildElement("CustomerStatus", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("CustomerId", post).addTextNode("");
            personalPartyBasicDetailsElem.addChildElement("CBKSector", post).addTextNode( "SC01");
            personalPartyBasicDetailsElem.addChildElement("CBKSubSector", post).addTextNode("SC0100107.050");
            personalPartyBasicDetailsElem.addChildElement("ContactPersonName", post).addTextNode("Alekya chiluka");
            personalPartyBasicDetailsElem.addChildElement("ContactPersonPhone", post).addTextNode("9090909090");
            personalPartyBasicDetailsElem.addChildElement("ContactPersonEmail", post).addTextNode("alekya@abc.com");
            personalPartyBasicDetailsElem.addChildElement("ContactPersonAddress", post).addTextNode("Hyderabad");
            personalPartyBasicDetailsElem.addChildElement("PEPAssociate", post).addTextNode("Y");
            personalPartyBasicDetailsElem.addChildElement("PEPName", post).addTextNode("Alekya");
            personalPartyBasicDetailsElem.addChildElement("PEPRole", post).addTextNode("aaaaa");
            personalPartyBasicDetailsElem.addChildElement("FatcaStatus", post).addTextNode("N");
            personalPartyBasicDetailsElem.addChildElement("TotalHouseldIncome", post).addTextNode("100");
            personalPartyBasicDetailsElem.addChildElement("AgentCode", post).addTextNode("AGENT1");
            personalPartyBasicDetailsElem.addChildElement("AROCode", post).addTextNode("114BB01");
            personalPartyBasicDetailsElem.addChildElement("PreferredLanguage", post).addTextNode("en_US");
            personalPartyBasicDetailsElem.addChildElement("PreferredChannelForCommunication", post).addTextNode("5,6");
            personalPartyBasicDetailsElem.addChildElement("NextOfKinPostalOrMailingAddress", post).addTextNode("PO.BOX 124566");
            personalPartyBasicDetailsElem.addChildElement("NextOfKinPhone", post).addTextNode("072056798");
            personalPartyBasicDetailsElem.addChildElement("NextOfKinName", post).addTextNode("NEXTKIN1");
            personalPartyBasicDetailsElem.addChildElement("NextOfKinIdentificationNo", post).addTextNode("345678");
            personalPartyBasicDetailsElem.addChildElement("NextOfKinEmailAddress", post).addTextNode("nextofkin@abc.com");
            personalPartyBasicDetailsElem.addChildElement("RelationshipWithNextOfKin", post).addTextNode("GUARDIAN");
            personalPartyBasicDetailsElem.addChildElement("IncomerRange", post).addTextNode("006");
            personalPartyBasicDetailsElem.addChildElement("ConstitutionCode", post).addTextNode("INDIV");
            personalPartyBasicDetailsElem.addChildElement("AccountUsedForBusinessPurpose", post).addTextNode("11112333333344");
            personalPartyBasicDetailsElem.addChildElement("BusinessName", post).addTextNode("ABC LTD");
            personalPartyBasicDetailsElem.addChildElement("BusinesIndicator", post).addTextNode("Y");
            personalPartyBasicDetailsElem.addChildElement("MajorSuppliers", post).addTextNode("GIKOMBA");
            personalPartyBasicDetailsElem.addChildElement("BusinessOperation", post).addTextNode("OTHER");
            personalPartyBasicDetailsElem.addChildElement("CountryOfBirth", post).addTextNode("002");


            // ...
//            AddressDetails addressDetails = retailCustomerCreate.getAddressDetails();

            // Create AddressDetails element
            SOAPElement addressDetailsElem = retailCustomerCreateRq.addChildElement("AddressDetails", post);

            // Add elements for AddressDetails
            addressDetailsElem.addChildElement("PrefferedAddressType", post).addTextNode("Mailing");

//            List<AddressDetail> addressDetailList = addressDetails.getAddressDetail();

//            for (AddressDetail addressDetail : addressDetailList) {
                // Create AddressDetail element
                SOAPElement addressDetailElem = addressDetailsElem.addChildElement("AddressDetail", post);

                addressDetailElem.addChildElement("AddressType", post).addTextNode("Mailing");
                addressDetailElem.addChildElement("AddressLine1", post).addTextNode("ADRS LINE1");
                addressDetailElem.addChildElement("AddressLine2", post).addTextNode("ADRS LINE2");
                addressDetailElem.addChildElement("AddressLine3", post).addTextNode("ADRS LINE3");
                addressDetailElem.addChildElement("County", post).addTextNode(personalPartyBasicDetails.getCounty());
                addressDetailElem.addChildElement("TownOrCity", post).addTextNode(personalPartyBasicDetails.getCityTown());
                addressDetailElem.addChildElement("CountryCode", post).addTextNode(personalPartyBasicDetails.getCountry());
                addressDetailElem.addChildElement("PostalCode", post).addTextNode(personalPartyBasicDetails.getZipCode());
                addressDetailElem.addChildElement("StartDate", post).addTextNode("2000-04-01T00:00:00.000");
                addressDetailElem.addChildElement("EndDate", post).addTextNode("2020-04-01T00:00:00.000");
                addressDetailElem.addChildElement("AddressProofIndicator", post).addTextNode("Y");
                addressDetailElem.addChildElement("AddressVerifiedInd", post).addTextNode("Y");
            
//            }


         // ...
//            ContactDetails contactDetails = retailCustomerCreate.getContactDetails();

            // Create ContactDetails element
            SOAPElement contactDetailsElem = retailCustomerCreateRq.addChildElement("ContactDetails", post);

            // Add elements for ContactDetails
            contactDetailsElem.addChildElement("PreferredEmail", post).addTextNode("HOMEEML");
            contactDetailsElem.addChildElement("PreferredPhone", post).addTextNode("HOMEPH1");

//            List<ContactDetail> contactDetailList = contactDetails.getContactDetail();

//            for (ContactDetail contactDetail : contactDetailList) {
                // Create ContactDetail element
                SOAPElement contactDetailPhoneElem = contactDetailsElem.addChildElement("ContactDetail", post);

                // Add elements for ContactDetail
                contactDetailPhoneElem.addChildElement("ContactMethod", post).addTextNode("PHONE");
                contactDetailPhoneElem.addChildElement("PhoneNumberCountryCode", post).addTextNode("+254");
                contactDetailPhoneElem.addChildElement("ContactType", post).addTextNode("HOMEPH1");
                contactDetailPhoneElem.addChildElement("EmailID", post).addTextNode("");
                contactDetailPhoneElem.addChildElement("PhoneNo", post).addTextNode(personalPartyBasicDetails.getPhone());
                
                SOAPElement contactDetailEmailElem = contactDetailsElem.addChildElement("ContactDetail", post);

                // Add elements for ContactDetail
                contactDetailEmailElem.addChildElement("ContactMethod", post).addTextNode("EMAIL");
                contactDetailEmailElem.addChildElement("PhoneNumberCountryCode", post).addTextNode("+254");
                contactDetailEmailElem.addChildElement("ContactType", post).addTextNode("HOMEEML");
                contactDetailEmailElem.addChildElement("EmailID", post).addTextNode(personalPartyBasicDetails.getEmail());
                contactDetailEmailElem.addChildElement("PhoneNo", post).addTextNode("");
              
//            }
            
//        	DocumentDetails documentDetails = retailCustomerCreate.getDocumentDetails();

			// Create DocumentDetails element
			SOAPElement documentDetailsElem = retailCustomerCreateRq.addChildElement("DocumentDetails", post);

			// Create DocumentDetail element
			SOAPElement documentDetailElem = documentDetailsElem.addChildElement("DocumentDetail", post);

			// Add elements for DocumentDetail (include all elements)
			documentDetailElem.addChildElement("PreferredUniqueIdIndicator", post).addTextNode("Y");
			documentDetailElem.addChildElement("DocumentTypeCode", post).addTextNode("IDPRF");
			documentDetailElem.addChildElement("DocumentCategory", post).addTextNode(personalPartyBasicDetails.getDocType());
			documentDetailElem.addChildElement("CountryOfIssue", post).addTextNode("KE");
			documentDetailElem.addChildElement("IssueAuthority", post).addTextNode("OTHER");
			documentDetailElem.addChildElement("DocumentReferenceNumber", post).addTextNode(personalPartyBasicDetails.getIdNumber());
			documentDetailElem.addChildElement("IsDocumentVerified", post).addTextNode("Y");
			documentDetailElem.addChildElement("DocumentIssuedDate", post).addTextNode("2020-01-10T00:00:00.000");
			documentDetailElem.addChildElement("DocumentExpiryDate", post).addTextNode("2030-01-10T00:00:00.000");
			documentDetailElem.addChildElement("PlaceOfIssue", post).addTextNode("003");
			documentDetailElem.addChildElement("IDIssuedOrganisation", post).addTextNode("");

            // ...

         // ...
//            RelationshipDetails relationshipDetails = retailCustomerCreate.getRelationshipDetails();

            // Create RelationshipDetails element
            SOAPElement relationshipDetailsElem = retailCustomerCreateRq.addChildElement("RelationshipDetails", post);

//            RelationshipDetail relationshipDetail = relationshipDetails.getRelationshipDetail();

            // Create RelationshipDetail element
            SOAPElement relationshipDetailElem = relationshipDetailsElem.addChildElement("RelationshipDetail", post);

            // Add elements for RelationshipDetail
            relationshipDetailElem.addChildElement("RelatedEntityType", post).addTextNode("");
            relationshipDetailElem.addChildElement("RelatedEntity", post).addTextNode("");
            relationshipDetailElem.addChildElement("RelatedIternalPartyID", post).addTextNode("");
            relationshipDetailElem.addChildElement("RelationshipType", post).addTextNode("");
            relationshipDetailElem.addChildElement("RelationshipCategory", post).addTextNode("");
            relationshipDetailElem.addChildElement("PercentageOfShareHolding", post).addTextNode("");
            relationshipDetailElem.addChildElement("GuardCode", post).addTextNode("");
            relationshipDetailElem.addChildElement("ShareHolderType", post).addTextNode("");
            
            
          
            // ...

            // ...
//            RelatedBankDetails relatedBankDetails = retailCustomerCreate.getRelatedBankDetails();

            // Create RelatedBankDetails element
            SOAPElement relatedBankDetailsElem = retailCustomerCreateRq.addChildElement("RelatedBankDetails", post);

            // Add elements for RelatedBankDetails (include optional elements as needed)
            relatedBankDetailsElem.addChildElement("RelationshipType", post).addTextNode("");
            relatedBankDetailsElem.addChildElement("BankID", post).addTextNode("");
            relatedBankDetailsElem.addChildElement("BranchID", post).addTextNode("");
            relatedBankDetailsElem.addChildElement("ProductType", post).addTextNode("");
            relatedBankDetailsElem.addChildElement("AccountNumber", post).addTextNode("");
            //relatedBankDetailsElem.addChildElement("RelationshipStartdate", post).addTextNode(relatedBankDetails.getRelationshipStartdate());
            relatedBankDetailsElem.addChildElement("ReasonForContinuing", post).addTextNode("");
          
            // ...

         // ...
//            InstituteDetails instituteDetails = retailCustomerCreate.getInstituteDetails();

            // Create InstituteDetails element
            SOAPElement instituteDetailsElem = retailCustomerCreateRq.addChildElement("InstituteDetails", post);

            // Add elements for InstituteDetails (include all elements)
            instituteDetailsElem.addChildElement("InstituteUniversity", post).addTextNode("");
            instituteDetailsElem.addChildElement("Qualification", post).addTextNode("");
            instituteDetailsElem.addChildElement("RegistrationNo", post).addTextNode("");
            instituteDetailsElem.addChildElement("EnrolmentStatus", post).addTextNode("");
            //instituteDetailsElem.addChildElement("CourseStartDate", post).addTextNode(instituteDetails.getCourseStartDate());
            //instituteDetailsElem.addChildElement("CourseEndDate", post).addTextNode(instituteDetails.getCourseEndDate());
            //instituteDetailsElem.addChildElement("CertificationDate", post).addTextNode(instituteDetails.getCertificationDate());
        
            // ...

//            DemographicInfo demographicInfo = retailCustomerCreate.getDemographicInfo();

	         // Create DemographicInfo element
	         SOAPElement demographicInfoElem = retailCustomerCreateRq.addChildElement("DemographicInfo", post);
	
	         // Create EduDtlsInfo element as child
	         SOAPElement eduDtlsInfoElem = demographicInfoElem.addChildElement("EduDtlsInfo" , post);
	
	         // Add elements for EduDtlsInfo (include all elements)
	         eduDtlsInfoElem.addChildElement("EnrollmentStatusStartDate" ,post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("SeparationDate", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("Qualification", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("SchoolCode", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("CampusCode", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("InstituteUniversity", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("EnrolmentStatus", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("CertificationDate", post).addTextNode("");
	         eduDtlsInfoElem.addChildElement("StudentRegistrationNo", post).addTextNode("");
	
	         // ...
	
//	         EmploymentDetails employmentDetails = retailCustomerCreate.getEmploymentDetails();

	      // Create EmploymentDetails element
	      SOAPElement employmentDetailsElem = retailCustomerCreateRq.addChildElement("EmploymentDetails", post);
	
	      // Add elements for EmploymentDetails (include all elements)
	      employmentDetailsElem.addChildElement("EmploymentType", post).addTextNode("Unemployed");
	      employmentDetailsElem.addChildElement("EmploymentStatus", post).addTextNode("");
	      employmentDetailsElem.addChildElement("EmpType", post).addTextNode("CURRENT_EMPLOYMENT");
	      employmentDetailsElem.addChildElement("IncomeNature", post).addTextNode("Stable");
	      employmentDetailsElem.addChildElement("IndustryType", post).addTextNode("");
	      employmentDetailsElem.addChildElement("PaymentMode", post).addTextNode("");
	      employmentDetailsElem.addChildElement("EmployerID", post).addTextNode("");
	      employmentDetailsElem.addChildElement("Occupation", post).addTextNode("");
	      employmentDetailsElem.addChildElement("Designation", post).addTextNode("");
	      //employmentDetailsElem.addChildElement("EmploymentStartDate", post).addTextNode(employmentDetails.getEmploymentStartDate());
	      employmentDetailsElem.addChildElement("Pensioner", post).addTextNode("");
	      employmentDetailsElem.addChildElement("BankRelationType", post).addTextNode("");
	      employmentDetailsElem.addChildElement("EmployerName", post).addTextNode("");
	      employmentDetailsElem.addChildElement("PhoneNumber", post).addTextNode("");
	      employmentDetailsElem.addChildElement("EmployerCode", post).addTextNode("");

     
	      // ...

	      KYCDetails kycDetails = retailCustomerCreate.getKycDetails();

		   // Create KYCDetails element
		   SOAPElement kycDetailsElem = retailCustomerCreateRq.addChildElement("KYCDetails", post);
	
		   // Add elements for KYCDetails (include all elements)
		   kycDetailsElem.addChildElement("KYCStatus", post).addTextNode("");
		   //kycDetailsElem.addChildElement("KYCDate", post).addTextNode(kycDetails.getKycDate());
//		   kycDetailsElem.addChildElement("submittedForKYCIndicator", post).addTextNode(kycDetails.getSubmittedForKYCIndicator());
		   kycDetailsElem.addChildElement("SubmittedForKYCIndicator", post).addTextNode("");
		   //kycDetailsElem.addChildElement("KYCRecertificationDate", post).addTextNode(kycDetails.getKycRecertificationDate());
		   kycDetailsElem.addChildElement("MainSourceOfFunds", post).addTextNode(personalPartyBasicDetails.getSourceOfFunds());
		   kycDetailsElem.addChildElement("OtherSourceOfFunds", post).addTextNode("");
		   kycDetailsElem.addChildElement("OtherBankName", post).addTextNode("");
		   kycDetailsElem.addChildElement("PreferredCommunicationLanguage", post).addTextNode("");
		   kycDetailsElem.addChildElement("CustomerRating", post).addTextNode("");
		   
//		   kycDetailsElem.addChildElement("KYCStatus", post).addTextNode("");
//		   kycDetailsElem.addChildElement("KYCDate", post).addTextNode("");
//		   kycDetailsElem.addChildElement("KYCRecertificationDate", post).addTextNode("");
	

		   // ...

//		   TaxDetails taxDetails = retailCustomerCreate.getTaxDetails();

			// Create TaxDetails element
			SOAPElement taxDetailsElem = retailCustomerCreateRq.addChildElement("TaxDetails", post);
		
	
			// Add elements for TaxDetails (include all elements)
			taxDetailsElem.addChildElement("TaxDeductionTable", post).addTextNode("");
			taxDetailsElem.addChildElement("TaxCountry", post).addTextNode("");
			taxDetailsElem.addChildElement("TaxExempt", post).addTextNode("");
			taxDetailsElem.addChildElement("ForeignTaxReportingIndicator", post).addTextNode("");
			taxDetailsElem.addChildElement("ForeignTaxReportinStatus", post).addTextNode("");
			taxDetailsElem.addChildElement("ForeignTaxReportingCountry", post).addTextNode("");
			//taxDetailsElem.addChildElement("ForeignTaxReportingReviewDate", post).addTextNode(taxDetails.getForeignTaxReportingReviewDate());
			
	
	
			// ...

		

			// ...


            // Add more elements as necessary

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "Rtcustadd" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);
            

            SOAPMessage.saveChanges();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
//            String soapEnv = new String(out.toByteArray());
            System.out.println("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            // Send the SOAP request to the desired endpoint
            SOAPMessageResponse = postCreateRetailCustomer(SOAPMessage, retailCustomerCreateEndpoint, soaPassword);

            // Handle the response
//            log.info("\n Response  is  for ID  " + SOAPMessageResponse);

        } catch (Exception ex) {
            // Handle exceptions
            Logger.getLogger(CreateRetailCustomer.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling the SOAP service: " + ex);
        }

        return SOAPMessageResponse;
    }


}
