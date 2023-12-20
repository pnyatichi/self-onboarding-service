package com.coopbank.selfonboarding.soa.services.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
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

import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SanctionDetails {

	
    public  static SOAPMessage getSanctionDetails(SOAPMessage soapRequest,String sanctionDetailsEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  sanctionDetailsEndpoint;
//    log.info("\n--------------------------------- SOAP Response & URL ---------------------------------");
//    log.info("request is == " + soapRequest +" url == "+url);
//    log.info("\n--------------------------------- SOAP Response & URL ---------------------------------");

//    log.info("\n--------------------------------- SOAP Response ---------------------------------");
    soapResponse = soapConnection.call(soapRequest, url);
    CommonMethods.createSoapResponse(soapResponse);
//    System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
    soapConnection.close();
} catch (Exception e) {
    e.printStackTrace();
}
return soapResponse;

}
    public static SOAPMessage getCustomerSanctionDetails(String minMatchScore, String customerType, String fullName, String firstName,
			String middleName, String lastName, String identificationDocType, String identificationDocNo,
			String nationality, String dateOfBirth, String countryOfBirth,String sanctionDetailsEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;
//        SOAPMessage response = null;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();
            String myNamespace = "soapenv";
            
            String mes = "mes";
            String mesURI = "urn://co-opbank.co.ke/CommonServices/Data/Message/MessageHeader";
            String com = "com";
            String comURI = "urn://co-opbank.co.ke/CommonServices/Data/Common";
            String ns = "ns";
            String nsURI = "urn://co-opbank.co.ke/DataModel/Customer/SanctionDetails/Get/1.0";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, mesURI);
            envelope.addNamespaceDeclaration(com, comURI);
            envelope.addNamespaceDeclaration(ns, nsURI);
            
            SOAPHeader header = envelope.getHeader();
            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", mes);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", com);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", com);
//            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", mes);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", mes);
//            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", mes);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", mes);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", mes);
//            SOAPElement userName = credentialss.addChildElement("Username", mes);
//            SOAPElement password = credentialss.addChildElement("Password", mes);
//            SOAPElement realm = credentialss.addChildElement("Realm", mes);
//            SOAPElement bankId = credentialss.addChildElement("BankID", mes);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
//            faultTO.addTextNode("http");
//            replyTO.addTextNode("http");
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
//            userName.addTextNode(soaUsername);
//            password.addTextNode(soaPassword);
//            realm.addTextNode("cc");
//            bankId.addTextNode("01");

            SOAPBody soapBody = envelope.getBody();

            SOAPElement sanctionDetailsInputTag = soapBody.addChildElement("sanctionDetailsInput", ns);
            SOAPElement operationParametersTag = sanctionDetailsInputTag.addChildElement("OperationParameters", ns);
            
            SOAPElement minMatchScoreTag = operationParametersTag.addChildElement("MinMatchScore", ns);
            SOAPElement customerTypeTag = operationParametersTag.addChildElement("CustomerType", ns);
            SOAPElement fullNameTag = operationParametersTag.addChildElement("FullName", ns);
            SOAPElement firstNameTag = operationParametersTag.addChildElement("FirstName", ns);
            SOAPElement middleNameTag = operationParametersTag.addChildElement("MiddleName", ns);
            SOAPElement lastNameTag = operationParametersTag.addChildElement("LastName", ns);
            SOAPElement identificationDocTypeTag = operationParametersTag.addChildElement("IdentificationDocType", ns);
            SOAPElement identificationDocNoTag = operationParametersTag.addChildElement("IdentificationDocNo", ns);
            SOAPElement nationalityTag = operationParametersTag.addChildElement("Nationality", ns);
            SOAPElement dateOfBirthTag = operationParametersTag.addChildElement("DateOfBirth", ns);
            SOAPElement countryOfBirthTag = operationParametersTag.addChildElement("CountryOfBirth", ns);
            SOAPElement addressDetailTag = operationParametersTag.addChildElement("AddressDetail", ns);
            
            SOAPElement cityTag = addressDetailTag.addChildElement("City", ns);
            SOAPElement stateOrProvinceTag = addressDetailTag.addChildElement("StateOrProvince", ns);
            SOAPElement postalAddressTag = addressDetailTag.addChildElement("PostalAddress", ns);
            SOAPElement zipCodeTag = addressDetailTag.addChildElement("ZipCode", ns);
            SOAPElement postalCodeTag = addressDetailTag.addChildElement("PostalCode", ns);
            SOAPElement countryTag = addressDetailTag.addChildElement("Country", ns);
       

            minMatchScoreTag.addTextNode(minMatchScore);
            customerTypeTag.addTextNode(customerType);
            fullNameTag.addTextNode(fullName);
            firstNameTag.addTextNode(firstName);
            middleNameTag.addTextNode(middleName);
            lastNameTag.addTextNode(lastName);
            identificationDocTypeTag.addTextNode(identificationDocType);
            identificationDocNoTag.addTextNode(identificationDocNo);
            nationalityTag.addTextNode(nationality);
            dateOfBirthTag.addTextNode(dateOfBirth);
            countryOfBirthTag.addTextNode(countryOfBirth);

            
            cityTag.addTextNode("");
            stateOrProvinceTag.addTextNode("");
            postalAddressTag.addTextNode("");
            zipCodeTag.addTextNode("");
            postalCodeTag.addTextNode("");
            countryTag.addTextNode("");

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "SanctionDetails" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(AccountDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String soapEnv = new String(out.toByteArray());
            log.info("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = getSanctionDetails((SOAPMessage),sanctionDetailsEndpoint,soaPassword);

//            log.info(" Response  is  for ID "/*+idNumber*/ + " is " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(AccountDetails.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}

