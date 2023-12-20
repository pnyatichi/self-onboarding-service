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

import com.coopbank.selfonboarding.request.AccountCreateRequest;
import com.coopbank.selfonboarding.request.CustomerAccountDetailsInquiryRequest;
import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AccountCreate {

	
    public  static SOAPMessage getAccountCreate(SOAPMessage soapRequest,String customerAccDetailsInquiryEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  customerAccDetailsInquiryEndpoint;
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
    
    public static SOAPMessage postAccountCreateReq(String schemeCode,String product,String customerCode,String sourceOfFunds,String accountCreateEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
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
            String nsURI = "urn://co-opbank.co.ke/DataModel/Acccount/AccountCreate/Post/2.0";

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
            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", mes);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", mes);
            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", mes);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", mes);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", mes);
            SOAPElement userName = credentialss.addChildElement("Username", mes);
            SOAPElement password = credentialss.addChildElement("Password", mes);
            SOAPElement realm = credentialss.addChildElement("Realm", mes);
            SOAPElement bankID = credentialss.addChildElement("BankID", mes);

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            faultTO.addTextNode("http");
            replyTO.addTextNode("http");
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
            userName.addTextNode(soaUsername);
            password.addTextNode(soaPassword);
            realm.addTextNode("cc");
            bankID.addTextNode("01");

            SOAPBody soapBody = envelope.getBody();

            SOAPElement AccountCreateRq = soapBody.addChildElement("AccountCreateRequest", ns);
            
            AccountCreateRq.addChildElement("SchemeCode", ns).addTextNode(schemeCode);
            AccountCreateRq.addChildElement("Product", ns).addTextNode(product);
            AccountCreateRq.addChildElement("BranchSortCode", ns).addTextNode("1002");
            AccountCreateRq.addChildElement("Currency", ns).addTextNode("KES");
            AccountCreateRq.addChildElement("CustomerCode", ns).addTextNode(customerCode);
            AccountCreateRq.addChildElement("SectorCode", ns).addTextNode("01");
            AccountCreateRq.addChildElement("SubsectorCode", ns).addTextNode("0101");
            AccountCreateRq.addChildElement("PurposeOfAccount", ns).addTextNode("PERSONAL SAVING");
            AccountCreateRq.addChildElement("WHTTaxIndicator", ns).addTextNode("P");
            AccountCreateRq.addChildElement("AROCode", ns).addTextNode("063BB05");
            AccountCreateRq.addChildElement("DSOCode", ns).addTextNode("8230");
            AccountCreateRq.addChildElement("StatementMode", ns).addTextNode("S");
            AccountCreateRq.addChildElement("StatementFrequency", ns).addTextNode("M");
            AccountCreateRq.addChildElement("StatementMedium", ns).addTextNode("");
            AccountCreateRq.addChildElement("StartDate", ns).addTextNode("1");
            AccountCreateRq.addChildElement("WeekDay", ns).addTextNode("0");
            AccountCreateRq.addChildElement("HolidayStatus", ns).addTextNode("N");
            AccountCreateRq.addChildElement("BusinessEconomicCode", ns).addTextNode("A0111");
            AccountCreateRq.addChildElement("SourceOfFunds", ns).addTextNode(sourceOfFunds);
            AccountCreateRq.addChildElement("ProductSegment", ns).addTextNode("");
            
            AccountCreateRq.addChildElement("AccountName", ns).addTextNode("");
            AccountCreateRq.addChildElement("OperationMode", ns).addTextNode("");
            AccountCreateRq.addChildElement("GenLedgerSubHeadCode", ns).addTextNode("");
            
            SOAPElement RelatedPartyRecReq = AccountCreateRq.addChildElement("RelatedPartyRec", ns);
            RelatedPartyRecReq.addChildElement("RelationshipType", ns).addTextNode("");
            RelatedPartyRecReq.addChildElement("CustomerId", ns).addTextNode("");


            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "AccountCreate" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(AccountCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String soapEnv = new String(out.toByteArray());
            System.out.println("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = getAccountCreate((SOAPMessage),accountCreateEndpoint,soaPassword);

//            log.info("\n Response  is  for ID  " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(AccountCreate.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}

