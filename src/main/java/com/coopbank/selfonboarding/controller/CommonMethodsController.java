package com.coopbank.selfonboarding.controller;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.NodeList;

import com.coopbank.selfonboarding.request.AccountDetailsRequest;
import com.coopbank.selfonboarding.request.CreateDocumentRequest;
import com.coopbank.selfonboarding.request.CustomerAccountDetailsInquiryRequest;
import com.coopbank.selfonboarding.request.CustomerBlacklistRequest;
import com.coopbank.selfonboarding.request.CustomerDetailsSummaryRequest;
import com.coopbank.selfonboarding.request.CustomerIDRequest;
import com.coopbank.selfonboarding.request.IprsRequest;
import com.coopbank.selfonboarding.request.RetailCustomerCreate;
import com.coopbank.selfonboarding.request.SanctionDetailsRequest;
import com.coopbank.selfonboarding.request.SendEmailRequest;
import com.coopbank.selfonboarding.request.SendSMSRequest;
import com.coopbank.selfonboarding.request.SigningDetailsRequest;
import com.coopbank.selfonboarding.request.ValidatePinRequest;
import com.coopbank.selfonboarding.request.SigningDetailsData.SignatoryDetail;
import com.coopbank.selfonboarding.request.SigningDetailsData.SignatoryDetails;
import com.coopbank.selfonboarding.request.retailCustomerCreate.PersonalPartyBasicDetails;
import com.coopbank.selfonboarding.soa.services.bean.AccountCreate;
import com.coopbank.selfonboarding.soa.services.bean.AccountDetails;
import com.coopbank.selfonboarding.soa.services.bean.ConnectCabinet;
import com.coopbank.selfonboarding.soa.services.bean.CreateDocument;
import com.coopbank.selfonboarding.soa.services.bean.CreateRetailCustomer;
import com.coopbank.selfonboarding.soa.services.bean.CustomerAccountDetailsInquiry;
import com.coopbank.selfonboarding.soa.services.bean.CustomerBlacklist;
import com.coopbank.selfonboarding.soa.services.bean.CustomerDetailsSummary;
import com.coopbank.selfonboarding.soa.services.bean.CustomerID;
import com.coopbank.selfonboarding.soa.services.bean.IprsApis;
import com.coopbank.selfonboarding.soa.services.bean.SanctionDetails;
import com.coopbank.selfonboarding.soa.services.bean.SendEmail;
import com.coopbank.selfonboarding.soa.services.bean.SendSMS;
import com.coopbank.selfonboarding.soa.services.bean.SigningDetails;
import com.coopbank.selfonboarding.soa.services.bean.ValidatePin;
import com.coopbank.selfonboarding.util.CommonMethods;
import com.coopbank.selfonboarding.util.PdfConverterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonMethodsController {

	@Value("${api.IPRS.ENDPOINT_URL}")
	String iprsEndpoint;
	
	@Value("${api.IPRS.USERNAME}")
	String iprsUserName;
	
	@Value("${api.IPRS.PASSWORD}")
	String iprsPassword;
	
	@Value("${api.SANCTION.USERNAME}")
	String sanctionUserName;
	
	@Value("${api.SANCTION.PASSWORD}")
	String sanctionPassword;
	
	@Value("${api.BLACKLIST.ENDPOINT_URL}")
	String blacklistEndpoint;
	
	@Value("${api.CUSTOMERID.ENDPOINT_URL}")
	String customerIDEndpoint;
	
	@Value("${api.SMS.ENDPOINT_URL}")
	String smsEndpoint;
	@Value("${api.SMS.USERNAME}")
	String smsUsername;
	@Value("${api.SMS.PASSWORD}")
	String smsPassword;
	@Value("${api.SMS.ENCRYPED}")
	String smsEncryped;
	@Value("${api.SMS.CLIENTID}")
	String smsClientID;
	
	@Value("${api.SOA.USERNAME}")
	String soaUsername;
	
	@Value("${api.SOA.PASSWORD}")
	String soaPassword;
	
	@Value("${api.SOA.SYSTEMCODE}")
	String soaSystemCode;
	
	@Value("${api.KRA.ENDPOINT_URL}")
	String kraEndpoint;
	
	@Value("${api.EMAIL.ENDPOINT_URL}")
	String emailEndpoint;
	
	@Value("${api.ACCDETAILS.ENDPOINT_URL}")
	String accDetailsEndpoint;
	
	@Value("${api.SANCTIONDETAILS.ENDPOINT_URL}")
	String sanctionDetailsEndpoint;
	
	@Value("${api.CUSTOMERACCDETAILSINQUIRY.ENDPOINT_URL}")
	String customerDetailsInquiryEndpoint;
	
	@Value("${api.CUSTOMERACCDETAILSSUMMARY.ENDPOINT_URL}")
	String custDetailsSummaryEndpoint;
	
	@Value("${api.ACCOUNTCREATE.ENDPOINT_URL}")
	String accountCreateEndpoint;
	
	@Value("${api.SIGNINGDETAILS.ENDPOINT_URL}")
	String signingDetailsEndpoint;
	
	@Value("${api.CONNECTCABINET.ENDPOINT_URL}")
	String connectCabinetEndpoint;
	
	@Value("${api.CREATEDOCUMENT.ENDPOINT_URL}")
	String createDocumentEndpoint;
	
	@Value("${api.CONNECTCABINET.CABINETNAME}")
	String connectCabinetName;
	
	@Value("${api.CONNECTCABINET.USERNAME}")
	String connectCabinetUserName;
	
	@Value("${api.CONNECTCABINET.PASSWORD}")
	String connectCabinetPassword;
	
	@Value("${api.RETAILCUSTOMERCREATE.ENDPOINT_URL}")
	String retailCustomerCreateEndpoint;
	
	String coopLogoheader = "https://kikimodev.com/coop/coopselfheader.png";


	ObjectMapper objectMapper = new ObjectMapper();
	
	
//	----------------------- Start IPRS Details ----------------------- //
	public static int INDENTATION = 4;
	@PostMapping("/iprsDetails")
	public ResponseEntity<Object> postIprsDetails(@RequestBody IprsRequest iprsData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(iprsData);
		log.info("Request IPRS " + requestJson);
		Map<String, Object> map = new HashMap<String, Object>();
		SOAPMessage soapResponse = IprsApis.getKslIprsDetails(iprsUserName,iprsPassword,iprsData.getDoc_type(), iprsData.getDoc_number(), iprsData.getSerial_number(),iprsEndpoint,soaUsername,soaPassword,soaSystemCode);
		String status = "";

        String Description = "";
        if (soapResponse == null) {
            status = "3";
            Description = "Failed to get response From Core Banking ";
            log.debug("We got a Null SoapResponse from Balance API Call");
            
            map.put("status", status);
            map.put("Description", Description);
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	log.info("--------------------------------- Response Header ---------------------------------");
        	log.info("Response Header " + header.toString());
        	log.info("--------------------------------- Response Header ---------------------------------");
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(4).getNodeName());
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		           String xmlString = CommonMethods.convertToString(sb);
        		          
        		            xmlString=xmlString.replace("http://schemas.datacontract.org/2004/07/IPRSManager/CommonData","");
        		            xmlString=xmlString.replace(" xmlns:ns012=\"urn://co-opbank.co.ke/Banking/External/IPRS/1.0\"","");
        		            xmlString=xmlString.replace("ns012:","");        		            
        		            xmlString=xmlString.replace("xmlns:ns6","");
        		            xmlString=xmlString.replace("=","");
        		            xmlString=xmlString.replace("ns6:","");
        		            xmlString=xmlString.replaceAll("\"", "");
        		            xmlString=xmlString.replaceAll(" >", ">");
        		            
//        		            log.info(xmlString);
          		           try {
                           JSONObject jsonObj = XML.toJSONObject(xmlString);
                           String json = jsonObj.toString(INDENTATION); 
                              map.put("Status", "true");
                              map.put("StatusDescription", statusDesc);
                              map.put("Response", json); 
                                                            
                           } catch (JSONException ex) {
                               ex.printStackTrace();
                           }
        		            
                           
        			 } else {
        				 map.put("status", statusDesc);
        				 map.put("Description", Description);
                     }
        		 } else {
        			 Description = "We got an invalid Response";
        			 map.put("Description", Description);
                     log.info("We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End IPRS Details ----------------------- //
	
//	----------------------- Start Blacklist Details ----------------------- //
	@GetMapping("/blacklistDetails")
	public ResponseEntity<Object> getblacklistDetails(@RequestBody CustomerBlacklistRequest blacklistData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(blacklistData);
		log.info("Request blacklistData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerBlacklist.getCustomerBlacklistDetails(blacklistData.getIdentificationType(), blacklistData.getIdentificationNumber(),blacklistEndpoint,soaUsername,soaPassword,soaSystemCode);
		String status = "";
        String Description = "";
        
        if (soapResponse == null) {
            status = "Failed";
            Description = "Failed to get response for customer Blacklist";
            log.debug("Failed to get response for customer Blacklist");
            
            map.put("status", status);
            map.put("Description", Description);
        } 
        else {

        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

        	log.info(xmlStringHeader);
        	
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(3).getNodeName());
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Not found/Not negated")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		           String xmlString = CommonMethods.convertToString(sb);
//        		           log.info(xmlString);

        		           map.put("Status", "true");
        		           map.put("StatusDescription", statusDesc);
        		           map.put("MessageDescription", messageDescriptionRes);
 
        			 }else if (statusDesc.equals("Failed")) {
        				 map.put("Status", "false");
      		             map.put("StatusDescription", statusDesc);
      		             map.put("MessageDescription", messageDescriptionRes);
        			 } 
        			 else {
        				 map.put("status", "false");
        				 map.put("StatusDescription", Description);
        				 map.put("MessageDescription", messageDescriptionRes);
                     }
        		 } else {
        			 
        			 map.put("status", "false");
    				 map.put("StatusDescription", "We got an invalid Response");
    				 map.put("MessageDescription", "Failed to get response for customer Blacklist");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Blacklist Details ----------------------- //
	
	
//	----------------------- Start CustomerID Details ----------------------- //
	@GetMapping("/CustomerID")
	public ResponseEntity<Object> getCustomerIDData(@RequestBody CustomerIDRequest ncustomerIDData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(ncustomerIDData);
		log.info("Request customerIDData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		String customerIdRes = null;
		String isExist = "false";
		SOAPMessage soapResponse = CustomerID.getCustomerIDDetails(ncustomerIDData.getUniqueIdentifierType(),ncustomerIDData.getUniqueIdentifierValue(),customerIDEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	
         	SOAPHeader header = soapResponse.getSOAPHeader();
         	
         	String xmlStringHeader = CommonMethods.convertHeaderString(header);
         	
         	
         	xmlStringHeader = xmlStringHeader.replace("head:",""); 
         	
         	String statusDescriptionTag = "StatusDescription";
             String statusDescriptionRes = xmlStringHeader.split("<"+ statusDescriptionTag +">")[1].split("</"+ statusDescriptionTag+">")[0];
             
             String messageDescriptionRes = null;
             
             if(statusDescriptionRes.equals("Failed")) {
    	        	String messageDescriptionTag = "MessageDescription";
    	            messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];
    			}
            

         	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");

        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerId.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns29","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns29:","");
  		            
  		          log.info("Response: " + xmlString);
  		            
  		            
  		          String CustomerIdTag = "CustomerId";
  		          customerIdRes = xmlString.split("<" + CustomerIdTag + ">")[1].split("</" + CustomerIdTag + ">")[0];
  		        log.info("customerIdRes: " + customerIdRes);
  		        if (customerIdRes != null && !customerIdRes.isEmpty()) {
  		          isExist = "true";
	  		      } else {
	  		          isExist = "false";
	  		      }
	  		          
  		          log.info("CustomerId: " + customerIdRes);
  		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    map.put("isExist", isExist);
                    map.put("CustomerId", customerIdRes);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDescriptionRes);
        	             map.put("Response", messageDescriptionRes);
        	             map.put("isExist", isExist);
        	             map.put("CustomerId", customerIdRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Response", messageDescriptionRes);
    	             map.put("isExist", isExist);
    	             map.put("CustomerId", customerIdRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	

//	----------------------- End CustomerID Details ----------------------- //
	
	private CustomerIDRequest createCustomerIDRequest(String uniqueIdentifierType, String uniqueIdentifierValue) {
		CustomerIDRequest CustomerIDRequestData = new CustomerIDRequest();
		CustomerIDRequestData.setUniqueIdentifierType(uniqueIdentifierType);
		CustomerIDRequestData.setUniqueIdentifierValue(uniqueIdentifierValue);
	    return CustomerIDRequestData;
	}
	
	
//	----------------------- Start SendSMS Details ----------------------- //
	@PostMapping("/Sendsms")
	public ResponseEntity<Object> postSendSMSReq(@RequestBody SendSMSRequest sendSMSData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(sendSMSData);
		log.info("Request sendSMSData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SendSMS.postSendSMS(smsUsername,smsPassword,smsEncryped,smsClientID,sendSMSData.getReqmessage(),sendSMSData.getReqmsisdn(),smsEndpoint,soaUsername,soaPassword,soaSystemCode);
        
        if (soapResponse == null) {
      	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		            String xmlString = CommonMethods.convertToString(sb);             		        
        		            String descriptionTag = "description";
        		            String description = xmlString.split("<"+ descriptionTag +">")[1].split("</"+ descriptionTag+">")[0];
        		            
	        		           map.put("Status", statusDesc);
	                           map.put("Description", description);

        			 } else {
        				 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	
	private SendSMSRequest createSendSMSRequest(String phoneNumber, String message) {
	    // Create a SendSMSRequest object with the required data
	    SendSMSRequest sendSMSData = new SendSMSRequest();
	    // Set other required properties of sendSMSData based on your implementation
	    sendSMSData.setReqmsisdn(phoneNumber);
	    sendSMSData.setReqmessage(message);
	    return sendSMSData;
	}
	
//	----------------------- End SendSMS Details ----------------------- //
	
//	----------------------- Start ValidatePin Details ----------------------- //
	@GetMapping("/validatepin")
	public ResponseEntity<Object> getValidatePinData(@RequestBody ValidatePinRequest validatePinData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(validatePinData);
		log.info("Request validatePinData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = ValidatePin.getKslValidatePin(validatePinData.getIdNumber(),validatePinData.getTaxPayerType(),kraEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(2).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(2).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerId.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns29","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns29:","");
//      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End ValidatePin Details ----------------------- //
	
//	----------------------- Start SendEmail Details ----------------------- //
	@PostMapping("/sendemail")
	public ResponseEntity<Object> postSendEmailReq(@RequestBody SendEmailRequest sendEmailData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(sendEmailData);
		log.info("Request sendEmailData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SendEmail.postKslSendEmail(sendEmailData.getFrom(),sendEmailData.getTo(),sendEmailData.getMessage(),sendEmailData.getSubject(),emailEndpoint,soaUsername,soaPassword,soaSystemCode);
		
        if (soapResponse == null) {
      	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	NodeList returnList = (NodeList) header.getElementsByTagName("tns1:HeaderReply");

        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("tns1:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
        		            String xmlString = CommonMethods.convertToString(sb);  
//        		            log.info("Send Email "+xmlString);
        		            xmlString=xmlString.replace("xmlns:ns1=\"urn://co-opbank.co.ke/Banking/Common/DataModel/CommonEmail/Send/1.0/CommonEmail.send","");
          		            xmlString=xmlString.replace("ns1:","");
          		            
        		            String operationDateTag = "OperationDate";
        		            String operationDateRes = xmlString.split("<"+ operationDateTag +">")[1].split("</"+ operationDateTag+">")[0];
	        		           map.put("Status", statusDesc);
	        		           map.put("Response", "Email Sent!");
	                           map.put("OperationDate", operationDateRes);

        			 } else {
        				 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", "We got an invalid Response");
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", "We got an invalid Response");
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	
	private SendEmailRequest createSendEmailRequest(String toEmail, String emailSubject, String emailMessage) {
		SendEmailRequest SendEmailData = new SendEmailRequest();
		SendEmailData.setFrom("mmuthigani@co-opbank.co.ke");
		SendEmailData.setSubject(emailSubject);
		SendEmailData.setTo(toEmail);
		SendEmailData.setMessage(emailMessage);
	    return SendEmailData;
	}
//	----------------------- End SendEmail Details ----------------------- //
	
//	----------------------- Start AccountDetails Details ----------------------- //
	@GetMapping("/accountDetails")
	public ResponseEntity<Object> getAccountDetailsData(@RequestBody AccountDetailsRequest AccountDetailsData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(AccountDetailsData);
		log.info("Request AccountDetailsData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = AccountDetails.getAccountDetailsDetails(AccountDetailsData.getAccountNumber(),accDetailsEndpoint,soaUsername,soaPassword,soaSystemCode);
		map.put("StatusDescription", "Service timeout");
        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
         	
         	String xmlStringHeader = CommonMethods.convertHeaderString(header);
         	
         	
         	xmlStringHeader = xmlStringHeader.replace("head:",""); 
         	
         	String statusDescriptionTag = "StatusDescription";
             String statusDescriptionRes = xmlStringHeader.split("<"+ statusDescriptionTag +">")[1].split("</"+ statusDescriptionTag+">")[0];
             
             String messageDescriptionRes = null;
             
             if(statusDescriptionRes.equals("Failed")) {
    	        	String messageDescriptionTag = "MessageDescription";
    	            messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];
    			}
            

         	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
		            xmlString=xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
		            xmlString=xmlString.replace("xmlns:tns25=\"urn://co-opbank.co.ke/BS/Account/BSAccountDetails.3.0\"","");
      		        xmlString=xmlString.replace("xmlns:tns25\"urn://co-opbank.co.ke/BS/Account/BSAccountDetails.3.0\"","");
  		            xmlString=xmlString.replace("tns25:","");
     		            
  		          try {
    		        	JSONObject jsonObj = XML.toJSONObject(xmlString);
                       String json = jsonObj.toString(INDENTATION);
              
                      map.put("Status", "true");
                      map.put("StatusDescription", statusDesc);
                      map.put("Response", json);
                   
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
       			 } else {
           			 map.put("Status", "false");
       	             map.put("StatusDescription", statusDesc);
       	             map.put("Description", messageDescriptionRes);
                    }
       		 } else {
       			 map.put("Status", "false");
   	             map.put("StatusDescription", "Failed");
   	             map.put("Description", messageDescriptionRes);
                }
       	}
       }

  		return new ResponseEntity<Object>(map, HttpStatus.OK);

  	}
//	----------------------- End AccountDetails Details ----------------------- //
	
	
//	----------------------- Start SanctionDetails Details ----------------------- //
	@PostMapping("/sanctionDetails")
	public ResponseEntity<Object> postSanctionDetails(@RequestBody SanctionDetailsRequest sanctionDetailsData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(sanctionDetailsData);
		log.info("Request sanctionDetailsData " + requestJson);
		Map<String, Object> map = new HashMap<String, Object>();
		SOAPMessage soapResponse = SanctionDetails.getCustomerSanctionDetails(sanctionDetailsData.getMinMatchScore(),sanctionDetailsData.getCustomerType(),sanctionDetailsData.getFullName(),sanctionDetailsData.getFirstName(),sanctionDetailsData.getMiddleName(),sanctionDetailsData.getLastName(),sanctionDetailsData.getIdentificationDocType(),sanctionDetailsData.getIdentificationDocNo(),sanctionDetailsData.getNationality(),sanctionDetailsData.getDateOfBirth(),sanctionDetailsData.getCountryOfBirth(),sanctionDetailsEndpoint,sanctionUserName,sanctionPassword,soaSystemCode);

        String Description = "";
        if (soapResponse == null) {
            map.put("status", "false");
            map.put("StatusDescription", "Failed");
            map.put("Description", "Failed to get response From Core Banking ");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	SOAPBody sb = soapResponse.getSOAPBody();
        	
        	String xmlString = CommonMethods.convertToString(sb);
	           
        	SOAPHeader hd = soapResponse.getSOAPHeader();
        	String xmlStringHeader = CommonMethods.convertHeaderString(hd);
	           
	           log.info(xmlString);
	           log.info(xmlStringHeader);
	           xmlStringHeader = xmlStringHeader.replace("head:",""); 
	           
	            xmlString=xmlString.replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"","");
	            xmlString=xmlString.replace(" xmlns:ns7=\"urn://co-opbank.co.ke/DataModel/Customer/SanctionDetails/Get/1.0\"","");
	            xmlString=xmlString.replace("ns7:","");        		            
	            xmlString=xmlString.replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"","");
	            xmlString=xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
	            
//	            log.info(xmlString);
//	            log.info(xmlStringHeader);      	
        	
	            String messageDescriptionTag = "MessageDescription";
	            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

	            JSONObject jsonObj = XML.toJSONObject(xmlString);
                String json = jsonObj.toString(INDENTATION);
        	log.info("--------------------------------- Response Header ---------------------------------");
        	log.info("Response Header " + header.toString());
        	log.info("--------------------------------- Response Header ---------------------------------");
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		log.info("Log resp"+ innerResultList.item(4).getNodeName());
        		 if (innerResultList.item(4).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(4).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
          		           try {
                              map.put("Status", "true");
                              map.put("StatusDescription", statusDesc);
                              map.put("MessageDescription", messageDescriptionRes);
                              map.put("Response", json); 
                              
                                                            
                           } catch (JSONException ex) {
                               ex.printStackTrace();
                           }
        			 } else {
        				
        				 map.put("Status", "false");
                         map.put("StatusDescription", statusDesc);
                         map.put("MessageDescription", messageDescriptionRes);
                         map.put("Response", json);
                     }
        		 } else {
        			 map.put("Status", "false");
                     map.put("StatusDescription", "Failed");
                     map.put("MessageDescription", Description);
                     map.put("Response", json);
                 }

        	}

        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End SanctionDetails Details ----------------------- //
	
	
//	----------------------- Start Customer Account Details Inquiry ----------------------- //
	@GetMapping("/CustomerAccountDetailsInquiry")
	public ResponseEntity<Object> getCustomerAccountDetailsInquiryData(@RequestBody CustomerAccountDetailsInquiryRequest custAccDetailsInqReqData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(custAccDetailsInqReqData);
		log.info("Request CustomerAccountDetailsInquiryData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerAccountDetailsInquiry.getCustomerAccountDetailsInquiry(custAccDetailsInqReqData.getCustomerId(),customerDetailsInquiryEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
         	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

            
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("\"urn://co-opbank.co.ke/BS/Customer/CustomerAccount.Get.3.0\"","");
  		            xmlString=xmlString.replace("xmlns:tns28","");
  		            xmlString=xmlString.replace("=","");
  		            xmlString=xmlString.replace("tns28:","");
  		            xmlString=xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>","");
//      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Customer Account Details Inquiry ----------------------- //

	
//	----------------------- Start Customer Details Summary ----------------------- //
	@GetMapping("/CustomerDetailsSummary")
	public ResponseEntity<Object> getCustomerAccountSummaryData(@RequestBody CustomerDetailsSummaryRequest custAccSummaryReqData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(custAccSummaryReqData);
		log.info("Request CustomerAccountDetailsInquiryData " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = CustomerDetailsSummary.getcustomerDetailsSummaryReq(custAccSummaryReqData.getCustomerId(),custDetailsSummaryEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];

            
        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        				 SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:tns28=\"urn://co-opbank.co.ke/BS/Customer/CustomerDetailsSummary.1.0\"","");
  		            xmlString=xmlString.replace(" xmlns:tns30=\"urn://co-opbank.co.ke/TS/Finacle/CustomerDetailsSummary.Get.1.0\"","");
  		            xmlString=xmlString.replace("tns28:","");
  		            xmlString=xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>","");
//      		        log.info(xmlString);
      		            
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
//	----------------------- End Customer Details Summary ----------------------- //
	
//	----------------------- Start Account Create ----------------------- //
	public Map<String, String> getAccountCreateData(String schemeCode, String product, String customerCode, String sourceOfFunds) throws Exception {
	    HashMap<String, String> map = new HashMap<>();
	    SOAPMessage soapResponse = AccountCreate.postAccountCreateReq(schemeCode, product, customerCode, sourceOfFunds, accountCreateEndpoint, soaUsername, soaPassword, soaSystemCode);
	    String messageDescriptionRes = null;
	    String accountIDRes = null;

	    if (soapResponse == null) {
	        map.put("Status", "false");
	        map.put("StatusDescription", "Failed");
	        map.put("Description", "Null response");
	    } else {
	        SOAPHeader header = soapResponse.getSOAPHeader();

	        String xmlStringHeader = CommonMethods.convertHeaderString(header);
	        xmlStringHeader = xmlStringHeader.replace("head:", "");
	        xmlStringHeader = xmlStringHeader.replace("tns3:", "");

	        String messageDescriptionTag = "MessageDescription";
	        messageDescriptionRes = xmlStringHeader.split("<" + messageDescriptionTag + ">")[1].split("</" + messageDescriptionTag + ">")[0];

	        NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");

	        for (int k = 0; k < returnList.getLength(); k++) {
	            NodeList innerResultList = returnList.item(k).getChildNodes();
	            if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
	                String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
	                if (statusDesc.equals("Success")) {
	                    SOAPBody sb = soapResponse.getSOAPBody();
	                    String xmlString = CommonMethods.convertToString(sb);
	                    xmlString = xmlString.replace(" xmlns:ns25=\"urn://co-opbank.co.ke/DataModel/Acccount/AccountCreate/Post/2.0\"", "");
	                    xmlString = xmlString.replace(" xmlns:tns30=\"urn://co-opbank.co.ke/TS/Finacle/CustomerDetailsSummary.Get.1.0\"", "");
	                    xmlString = xmlString.replace("ns25:", "");
	                    xmlString = xmlString.replace("<?xml version\"1.0\" encoding\"UTF-8\" standalone\"no\"?>", "");

	                    String accountIDTag = "AccountID";
	                    accountIDRes = xmlString.split("<" + accountIDTag + ">")[1].split("</" + accountIDTag + ">")[0];

	                    try {
	                        JSONObject jsonObj = XML.toJSONObject(xmlString);
	                        String json = jsonObj.toString(INDENTATION);

	                        map.put("Status", "true");
	                        map.put("StatusDescription", statusDesc);
	                        map.put("Response", json);
	                        map.put("Description", accountIDRes);
	                    } catch (JSONException ex) {
	                        ex.printStackTrace();
	                    }
	                } else {
	                    map.put("Status", "false");
	                    map.put("StatusDescription", statusDesc);
	                    map.put("Response", messageDescriptionRes);
	                    map.put("Description", messageDescriptionRes);
	                }
	            } else {
	                map.put("Status", "false");
	                map.put("StatusDescription", "Failed");
	                map.put("Response", messageDescriptionRes);
	                map.put("Description", messageDescriptionRes);
	            }
	        }
	    }

	    return map;
	}

  //	----------------------- End Account Create----------------------- //
	
//	----------------------- Start Signing Details ----------------------- //
	@PostMapping("/SigningDetails")
	public ResponseEntity<Object> getSigningDetailsData(@RequestBody SigningDetailsRequest signingDetailsReqData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(signingDetailsReqData);
		log.info("Request SigningDetails " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = SigningDetails.getSigningDetailsReq(signingDetailsReqData,signingDetailsEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	xmlStringHeader = xmlStringHeader.replace("head:",""); 
        	xmlStringHeader = xmlStringHeader.replace("tns3:",""); 
        	
        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];


        	NodeList returnList = (NodeList) header.getElementsByTagName("head:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:ns8=\"urn://co-opbank.co.ke/BS/DataModel/Account/SigningDetails/Post/2.0\"","");
  		            xmlString=xmlString.replace("ns8:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
//      		        log.info(xmlString);
     		            
     		        try {
                     JSONObject jsonObj = XML.toJSONObject(xmlString);
                     String json = jsonObj.toString(INDENTATION);
            
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	
	
	private SigningDetailsRequest createSigningDetailsRequest(String accountNumber, String customerName, String signature, String photo, String remarks) {
	    // Create a SigningDetailsRequest object with the required data
	    SigningDetailsRequest signingDetailsRequestData = new SigningDetailsRequest();
	    signingDetailsRequestData.setAccountNumber(accountNumber);
	    signingDetailsRequestData.setBankCode("01");
	    signingDetailsRequestData.setCustomerName(customerName);

	    SignatoryDetails signatoryDetails = new SignatoryDetails();
	    
	    // Create a list to hold SignatoryDetail objects
	    List<SignatoryDetail> signatoryDetailList = new ArrayList<>();

	    SignatoryDetail signatoryDetail = new SignatoryDetail();
	    signatoryDetail.setSignature(signature);
	    signatoryDetail.setPhoto(photo);
	    signatoryDetail.setSignEffectiveDate("");
	    signatoryDetail.setRemarks(remarks);

	    // Add the signatoryDetail to the list
	    signatoryDetailList.add(signatoryDetail);

	    // Set the list of signatoryDetail in signatoryDetails
	    signatoryDetails.setSignatoryDetail(signatoryDetailList);

	    // Set the signatoryDetails in signingDetailsRequestData
	    signingDetailsRequestData.setSignatoryDetails(signatoryDetails);

	    return signingDetailsRequestData;
	}

	//	----------------------- End Signing Details ----------------------- //
	
	
	//	----------------------- Start Connect Cabinet----------------------- //
	public String getConnectCabinetData() throws Exception {
		HashMap<String, String> map = new HashMap<>();
		SOAPMessage soapResponse = ConnectCabinet.postConnectCabinetReq(connectCabinetName,connectCabinetUserName,connectCabinetPassword,connectCabinetEndpoint,soaUsername,soaPassword,soaSystemCode);
		String userDBIdTagRes = null;
        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	
        	xmlStringHeader = xmlStringHeader.replace("tns33:",""); 

        	String messageDescriptionTag = "MessageDescription";
            String messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];


        	NodeList returnList = (NodeList) header.getElementsByTagName("tns33:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("tns33:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace(" xmlns:ns12=\"urn://co-opbank.co.ke/DataModel/DocMgt/ConnectCabinet/Post/1.0/Body\"","");
  		            xmlString=xmlString.replace("ns12:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
      		        
      		      String userDBIdTag = "UserDBId";
                  userDBIdTagRes = xmlString.split("<"+ userDBIdTag +">")[1].split("</"+ userDBIdTag+">")[0];

     		            
     		        try {
                    map.put("Status", "true");
                    map.put("StatusDescription", statusDesc);
                    map.put("UserDBId", userDBIdTagRes);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return userDBIdTagRes;

	}
	//	----------------------- End Connect Cabinet ----------------------- //
	
	
//	----------------------- Start Create Document----------------------- //
	@PostMapping("/CreateDocument")
	public ResponseEntity<Object> getCreateDocumentData(@RequestBody CreateDocumentRequest createDocumentReqData) throws Exception {
		String requestJson = objectMapper.writeValueAsString(createDocumentReqData);
		log.info("Request CreateDocument " + requestJson);
		HashMap<String, String> map = new HashMap<>();
		String userId = getConnectCabinetData();
		if (userId == null) {            
	     	   map.put("Status", "false");
	           map.put("StatusDescription", "Failed");
	           map.put("Description", "Connect Cabinet Service error");
	        } 
		SOAPMessage soapResponse = CreateDocument.postCreateDocumentReq(createDocumentReqData,userId,createDocumentEndpoint,soaUsername,soaPassword,soaSystemCode);

        if (soapResponse == null) {            
     	   map.put("Status", "false");
           map.put("StatusDescription", "Failed");
           map.put("Description", "Null response");
        } 
        else {
        	SOAPHeader header = soapResponse.getSOAPHeader();
        	
        	String xmlStringHeader = CommonMethods.convertHeaderString(header);
        	
        	
        	xmlStringHeader = xmlStringHeader.replace("tns33:",""); 
        	
        	String statusDescriptionTag = "StatusDescription";
            String statusDescriptionRes = xmlStringHeader.split("<"+ statusDescriptionTag +">")[1].split("</"+ statusDescriptionTag+">")[0];
            String messageDescriptionRes = null;
            
            if(statusDescriptionRes.equals("Failed")) {
	        	String messageDescriptionTag = "MessageDescription";
	            messageDescriptionRes = xmlStringHeader.split("<"+ messageDescriptionTag +">")[1].split("</"+ messageDescriptionTag+">")[0];
			}


        	NodeList returnList = (NodeList) header.getElementsByTagName("tns33:ResponseHeader");
        
        	for (int k = 0; k < returnList.getLength(); k++) {
        		NodeList innerResultList = returnList.item(k).getChildNodes();
        		 if (innerResultList.item(3).getNodeName().equalsIgnoreCase("tns33:StatusDescription")) {
        			 String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
        			 if (statusDesc.equals("Success")) {
        			SOAPBody sb = soapResponse.getSOAPBody();
        				 
      		        String xmlString = CommonMethods.convertToString(sb);
      		        xmlString=xmlString.replace("xmlns:ns12=\"urn://co-opbank.co.ke/DataModel/DocMgt/CreateDocument/Post/1.0/Body\"","");
  		            xmlString=xmlString.replace("ns12:","");
  		            xmlString=xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","");
      		                 
     		        try {
     		        	JSONObject jsonObj = XML.toJSONObject(xmlString);
                        String json = jsonObj.toString(INDENTATION);
               
                       map.put("Status", "true");
                       map.put("StatusDescription", statusDesc);
                       map.put("Response", json);
                    
                 } catch (JSONException ex) {
                     ex.printStackTrace();
                 }
        			 } else {
            			 map.put("Status", "false");
        	             map.put("StatusDescription", statusDesc);
        	             map.put("Description", messageDescriptionRes);
                     }
        		 } else {
        			 map.put("Status", "false");
    	             map.put("StatusDescription", "Failed");
    	             map.put("Description", messageDescriptionRes);
                 }
        	}
        }

		return new ResponseEntity<Object>(map, HttpStatus.OK);

	}
	
	private CreateDocumentRequest createCreateDocumentRequest(String fullNames,String idNumber,String accNumberRes,String customerIDRes,String document) {
	    // Create a SigningDetailsRequest object with the required data
		CreateDocumentRequest createDocumentRequestData = new CreateDocumentRequest();
		createDocumentRequestData.setDocument(document);
		createDocumentRequestData.setAccName(fullNames);
		createDocumentRequestData.setAccNumber(accNumberRes);
		createDocumentRequestData.setCustNumber(customerIDRes);
		createDocumentRequestData.setIdNumberNumber(idNumber);
		
	    return createDocumentRequestData;
	}

	//	----------------------- End CreateDocument ----------------------- //
	
	
	
//	----------------------- Start Create Retail Customer ----------------------- //
	
	@PostMapping("/RetailCustomerCreate")
	public ResponseEntity<Object> postCreateRetailCustomer(@RequestBody RetailCustomerCreate retailCustomerCreate) {
	    try {
	    		    	
	        String requestJson = objectMapper.writeValueAsString(retailCustomerCreate);
	        log.info(" JSON Request RetailCustomerCreate: " + requestJson);
	        HashMap<String, String> map = new HashMap<>();
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap = isExistingCustomer(retailCustomerCreate);
	        String isExist = (String) responseMap.get("isExist");
		    String customerIDData = (String) responseMap.get("CustomerId");
	        
	        Boolean isExistBoolean = Boolean.parseBoolean(isExist);

	        if (isExistBoolean) {
	            handleExistingCustomer(retailCustomerCreate,customerIDData, map);
	        } else {
	            handleNewCustomer(retailCustomerCreate, map);
	        }

	        return new ResponseEntity<>(map, HttpStatus.OK);
	    } catch (Exception e) {
	        // Handle the exception appropriately (log or rethrow)
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private Map<String, Object> isExistingCustomer(RetailCustomerCreate retailCustomerCreate) throws Exception {
		HashMap<String, String> map = null;
	    CustomerIDRequest customerIDRequestData = createCustomerIDRequest(retailCustomerCreate.getPersonalPartyBasicDetails().getDocType(), retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber());
	    ResponseEntity<Object> responseEntity = getCustomerIDData(customerIDRequestData);
	    Map<String, Object> responseMap = (Map<String, Object>) responseEntity.getBody();
	    String isExist = (String) responseMap.get("isExist");
	    String customerIDData = (String) responseMap.get("CustomerId");

	    return responseMap;
	}



	private void handleExistingCustomer(RetailCustomerCreate retailCustomerCreate,String customerIDRes, HashMap<String, String> map) throws Exception {
//		log.info("---------------------------------------------------------------CustomerId:---------------------------------------------------------------");
//		log.info("CustomerId: " + customerIDRes);
//		log.info("---------------------------------------------------------------CustomerId:---------------------------------------------------------------");

  
	    Map<String, String> accountCreateResult = getAccountCreateData(
	            retailCustomerCreate.getPersonalPartyBasicDetails().getSchemeCode(),
	            retailCustomerCreate.getPersonalPartyBasicDetails().getProduct(),
	            customerIDRes,
	            retailCustomerCreate.getPersonalPartyBasicDetails().getSourceOfFunds());

	    String status = accountCreateResult.get("Status");
	    String statusDescription = accountCreateResult.get("StatusDescription");
	    String response = accountCreateResult.get("Response");
	    String accNumberRes = accountCreateResult.get("Description");
	    
	    log.info("accountCreateResult: " + accountCreateResult+"\n\nStatus: " + status);

	    handleSuccess(map, customerIDRes, accNumberRes, status, statusDescription, response);
	    
	    try {
	        // Attempt to parse the accNumberRes as an integer
	        long accNumber = Long.parseLong(accNumberRes);

	        // Check if the accNumber has exactly 14 digits
	        if (String.valueOf(accNumberRes).length() == 14) {
	            // accNumberRes is an integer with 14 digits
	            System.out.println("Valid accNumberRes: " + accNumberRes+" status "+status);
	         // Additional processing for existing customers
	    	    postCreateRetailCustomerActions(retailCustomerCreate, accNumberRes,customerIDRes);
	        } else {
	            // accNumberRes is not an integer with 14 digits
	            System.out.println("Invalid "+accNumberRes+": Does not have 14 digits");
	        }
	    } catch (NumberFormatException e) {
	        // accNumberRes is not a valid integer
	        System.out.println("Invalid accNumberRes: Not an integer");
	    }
	    
	    
	}

	private void handleNewCustomer(RetailCustomerCreate retailCustomerCreate, HashMap<String, String> map) throws Exception {
	    SOAPMessage soapResponse = CreateRetailCustomer.createRetailCustomerSOAPRequest(
	            retailCustomerCreate, retailCustomerCreateEndpoint, soaUsername, soaPassword, soaSystemCode);

	    if (soapResponse == null) {
	        handleFailure(map, "Null response");
	    } else {
	        handleSoapResponse(soapResponse, map, retailCustomerCreate);
	    }
	}

	private void postCreateRetailCustomerActions(RetailCustomerCreate retailCustomerCreate, String accNumberRes,String customerIDRes) throws Exception {
	    // Send SMS after successful customer creation
	    SendSMSRequest sendSMSData = createSendSMSRequest(
	            retailCustomerCreate.getPersonalPartyBasicDetails().getPhone(),
	            "Your Account Number " + accNumberRes + " had been created");

//	    postSendSMSReq(sendSMSData);
	    
	    String fullNames = retailCustomerCreate.getPersonalPartyBasicDetails().getFullName();
	    
	    String productType  = retailCustomerCreate.getPersonalPartyBasicDetails().getProduct();	
	    String schemeCode  = retailCustomerCreate.getPersonalPartyBasicDetails().getSchemeCode();
	    String uppercaseFullNames = fullNames.toUpperCase();
	    
	    
	    String salutation = retailCustomerCreate.getPersonalPartyBasicDetails().getSalutation();
	    String gender =  retailCustomerCreate.getPersonalPartyBasicDetails().getGender();
	    if (gender.equals("M")) {
	    	gender = "Male";	
	    }else if(gender.equals("F")) {
	    	gender = "Female";
	    }else {
	    	gender = "UNKNOWN";
	    }
	    String fname = retailCustomerCreate.getPersonalPartyBasicDetails().getFirstName();
	    String mname = retailCustomerCreate.getPersonalPartyBasicDetails().getMiddleName();
	    String lname = retailCustomerCreate.getPersonalPartyBasicDetails().getLastName();
	    String dob = retailCustomerCreate.getPersonalPartyBasicDetails().getDob();
	    String idNumber = retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber();
	    String kraPinNo = retailCustomerCreate.getPersonalPartyBasicDetails().getKraPin();
	    String sourceOfFunds = retailCustomerCreate.getPersonalPartyBasicDetails().getSourceOfFunds();
	    String phoneNo = retailCustomerCreate.getPersonalPartyBasicDetails().getPhone();
	    String emailAdd = retailCustomerCreate.getPersonalPartyBasicDetails().getEmail();
	    String cityBirth = retailCustomerCreate.getPersonalPartyBasicDetails().getCityTown();
	    String physicalAdd = retailCustomerCreate.getPersonalPartyBasicDetails().getStreetRoadName();

	    String occupation = retailCustomerCreate.getPersonalPartyBasicDetails().getOccupationStatus();
	    
	    LocalDate currentDate = LocalDate.now();
        String currentDateString = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyy"));
        
        Year currentYear = Year.now();
        int year = currentYear.getValue();

	    
        String emailMessage = "<!DOCTYPE html><html><head> <style> body { font-family: Arial, sans-serif; margin: 0; padding: 0; } img { max-width: 100%; height: auto; } .email-content { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; } </style></head><body> <div class='email-content'> <img src='"+coopLogoheader+"' alt='Coop Logo'/> <p style='font-family:sans-serif; font-size: 10pt;'>Dear "+fullNames+",</p> <p style='font-family:sans-serif; font-size: 10pt;'>We thank you for choosing Cooperative Bank as your preferred bank. We are pleased to advise that your account is now open with the following details:</p> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Account Details </td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'> Account Name: <b>"+uppercaseFullNames+"</b></td> </tr> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'> Account Number: <b>"+accNumberRes+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'> Account Type: <b>COOPERATIVE BANK "+schemeCode+" - "+productType+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'> Account Currency: <b>KENYAN SHILLINGS</b></td> </tr> </tbody> </table> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Applicant Details </td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Salutation: <b>"+salutation+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'> Gender: <b> "+gender+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>DOB: <b> "+dob+" </b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>First Name: <b>"+fname+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Middle Name: <b> "+mname+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Last Name: <b> "+lname+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Id/PassPort No: <b>"+idNumber+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>KRA Pin: <b>"+kraPinNo+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Physical/Current Residence: <b>"+physicalAdd+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Occupation: <b>"+occupation+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Source of Funds: <b>"+sourceOfFunds+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>City of birth: <b>"+cityBirth+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Mobile number: <b> "+phoneNo+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Email Address: <b> "+emailAdd+"</b></td> </tr> </tbody> </table> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Customer(s) Declaration </td> </tr> </tbody> </table> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>TO THE CO-OPERATIVE BANK OF KENYA LIMITED, I/We agree that this account shall be operated solely at the discretion of the Bank and agree to hereby indemnify the Bank at my/our cost against any loss or claims arising out of the account being closed by the Bank without notice due to unsatisfactory performance.</div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>I/We having read and understood the General Terms and Conditions and Data Privacy Statement available at the Branches or on the Bank's website <strong> www.coopbank.co.ke</strong> or such other websites as the Bank may designate as its social website from time to time hereby <strong>authorize </strong> the Bank to use My/Our contact details to send information about products and services including but not limited to offers and promotions which may be of interest to me/us. The Bank may do this by phone, post, email, text or through other digital media. I/We confirm having understood that my/our personal information provided in this application form shall be processed in accordance with the provisions of the Data Protection Act, 2019, and where applicable the General Data Protection Regulation (EU) 2016/679 or all other applicable laws as may be amended from time to time </div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 4pt;'>On this day: "+currentDateString+"</div> <hr></hr> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 8pt;text-align: center;'>Cooperative Bank of Kenya is regulated by the Central Bank of Kenya</div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;text-align: center;'>Copyright © "+year+" Cooperative Bank of Kenya. All Rights Reserved.</div><div style='height: 8px;background-color: #00513b;border-bottom: 4px solid  #78af19;margin-top: 12px;'></div></div></body></html>";	                        
        
        
        String mandateHtml = "<!DOCTYPE html><html><head><style>body {font-family: Arial, sans-serif;margin: 0;padding: 0;}img {max-width: 100%;height: auto;}.email-content {max-width: 100%;margin: 0px;padding: 0px;border: 1px solid #ccc;border-radius: 0px;}</style></head><body><div class='email-content'> <table width='100%'> <tbody style='border-bottom: 0px solid #78af19;'> <tr style='border-bottom: 0px solid #78af19;'> <td style='background-color: #00513b; font-family:sans-serif; font-size: 7.9pt;' width='45.5%'></td> <td style='background-color: #eaf3f0;' width='54.5%'> <table> <tboby> <tr> <td style='font-size: 7.9pt; color: #545453; width:100%;' align='right'>Form No: A1 (a)</td> </tr> <tr> <td style='font-size: 18pt; width:100%; color:#00513b; font-family:sans-serif; font-size: 16pt;'><b>Personal/Joint Customer <br/>Engagement Form</b></td> </tr> </tboby> </table> </td> </tr> </tbody> </table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Account Details </td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'>Account Name: <b>"+uppercaseFullNames+"</b></td></tr><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'>Account Number: <b>"+accNumberRes+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Account Type: <b>COOPERATIVE BANK "+schemeCode+" - "+productType+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Account Currency: <b>KENYAN SHILLINGS</b></td></tr></tbody></table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Applicant Details </td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Salutation: <b>"+salutation+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Gender: <b> "+gender+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>DOB:<b> "+dob+" </b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>FirstName: <b>"+fname+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>MiddleName: <b> "+mname+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>LastName: <b> "+lname+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Id/PassPort No: <b>"+idNumber+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>KRAPin: <b>"+kraPinNo+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Physical/Current Residence: <b>"+physicalAdd+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Occupation: <b>"+occupation+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Sourceof Funds: <b>"+sourceOfFunds+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Cityof birth: <b>"+cityBirth+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Mobilenumber: <b> "+phoneNo+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>EmailAddress: <b> "+emailAdd+"</b></td></tr></tbody></table> <table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Signature and Photo </td></tr></tbody></table> <table style='height: 8px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%; height:30px;'><img align='right' src='https://www.pngmart.com/files/10/Female-User-Account-Transparent-PNG.png'/></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'><img align='right' src='https://www.pngmart.com/files/10/Female-User-Account-Transparent-PNG.png'/></td></tr></tbody></table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Customer(s) Declaration </td></tr></tbody></table> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>TO THE CO-OPERATIVE BANK OF KENYA LIMITED,I/We agree that this account shall be operated solely at the discretion of the Bank and agree to herebyindemnify the Bank at my/our cost against any loss or claims arising out of the account being closed by theBank without notice due to unsatisfactory performance.</div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>I/We having read and understood theGeneral Terms and Conditions and Data Privacy Statement available at the Branches or on the Bank's website<strong> www.coopbank.co.ke</strong> or such other websites as the Bank may designate as its social websitefrom time to time hereby <strong>authorize </strong> the Bank to use My/Our contact details to sendinformation about products and services including but not limited to offers and promotions which may be ofinterest to me/us. The Bank may do this by phone, post, email, text or through other digital media. I/Weconfirm having understood that my/our personal information provided in this application form shall beprocessed in accordance with the provisions of the Data Protection Act, 2019, and where applicable theGeneral Data Protection Regulation (EU) 2016/679 or all other applicable laws as may be amended from time totime </div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 4pt;'>On this day: "+currentDateString+"</div><hr/><div style='font-family:sans-serif; font-size: 8pt;padding-top: 8pt;text-align: center;'>Cooperative Bank ofKenya is regulated by the Central Bank of Kenya</div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;text-align: center;'>Copyright © "+year+"Cooperative Bank of Kenya. All Rights Reserved.</div><div style='height: 8px;background-color: #00513b;border-bottom: 4px solid #78af19;margin-top: 12px;'></div></div></body></html>";        
        
        
        String base64Pdf = PdfConverterService.generatePdfBase64(mandateHtml);
		log.info("---------------------------------------------------------------base64Pdf:---------------------------------------------------------------");
        log.info(base64Pdf);
		log.info("---------------------------------------------------------------base64Pdf:---------------------------------------------------------------");

        
        String emailSubject ="Welcome to Cooperative Bank of Kenya";
    	
    	SendEmailRequest SendEmailData = createSendEmailRequest(retailCustomerCreate.getPersonalPartyBasicDetails().getEmail(), emailSubject, emailMessage);
    	postSendEmailReq(SendEmailData);
    	
    	String signingDetailsPhoto = retailCustomerCreate.getPersonalPartyBasicDetails().getVerifiedPhoto();

	    SigningDetailsRequest signingDetailsRequestData = createSigningDetailsRequest(
	            accNumberRes,
	            uppercaseFullNames,
	            signingDetailsPhoto+"=",
	            signingDetailsPhoto+"=",
	            "SOLE SIGNATORY/" + uppercaseFullNames +
	                    "/" + retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber());

	    getSigningDetailsData(signingDetailsRequestData);
	    
	    CreateDocumentRequest createDocumentRequestData = createCreateDocumentRequest(
	    		retailCustomerCreate.getPersonalPartyBasicDetails().getFullName(),
	    		retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber(),
	    		accNumberRes,
	    		customerIDRes,
	    		base64Pdf);
	    getCreateDocumentData(createDocumentRequestData);

	}

	

	            

	 
	private void handleSuccess(HashMap<String, String> map, String customerID, String accNumber,String status,String statusDescription, String json) {
	    map.put("Status", status);
	    map.put("StatusDescription", statusDescription);
	    map.put("customerID", customerID);
	    map.put("AccountNumber", accNumber);
	    if (json != null) {
	        map.put("Response", json);
	    }
	    
	}

	private void handleFailure(HashMap<String, String> map, String description) {
	    map.put("Status", "false");
	    map.put("StatusDescription", "Failed");
	    map.put("Response", description);
	}


	private void handleSoapResponse(SOAPMessage soapResponse, HashMap<String, String> map,RetailCustomerCreate retailCustomerCreate) throws Exception {
	    SOAPHeader header = soapResponse.getSOAPHeader();
	    String accNumberRes = null;
	    String xmlStringHeader = CommonMethods.convertHeaderString(header);
	    xmlStringHeader = xmlStringHeader.replace("head:", "");

	    String statusDescriptionTag = "StatusDescription";
	    String statusDescriptionRes = xmlStringHeader.split("<" + statusDescriptionTag + ">")[1].split("</" + statusDescriptionTag + ">")[0];

	    if (statusDescriptionRes.equals("Failed")) {
	        String messageDescriptionTag = "MessageDescription";
	        String messageDescriptionRes = xmlStringHeader.split("<" + messageDescriptionTag + ">")[1].split("</" + messageDescriptionTag + ">")[0];
	        handleFailure(map, messageDescriptionRes);
	    } else {
	        NodeList returnList = header.getElementsByTagName("head:ResponseHeader");
	        for (int k = 0; k < returnList.getLength(); k++) {
	            NodeList innerResultList = returnList.item(k).getChildNodes();
	            if (innerResultList.item(3).getNodeName().equalsIgnoreCase("head:StatusDescription")) {
	                String statusDesc = String.valueOf(innerResultList.item(3).getTextContent().trim());
	                if (statusDesc.equals("Success")) {
	                    SOAPBody sb = soapResponse.getSOAPBody();
	                    String xmlString = CommonMethods.convertToString(sb);
	                    xmlString = xmlString.replace("xmlns:tns30=\"urn://co-opbank.co.ke/BS/Customer/RetailCustomerCreate/Post.1.0\"", "");
	                    xmlString = xmlString.replace("tns30:", "");
	                    xmlString = xmlString.replace(" <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
//	                    log.info(xmlString);

	                    try {
	                        String customerIDTag = "CustomerID";
	                        String customerIDRes = xmlString.split("<" + customerIDTag + ">")[1].split("</" + customerIDTag + ">")[0];
	                        
	                        Map<String, String> accountCreateResult = getAccountCreateData(
	                        		retailCustomerCreate.getPersonalPartyBasicDetails().getSchemeCode(), 
	                        		retailCustomerCreate.getPersonalPartyBasicDetails().getProduct(), 
	                        		customerIDRes, 
	                        		retailCustomerCreate.getPersonalPartyBasicDetails().getSourceOfFunds());
	                        
	            	        String status = accountCreateResult.get("Status");
	            	        String statusDescription = accountCreateResult.get("StatusDescription");
	            	        String response = accountCreateResult.get("Response");
	            	        accNumberRes = accountCreateResult.get("Description");
	            	        
	                        JSONObject jsonObj = XML.toJSONObject(xmlString);
	                        String json = jsonObj.toString(INDENTATION);
	                        
	                     // Send SMS after successful customer creation
	                        SendSMSRequest sendSMSData = createSendSMSRequest(retailCustomerCreate.getPersonalPartyBasicDetails().getPhone(), 
	                        		"Your Account Number "+accNumberRes+" had been created");
	                        postSendSMSReq(sendSMSData);	
	                        
	                	    String fullNames = retailCustomerCreate.getPersonalPartyBasicDetails().getFullName();
	                	    
	                	    String productType  = retailCustomerCreate.getPersonalPartyBasicDetails().getProduct();	  
	                	    String schemeCode  = retailCustomerCreate.getPersonalPartyBasicDetails().getSchemeCode();
	                	    String uppercaseFullNames = fullNames.toUpperCase();
	                	    String salutation = retailCustomerCreate.getPersonalPartyBasicDetails().getSalutation();
	                	    String gender =  retailCustomerCreate.getPersonalPartyBasicDetails().getGender();
	                	    if (gender.equals("M")) {
	                	    	gender = "Male";	
	                	    }else if(gender.equals("F")) {
	                	    	gender = "Female";
	                	    }else {
	                	    	gender = "UNKNOWN";
	                	    }
	                	    String fname = retailCustomerCreate.getPersonalPartyBasicDetails().getFirstName();
	                	    String mname = retailCustomerCreate.getPersonalPartyBasicDetails().getMiddleName();
	                	    String lname = retailCustomerCreate.getPersonalPartyBasicDetails().getLastName();
	                	    String dob = retailCustomerCreate.getPersonalPartyBasicDetails().getDob();
	                	    String idNumber = retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber();
	                	    String kraPinNo = retailCustomerCreate.getPersonalPartyBasicDetails().getKraPin();
	                	    String sourceOfFunds = retailCustomerCreate.getPersonalPartyBasicDetails().getSourceOfFunds();
	                	    String phoneNo = retailCustomerCreate.getPersonalPartyBasicDetails().getPhone();
	                	    String emailAdd = retailCustomerCreate.getPersonalPartyBasicDetails().getEmail();
	                	    String cityBirth = retailCustomerCreate.getPersonalPartyBasicDetails().getCityTown();
	                	    String physicalAdd = retailCustomerCreate.getPersonalPartyBasicDetails().getStreetRoadName();
	                	   
	                	    String occupation = retailCustomerCreate.getPersonalPartyBasicDetails().getOccupationStatus();
	                	    
	                	    LocalDate currentDate = LocalDate.now();
	                        String currentDateString = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyy"));
	                        
	                        Year currentYear = Year.now();
	                        int year = currentYear.getValue();
	                	 
	                	    
	                        String emailMessage = "<!DOCTYPE html><html><head> <style> body { font-family: Arial, sans-serif; margin: 0; padding: 0; } img { max-width: 100%; height: auto; } .email-content { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; } </style></head><body> <div class='email-content'> <img src='"+coopLogoheader+"' alt='Coop Logo'/> <p style='font-family:sans-serif; font-size: 10pt;'>Dear "+fullNames+",</p> <p style='font-family:sans-serif; font-size: 10pt;'>We thank you for choosing Cooperative Bank as your preferred bank. We are pleased to advise that your account is now open with the following details:</p> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Account Details </td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'> Account Name: <b>"+uppercaseFullNames+"</b></td> </tr> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'> Account Number: <b>"+accNumberRes+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'> Account Type: <b>COOPERATIVE BANK "+schemeCode+" - "+productType+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'> Account Currency: <b>KENYAN SHILLINGS</b></td> </tr> </tbody> </table> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Applicant Details </td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Salutation: <b>"+salutation+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'> Gender: <b> "+gender+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>DOB: <b> "+dob+" </b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>First Name: <b>"+fname+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Middle Name: <b> "+mname+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Last Name: <b> "+lname+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Id/PassPort No: <b>"+idNumber+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>KRA Pin: <b>"+kraPinNo+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Physical/Current Residence: <b>"+physicalAdd+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'> Occupation: <b>"+occupation+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Source of Funds: <b>"+sourceOfFunds+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>City of birth: <b>"+cityBirth+"</b></td> </tr> </tbody> </table> <table style='height: 28px;' width='100%'> <tbody> <tr> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Mobile number: <b> "+phoneNo+"</b></td> <td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Email Address: <b> "+emailAdd+"</b></td> </tr> </tbody> </table> <table style='height: 28px; width: 100%; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'> <tbody> <tr> <td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'> Customer(s) Declaration </td> </tr> </tbody> </table> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>TO THE CO-OPERATIVE BANK OF KENYA LIMITED, I/We agree that this account shall be operated solely at the discretion of the Bank and agree to hereby indemnify the Bank at my/our cost against any loss or claims arising out of the account being closed by the Bank without notice due to unsatisfactory performance.</div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>I/We having read and understood the General Terms and Conditions and Data Privacy Statement available at the Branches or on the Bank's website <strong> www.coopbank.co.ke</strong> or such other websites as the Bank may designate as its social website from time to time hereby <strong>authorize </strong> the Bank to use My/Our contact details to send information about products and services including but not limited to offers and promotions which may be of interest to me/us. The Bank may do this by phone, post, email, text or through other digital media. I/We confirm having understood that my/our personal information provided in this application form shall be processed in accordance with the provisions of the Data Protection Act, 2019, and where applicable the General Data Protection Regulation (EU) 2016/679 or all other applicable laws as may be amended from time to time </div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 4pt;'>On this day: "+currentDateString+"</div> <hr></hr> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 8pt;text-align: center;'>Cooperative Bank of Kenya is regulated by the Central Bank of Kenya</div> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;text-align: center;'>Copyright © "+year+" Cooperative Bank of Kenya. All Rights Reserved.</div><div style='height: 8px;background-color: #00513b;border-bottom: 4px solid  #78af19;margin-top: 12px;'></div></div></body></html>";	                        
	                        
	                        
	                        String mandateHtml = "<!DOCTYPE html><html><head><style>body {font-family: Arial, sans-serif;margin: 0;padding: 0;}img {max-width: 100%;height: auto;}.email-content {max-width: 100%;margin: 0px;padding: 0px;border: 1px solid #ccc;border-radius: 0px;}</style></head><body><div class='email-content'> <table width='100%'> <tbody style='border-bottom: 0px solid #78af19;'> <tr style='border-bottom: 0px solid #78af19;'> <td style='background-color: #00513b; font-family:sans-serif; font-size: 7.9pt;' width='45.5%'></td> <td style='background-color: #eaf3f0;' width='54.5%'> <table> <tboby> <tr> <td style='font-size: 7.9pt; color: #545453; width:100%;' align='right'>Form No: A1 (a)</td> </tr> <tr> <td style='font-size: 18pt; width:100%; color:#00513b; font-family:sans-serif; font-size: 16pt;'><b>Personal/Joint Customer <br/>Engagement Form</b></td> </tr> </tboby> </table> </td> </tr> </tbody> </table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Account Details </td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'>Account Name: <b>"+uppercaseFullNames+"</b></td></tr><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 100%;'>Account Number: <b>"+accNumberRes+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Account Type: <b>COOPERATIVE BANK "+schemeCode+" - "+productType+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Account Currency: <b>KENYAN SHILLINGS</b></td></tr></tbody></table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Applicant Details </td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Salutation: <b>"+salutation+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Gender: <b> "+gender+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>DOB:<b> "+dob+" </b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>FirstName: <b>"+fname+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>MiddleName: <b> "+mname+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>LastName: <b> "+lname+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Id/PassPort No: <b>"+idNumber+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>KRAPin: <b>"+kraPinNo+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Physical/Current Residence: <b>"+physicalAdd+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Occupation: <b>"+occupation+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 34%;'>Sourceof Funds: <b>"+sourceOfFunds+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 33%;'>Cityof birth: <b>"+cityBirth+"</b></td></tr></tbody></table><table style='height: 28px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>Mobilenumber: <b> "+phoneNo+"</b></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'>EmailAddress: <b> "+emailAdd+"</b></td></tr></tbody></table> <table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Signature and Photo </td></tr></tbody></table> <table style='height: 8px;' width='100%'><tbody><tr><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%; height:30px;'><img align='right' src='https://www.pngmart.com/files/10/Female-User-Account-Transparent-PNG.png'/></td><td style='border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; width: 50%;'><img align='right' src='https://www.pngmart.com/files/10/Female-User-Account-Transparent-PNG.png'/></td></tr></tbody></table><table style='height: 28px; width: 100%; border: 0px solid #00513b; font-family:sans-serif; font-size: 7.9pt;'><tbody><tr><td style='background-color: #78af19; border: .5px solid #00513b; font-family:sans-serif; font-size: 7.9pt; color: #000000; font-weight: bold;'>Customer(s) Declaration </td></tr></tbody></table> <div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>TO THE CO-OPERATIVE BANK OF KENYA LIMITED,I/We agree that this account shall be operated solely at the discretion of the Bank and agree to herebyindemnify the Bank at my/our cost against any loss or claims arising out of the account being closed by theBank without notice due to unsatisfactory performance.</div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;'>I/We having read and understood theGeneral Terms and Conditions and Data Privacy Statement available at the Branches or on the Bank's website<strong> www.coopbank.co.ke</strong> or such other websites as the Bank may designate as its social websitefrom time to time hereby <strong>authorize </strong> the Bank to use My/Our contact details to sendinformation about products and services including but not limited to offers and promotions which may be ofinterest to me/us. The Bank may do this by phone, post, email, text or through other digital media. I/Weconfirm having understood that my/our personal information provided in this application form shall beprocessed in accordance with the provisions of the Data Protection Act, 2019, and where applicable theGeneral Data Protection Regulation (EU) 2016/679 or all other applicable laws as may be amended from time totime </div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 4pt;'>On this day: "+currentDateString+"</div><hr/><div style='font-family:sans-serif; font-size: 8pt;padding-top: 8pt;text-align: center;'>Cooperative Bank ofKenya is regulated by the Central Bank of Kenya</div><div style='font-family:sans-serif; font-size: 8pt;padding-top: 2pt;text-align: center;'>Copyright © "+year+"Cooperative Bank of Kenya. All Rights Reserved.</div><div style='height: 8px;background-color: #00513b;border-bottom: 4px solid #78af19;margin-top: 12px;'></div></div></body></html>";        

	                        
	                        String base64Pdf = PdfConverterService.generatePdfBase64(mandateHtml);
	                        String emailSubject ="Welcome to Cooperative Bank of Kenya";
	                        
	                        SendEmailRequest SendEmailData = createSendEmailRequest(retailCustomerCreate.getPersonalPartyBasicDetails().getEmail(), emailSubject, emailMessage);
                        	postSendEmailReq(SendEmailData);
                        	
                        	String signingDetailsPhoto = retailCustomerCreate.getPersonalPartyBasicDetails().getVerifiedPhoto();
                        	
	                        SigningDetailsRequest SigningDetailsRequestData = createSigningDetailsRequest( 
	                        		accNumberRes,
	                        		uppercaseFullNames,  
	                        		signingDetailsPhoto+"=",  
	                        		signingDetailsPhoto+"=",  
	                        		"SOLE SIGNATORY/"+uppercaseFullNames+"/"+retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber());
	                        
	                        getSigningDetailsData(SigningDetailsRequestData);
	                        
	                        CreateDocumentRequest createDocumentRequestData =  createCreateDocumentRequest(
	                        		retailCustomerCreate.getPersonalPartyBasicDetails().getFullName(),
	                	    		retailCustomerCreate.getPersonalPartyBasicDetails().getIdNumber(),
	                        		accNumberRes,
	                	    		customerIDRes,
	                	    		base64Pdf);
	                        getCreateDocumentData(createDocumentRequestData);
	                        
	                        
	                        
	                        
	                        handleSuccess(map, customerIDRes, accNumberRes, status,statusDescription,json);
	                    } catch (JSONException ex) {
	                        ex.printStackTrace();
	                    }
	                } else {
	                    String messageDescriptionTag = "MessageDescription";
	                    String messageDescriptionRes = xmlStringHeader.split("<" + messageDescriptionTag + ">")[1].split("</" + messageDescriptionTag + ">")[0];
	                    handleFailure(map, messageDescriptionRes);
	                }
	            } else {
	                handleFailure(map, "Failed");
	            }
	        }
	    }
	}

	//	----------------------- End Create Retail Customer ----------------------- //
	
	
}


