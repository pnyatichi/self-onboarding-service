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

import com.coopbank.selfonboarding.request.CreateDocumentRequest;
import com.coopbank.selfonboarding.request.RetailCustomerCreate;
import com.coopbank.selfonboarding.util.CommonMethods;
import lombok.extern.slf4j.Slf4j;


import com.coopbank.selfonboarding.request.createDocumentData.documentDatas;
import com.coopbank.selfonboarding.request.createDocumentData.documentData;


@Slf4j
public class CreateDocument {

	
    public  static SOAPMessage postCreateDocument(SOAPMessage soapRequest,String createDocumentEndpoint,String soaPassword) throws MalformedURLException,
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
    String url =  createDocumentEndpoint;
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
    
    public static SOAPMessage postCreateDocumentReq(CreateDocumentRequest createDocumentReqData,String userId,String createDocumentEndpoint,String soaUsername,String soaPassword,String soaSystemCode) {
        SOAPMessage SOAPMessageResponse = null;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String strDate = formatter.format(date);
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage SOAPMessage = messageFactory.createMessage();
            SOAPPart soapPart = SOAPMessage.getSOAPPart();
            String reference = UUID.randomUUID().toString();
            String myNamespace = "soapenv";
            
            String ns = "ns";
            String nsURI = "urn://co-opbank.co.ke/DataModel/SimpleHeader/1.0";
            
            String body = "body";
            String bodyURI = "urn://co-opbank.co.ke/DataModel/DocMgt/CreateDocument/Post/1.0/Body";

            String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();

            envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            envelope.addNamespaceDeclaration(ns, nsURI);
            envelope.addNamespaceDeclaration(body, bodyURI);
            
            SOAPHeader header = envelope.getHeader();
            SOAPElement soapHeaderElem = header.addChildElement("RequestHeader", ns);
            SOAPElement creationTimestamp = soapHeaderElem.addChildElement("CreationTimestamp", ns);
            SOAPElement correlationID = soapHeaderElem.addChildElement("CorrelationID", ns);
            SOAPElement messageID = soapHeaderElem.addChildElement("MessageID", ns);
            SOAPElement credentialss = soapHeaderElem.addChildElement("Credentials", ns);

            SOAPElement systemCode = credentialss.addChildElement("SystemCode", ns);


            creationTimestamp.addTextNode(strDate);
            correlationID.addTextNode(reference);
            messageID.addTextNode(reference);
            systemCode.addTextNode(soaSystemCode);


            SOAPBody soapBody = envelope.getBody();

            SOAPElement postInputRq = soapBody.addChildElement("PostInput", body);
            
            SOAPElement cabinetName = postInputRq.addChildElement("cabinetName", body);
            cabinetName.addTextNode("coopcabuat");

            SOAPElement documentPath = postInputRq.addChildElement("documentPath", body);
            documentPath.addTextNode("");

            SOAPElement parentFolderIndex = postInputRq.addChildElement("parentFolderIndex", body);
            parentFolderIndex.addTextNode("1273");

            SOAPElement documentName = postInputRq.addChildElement("documentName",body);
            documentName.addTextNode(createDocumentReqData.getAccName()+".pdf");

            SOAPElement userDBId = postInputRq.addChildElement("userDBId",body);
            userDBId.addTextNode(userId);

            SOAPElement volumeId = postInputRq.addChildElement("volumeId",body);
            volumeId.addTextNode("1");

            SOAPElement versionFlag = postInputRq.addChildElement("versionFlag",body);
            versionFlag.addTextNode("N");

            SOAPElement accessType = postInputRq.addChildElement("accessType",body);
            accessType.addTextNode("");

            SOAPElement enableLog = postInputRq.addChildElement("enableLog",body);
            enableLog.addTextNode("N");

            SOAPElement comment = postInputRq.addChildElement("comment",body);
            comment.addTextNode("Some sample document");

            SOAPElement author = postInputRq.addChildElement("author",body);
            author.addTextNode("");

            SOAPElement ownerIndex = postInputRq.addChildElement("ownerIndex",body);
            ownerIndex.addTextNode("1");
            
            SOAPElement nameLength = postInputRq.addChildElement("nameLength",body);
            nameLength.addTextNode("");
            
            SOAPElement versionComment = postInputRq.addChildElement("versionComment",body);
            versionComment.addTextNode("v1");
            
            SOAPElement textAlsoFlag = postInputRq.addChildElement("textAlsoFlag",body);
            textAlsoFlag.addTextNode("");
            
            SOAPElement imageData = postInputRq.addChildElement("imageData",body);
            imageData.addTextNode("");
            
            SOAPElement validateDocumentImage = postInputRq.addChildElement("validateDocumentImage",body);
            validateDocumentImage.addTextNode("N");
            
            SOAPElement ownerType = postInputRq.addChildElement("ownerType",body);
            ownerType.addTextNode("");
            
            SOAPElement thumbNailFlag = postInputRq.addChildElement("thumbNailFlag",body);
            thumbNailFlag.addTextNode("N");
            
            SOAPElement userNameVal = postInputRq.addChildElement("userName",body);
            userNameVal.addTextNode("");
            
            SOAPElement userPassword = postInputRq.addChildElement("userPassword",body);
            userPassword.addTextNode("");
            
            SOAPElement encrFlag = postInputRq.addChildElement("encrFlag",body);
            encrFlag.addTextNode("N");
            
            
            SOAPElement document = postInputRq.addChildElement("document",body);
            document.addTextNode(createDocumentReqData.getDocument());
            
            // ...
//            documentDatas documentDatas = createDocumentReqData.getDocumentDatas();

            // Create documentDatas element
            SOAPElement documentDatasElem = postInputRq.addChildElement("NGOAddDocDataDefCriterionBDO", body);

            // Add elements for documentDatas
            documentDatasElem.addChildElement("dataDefIndex", body).addTextNode("146");
            documentDatasElem.addChildElement("dataDefName", body).addTextNode("Account Opening Mandate");

//            List<documentData> documentDataList = documentDatas.getDocumentData();

//            for (documentData documentData : documentDataList) {
                // Create documentData element
                SOAPElement documentDataElem = documentDatasElem.addChildElement("NGOAddDocDataDefCriteriaDataBDO", body);
                // Add elements for documentData
                documentDataElem.addChildElement("indexId", body).addTextNode("870");
                documentDataElem.addChildElement("indexType", body).addTextNode("S");
                documentDataElem.addChildElement("indexValue", body).addTextNode("Mandate");
                
                SOAPElement accDocumentDataElem = documentDatasElem.addChildElement("NGOAddDocDataDefCriteriaDataBDO", body);
                
                accDocumentDataElem.addChildElement("indexId", body).addTextNode("869");
                accDocumentDataElem.addChildElement("indexType", body).addTextNode("S");
                accDocumentDataElem.addChildElement("indexValue", body).addTextNode(createDocumentReqData.getAccNumber());
                
                SOAPElement custNoDocumentDataElem = documentDatasElem.addChildElement("NGOAddDocDataDefCriteriaDataBDO", body);
                
                custNoDocumentDataElem.addChildElement("indexId", body).addTextNode("892");
                custNoDocumentDataElem.addChildElement("indexType", body).addTextNode("S");
                custNoDocumentDataElem.addChildElement("indexValue", body).addTextNode(createDocumentReqData.getCustNumber());
                
                
                SOAPElement idNoDocumentDataElem = documentDatasElem.addChildElement("NGOAddDocDataDefCriteriaDataBDO", body);
                
                idNoDocumentDataElem.addChildElement("indexId", body).addTextNode("905");
                idNoDocumentDataElem.addChildElement("indexType", body).addTextNode("S");
                idNoDocumentDataElem.addChildElement("indexValue", body).addTextNode(createDocumentReqData.getIdNumberNumber());
                
                SOAPElement accNameDocumentDataElem = documentDatasElem.addChildElement("NGOAddDocDataDefCriteriaDataBDO", body);
                
                accNameDocumentDataElem.addChildElement("indexId", body).addTextNode("893");
                accNameDocumentDataElem.addChildElement("indexType", body).addTextNode("S");
                accNameDocumentDataElem.addChildElement("indexValue", body).addTextNode(createDocumentReqData.getAccName());
              
//            }
       
            SOAPElement NGOAddDocKeywordsCriterionBDO = postInputRq.addChildElement("NGOAddDocKeywordsCriterionBDO",body);
            SOAPElement keyword = NGOAddDocKeywordsCriterionBDO.addChildElement("keyword",body);
            keyword.addTextNode("");
          
            MimeHeaders headers = SOAPMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "\"" + "Post" + "\"");

            String authorization = Base64.getEncoder().encodeToString((soaUsername + ":" + soaPassword).getBytes());
            headers.addHeader("Authorization", "Basic " + authorization);

            SOAPMessage.saveChanges();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                SOAPMessage.writeTo(out);
            } catch (IOException ex) {
                Logger.getLogger(CreateDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String soapEnv = new String(out.toByteArray());
            System.out.println("\n--------------------------------- SOAP Request ---------------------------------");
          
            log.info("request is " + CommonMethods.soapMessageToString(SOAPMessage));

            log.info("\n--------------------------------- SOAP Request ---------------------------------");

            SOAPMessageResponse = postCreateDocument((SOAPMessage),createDocumentEndpoint,soaPassword);

//            log.info("\n Response  is  for ID  " + SOAPMessageResponse);

        } catch (Exception ex) {
            Logger.getLogger(CreateDocument.class.getName()).log(Level.SEVERE, null, ex);
            log.info("ERROR while calling FT Webservice:" + ex);
        }

        return SOAPMessageResponse;
    }
    
}


