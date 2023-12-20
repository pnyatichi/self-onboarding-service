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
public class IprsApis {

	  

	    public  static SOAPMessage postIprsRequest(SOAPMessage soapRequest, String iprsEndpoint,String soaPassword) throws MalformedURLException,
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
			    String url =  iprsEndpoint;			   
//			    System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
			    soapResponse = soapConnection.call(soapRequest, url);
			   
			    CommonMethods.createSoapResponse(soapResponse);
//			    System.out.println("\n--------------------------------- SOAP Response ---------------------------------");
		
			    soapConnection.close();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			return soapResponse;
	
	}
    

    

    

    
    public static SOAPMessage getKslIprsDetails(String iprsUserName,String iprsPassword,String doc_number,String doc_type,String serial_number, String iprsEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
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
            String nsURI = "urn://co-opbank.co.ke/Banking/External/IPRS/1.0";
            String com1 = "com1";
            String com1URI = "http://schemas.datacontract.org/2004/07/IPRSManager/CommonData";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(mes, mesURI);
            envelope.addNamespaceDeclaration(com, comURI);
			envelope.addNamespaceDeclaration(com1, com1URI);
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

            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            faultTO.addTextNode("http");
            replyTO.addTextNode("http");
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);
            userName.addTextNode(soaUsername);
            password.addTextNode(soaPassword);
            realm.addTextNode("cc");

            SOAPBody soapBody = envelope.getBody();

            SOAPElement dataInput = soapBody.addChildElement("GetCitizensDetailsReqData", ns);
          
            SOAPElement nlog = dataInput.addChildElement("log", com1);
            SOAPElement npass = dataInput.addChildElement("pass", com1);
            SOAPElement ndoc_type = dataInput.addChildElement("doc_type", com1);
            SOAPElement ndoc_number = dataInput.addChildElement("doc_number", com1);
            SOAPElement nserial_number = dataInput.addChildElement("serial_number", com1);
            


            nlog.addTextNode(iprsUserName);
            npass.addTextNode(iprsPassword);
            ndoc_type.addTextNode(doc_number);
            ndoc_number.addTextNode(doc_type);
            nserial_number.addTextNode(serial_number);

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "GetDetails" + "\"");
            

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(IprsApis.class.getName()).log(Level.SEVERE, null, ex);
            }

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            log.info(CommonMethods.soapMessageToString(SOAPMessage));
            log.info("\n---------------------------------End  SOAP Request ---------------------------------");


            SOAPMessageResponse = postIprsRequest((SOAPMessage),iprsEndpoint,soaPassword);

//            log.info(" Response  is  " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(IprsApis.class.getName()).log(Level.SEVERE, null, ex);
            log.info("\n--------------------------------- Start ERROR  Log ---------------------------------");
            log.info("ERROR while calling FT Webservice:" + ex);
            log.info("\n--------------------------------- ERROR ---------------------------------");
        }
        
        return SOAPMessageResponse;
    }
	
  
}


