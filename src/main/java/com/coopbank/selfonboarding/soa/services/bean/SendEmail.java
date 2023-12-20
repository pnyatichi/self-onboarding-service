package com.coopbank.selfonboarding.soa.services.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import com.coopbank.selfonboarding.request.SendEmailRequest;
import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendEmail {

    public static SOAPMessage postSendEmailRequest(SOAPMessage soapRequest, String emailEndpoint, String soaPassword)
            throws IOException {
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
            String url = emailEndpoint;
            log.info("\n--------------------------------- SOAP Response ---------------------------------");
            soapResponse = soapConnection.call(soapRequest, url);

            CommonMethods.createSoapResponse(soapResponse);
            log.info("\n--------------------------------- SOAP Response ---------------------------------");

            soapConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapResponse;

    }

    public static SOAPMessage postKslSendEmail(String from, String to, String htmlContent, String subject,
            String emailEndpoint, String soaUsername, String soaPassword, String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;
        log.info("###############################################HTML CONTENT###############################################");
        log.info(htmlContent);
        log.info("###############################################HTML CONTENT###############################################");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();
            String myNamespace = "soapenv";

            String dat = "dat";
            String datURI = "urn://co-opbank.co.ke/Banking/Common/Service/CommonEmail/Send/1.0/DataIO";
            String com = "com";
            String comURI = "urn://co-opbank.co.ke/Banking/Common/DataModel/CommonEmail/Send/1.0/CommonEmail.send";
            String ns = "ns";
            String nsURI = "urn://co-opbank.co.ke/Banking/CanonicalDataModel/Email/1.0";
            String soap = "soap";
            String soapURI = "urn://co-opbank.co.ke/SharedResources/Schemas/SOAMessages/SoapHeader";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(dat, datURI);
            envelope.addNamespaceDeclaration(com, comURI);
            envelope.addNamespaceDeclaration(ns, nsURI);
            envelope.addNamespaceDeclaration(soap, soapURI);
            SOAPHeader header = envelope.getHeader();

            SOAPElement soapHeaderElem = header.addChildElement("HeaderRequest", soap);

            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", soap);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", soap);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", soap);
            SOAPElement replyTO = soapHeaderElem.addChildElement("ReplyTO", soap);
            SOAPElement faultTO = soapHeaderElem.addChildElement("FaultTO", soap);

            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", soap);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", soap);
            SOAPElement userName = credentialss.addChildElement("Username", soap);
            SOAPElement password = credentialss.addChildElement("Password", soap);
            SOAPElement realm = credentialss.addChildElement("Realm", soap);

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
            SOAPElement dataInput = soapBody.addChildElement("DataInput", dat);
            SOAPElement sendInputTag = dataInput.addChildElement("sendInput", com);
            SOAPElement operationParametersTag = sendInputTag.addChildElement("OperationParameters", com);
            SOAPElement operationDateTag = operationParametersTag.addChildElement("OperationDate", com);

            SOAPElement emailTag = sendInputTag.addChildElement("Email", ns);
            SOAPElement fromTag = emailTag.addChildElement("From", ns);
            SOAPElement toTag = emailTag.addChildElement("To", ns);
            SOAPElement subjectTag = emailTag.addChildElement("Subject", ns);
            SOAPElement sendDateTag = emailTag.addChildElement("SendDate", ns);
            SOAPElement messageTag = emailTag.addChildElement("Message", ns);

            operationDateTag.addTextNode(strDate);
            fromTag.addTextNode(from);
            toTag.addTextNode(to);
            subjectTag.addTextNode(subject);
            sendDateTag.addTextNode(strDate);

            // Set HTML content without CDATA
            messageTag.addTextNode(htmlContent);

            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "send" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SOAPMessage.writeTo(out);

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            log.info(CommonMethods.soapMessageToString(SOAPMessage));

            log.info("--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = postSendEmailRequest(SOAPMessage, emailEndpoint, soaPassword);

            log.info(" Response  is " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(SendEmailRequest.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
}
