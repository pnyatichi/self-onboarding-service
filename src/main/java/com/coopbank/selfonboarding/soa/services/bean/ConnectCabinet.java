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

import com.coopbank.selfonboarding.request.ConnectCabinetRequest;
import com.coopbank.selfonboarding.request.SigningDetailsRequest;
import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConnectCabinet {

	
    public  static SOAPMessage postConnectCabinet(SOAPMessage soapRequest,String connectCabinetEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  connectCabinetEndpoint;
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
    
    public static SOAPMessage postConnectCabinetReq(String connectCabinetName,String connectCabinetUserName,String connectCabinetPassword,String connectCabinetEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
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
            
            String ns2 = "ns2";
            String ns2URI = "urn://co-opbank.co.ke/DataModel/SimpleHeader/1.0";
            String ns1 = "ns1";
            String ns1URI = "urn://co-opbank.co.ke/DataModel/DocMgt/ConnectCabinet/Post/1.0/Body";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(ns2, ns2URI);
            envelope.addNamespaceDeclaration(ns1, ns1URI);
            
            SOAPHeader header = envelope.getHeader();
            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", ns2);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", ns2);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", ns2);
            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", ns2);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", ns2);
            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", ns2);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", ns2);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", ns2);
            SOAPElement userName = credentialss.addChildElement("Username", ns2);
            SOAPElement password = credentialss.addChildElement("Password", ns2);
            SOAPElement realm = credentialss.addChildElement("Realm", ns2);

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

            SOAPElement postInputRq = soapBody.addChildElement("PostInput", ns1);
            
            SOAPElement userDBIdValue = postInputRq.addChildElement("UserDBId", ns1);
            SOAPElement cabinetNameValue = postInputRq.addChildElement("CabinetName", ns1);
            SOAPElement userNameValue = postInputRq.addChildElement("UserName", ns1);
            SOAPElement userPasswordValue = postInputRq.addChildElement("UserPassword", ns1);
            SOAPElement creationDateTime = postInputRq.addChildElement("CreationDateTime", ns1);
            SOAPElement userExistValue = postInputRq.addChildElement("UserExist", ns1);
            SOAPElement mainGroupIndexValue = postInputRq.addChildElement("MainGroupIndex", ns1);
            SOAPElement userTypeValue = postInputRq.addChildElement("UserType", ns1);
            SOAPElement localeValue = postInputRq.addChildElement("Locale", ns1);
            SOAPElement applicationInfoValue = postInputRq.addChildElement("ApplicationInfo", ns1);
            SOAPElement applicationNameValue = postInputRq.addChildElement("ApplicationName", ns1);
       
       

            userDBIdValue.addTextNode("");
            cabinetNameValue.addTextNode(connectCabinetName);
            userNameValue.addTextNode(connectCabinetUserName);
            userPasswordValue.addTextNode(connectCabinetPassword);
            creationDateTime.addTextNode("");
            userExistValue.addTextNode("N");
            mainGroupIndexValue.addTextNode("1");
            userTypeValue.addTextNode("");
            localeValue.addTextNode("");
            applicationInfoValue.addTextNode("");
            applicationNameValue.addTextNode("");
            

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "Post" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(ConnectCabinet.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String soapEnv = new String(out.toByteArray());
            System.out.println("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = postConnectCabinet((SOAPMessage),connectCabinetEndpoint,soaPassword);

//            log.info("\n Response  is  for ID  " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(ConnectCabinet.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}


