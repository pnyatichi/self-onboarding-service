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
public class AccountDetails {

	
    public  static SOAPMessage getAccountDetails(SOAPMessage soapRequest,String accDetailsEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  accDetailsEndpoint;
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
    
    public static SOAPMessage getAccountDetailsDetails(String accountNumber,String accDetailsEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
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
            String bsac = "bsac";
            String bsacURI = "urn://co-opbank.co.ke/BS/Account/BSAccountDetails.3.0";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, mesURI);
            envelope.addNamespaceDeclaration(com, comURI);
            envelope.addNamespaceDeclaration(bsac, bsacURI);
            SOAPHeader header = envelope.getHeader();
            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", mes);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", com);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", com);
            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", mes);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", mes);
            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", mes);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", mes);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", mes);
            SOAPElement userName = credentialss.addChildElement("Username", mes);
            SOAPElement password = credentialss.addChildElement("Password", mes);
            SOAPElement realm = credentialss.addChildElement("Realm", mes);
            SOAPElement bankId = credentialss.addChildElement("BankID", mes);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            faultTO.addTextNode("http");
            replyTO.addTextNode("http");
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
            userName.addTextNode(soaUsername);
            password.addTextNode(soaPassword);
            realm.addTextNode("cc");
            bankId.addTextNode("01");

            SOAPBody soapBody = envelope.getBody();

            SOAPElement accountDetailsRequestTag = soapBody.addChildElement("AccountDetailsRequest", bsac);
            SOAPElement accountNumberTag = accountDetailsRequestTag.addChildElement("AccountNumber", bsac);
       

            accountNumberTag.addTextNode(accountNumber);

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "GetAccountDetails" + "\"");

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

            SOAPMessageResponse = getAccountDetails((SOAPMessage),accDetailsEndpoint,soaPassword);

//            log.info(" Response  is  for ID "/*+idNumber*/ + " is " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(AccountDetails.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}

