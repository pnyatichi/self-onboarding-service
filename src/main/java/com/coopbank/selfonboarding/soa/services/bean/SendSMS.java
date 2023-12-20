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
//import javax.xml.transform.Source;


@Slf4j
public class SendSMS {


	    public  static SOAPMessage postSMS(SOAPMessage soapRequest,String smsEndpoint,String soaPassword) throws MalformedURLException,
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
	    String url =  smsEndpoint;
//		log.info("\n--------------------------------- SOAP Response & URL ---------------------------------");
//	    log.info("request is == " + soapRequest +" url == "+url);
//		log.info("\n--------------------------------- SOAP Response & URL ---------------------------------");

//		log.info("\n--------------------------------- SOAP Response ---------------------------------");
	    soapResponse = soapConnection.call(soapRequest, url);
	    CommonMethods.createSoapResponse(soapResponse);
//		log.info("\n--------------------------------- SOAP Response ---------------------------------");
	    soapConnection.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return soapResponse;

	}
	    
	    public static SOAPMessage postSendSMS(String smsUsername,String smsPassword,String smsEncryped,String smsClientID,String reqmessage, String reqmsisdn,String smsEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
			 
	        SOAPMessage SOAPMessageResponse = null;
//	        SOAPMessage response = null;

	        Date date = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        String strDate = formatter.format(date);
	        try {
	            MessageFactory messageFactory = MessageFactory.newInstance();
	            SOAPMessage SOAPMessage = messageFactory.createMessage();
	            SOAPPart soapPart = SOAPMessage.getSOAPPart();
	            String reference = UUID.randomUUID().toString();
	            String myNamespace = "soapenv";
	            
	            String ns3 = "ns3";
	            String ns3URI = "urn://co-opbank.co.ke/CommonServices/Data/Message/MessageHeader";
	            String ns2 = "ns2";
	            String ns2URI = "urn://co-opbank.co.ke/CommonServices/Data/Common";
	            String ns1 = "ns1";
	            String ns1URI = "urn://co-opbank.co.ke/Banking/Common/SMS/1.0";

	            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
	            // SOAP Envelope
	            SOAPEnvelope envelope = soapPart.getEnvelope();

	            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
	            envelope.addNamespaceDeclaration(ns3, ns3URI);
	            envelope.addNamespaceDeclaration(ns2, ns2URI);
	            envelope.addNamespaceDeclaration(ns1, ns1URI);
	            SOAPHeader header = envelope.getHeader();
	            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", ns3);
	            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", ns2);
	            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", ns2);
	            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", ns3);
	            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", ns3);
	            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", ns3);


	            creationTimestamp.addTextNode(strDate);
	            correlationID.addTextNode(reference);
	            faultTO.addTextNode("");
	            replyTO.addTextNode("");
	            messageID.addTextNode(reference);     

	            SOAPBody soapBody = envelope.getBody();

	            SOAPElement smsRequest = soapBody.addChildElement("SMSRequest", ns1);
	            SOAPElement smsBody = smsRequest.addChildElement("SMSBody",ns1);
	            SOAPElement messageIDTag = smsBody.addChildElement("messageID");
	            SOAPElement messageTag = smsBody.addChildElement("message");
	            SOAPElement msisdnTag = smsBody.addChildElement("MSISDN");
	            SOAPElement CredentialsTag = smsBody.addChildElement("Credentials");
	            SOAPElement usernameTag = CredentialsTag.addChildElement("username");
	            SOAPElement passwordTag = CredentialsTag.addChildElement("password");
	            SOAPElement encrypedTag = CredentialsTag.addChildElement("encryped");
	            SOAPElement clientIDTag = CredentialsTag.addChildElement("clientID");
	            

	            messageIDTag.addTextNode(reference);
	            messageTag.addTextNode(reqmessage);
	            msisdnTag.addTextNode(reqmsisdn);
	            usernameTag.addTextNode(smsUsername);
	            passwordTag.addTextNode(smsPassword);
	            encrypedTag.addTextNode(smsEncryped);
	            clientIDTag.addTextNode(smsClientID);
	            

	            MimeHeaders headers = SOAPMessage.getMimeHeaders();
	            headers.addHeader("SOAPAction", "\"" + "SendSMS" + "\"");

	            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
	            headers.addHeader("Authorization", "Basic " + authorization);

	            SOAPMessage.saveChanges();

	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            try {
	                SOAPMessage.writeTo(out);
	            } catch (IOException ex) {
	                Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);
	            }
//	            String soapEnv = new String(out.toByteArray());
				log.info("\n--------------------------------- SOAP Request ---------------------------------");
	          
	            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

				log.info("\n--------------------------------- SOAP Request ---------------------------------");

	            SOAPMessageResponse = postSMS((SOAPMessage),smsEndpoint,soaPassword);

	            log.info(" Response  is  for ID "/*+idNumber*/ + " is " + SOAPMessageResponse);

	        } catch (Exception ex) {
	            Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);
	            log.info("ERROR while calling FT Webservice:" + ex);
	        }

	        return SOAPMessageResponse;
	    }

}
