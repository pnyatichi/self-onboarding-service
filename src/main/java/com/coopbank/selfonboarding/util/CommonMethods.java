package com.coopbank.selfonboarding.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.Logger;
import org.json.*;

@Slf4j
public class CommonMethods {

	 

	  
public  static void createSoapResponse(SOAPMessage soapResponse) throws Exception {


    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    Source sourceContent = soapResponse.getSOAPPart().getContent();
    System.out.println("\n--------------------------------- Start SOAP Response ---------------------------------");
    StreamResult result = new StreamResult(System.out);  
    transformer.transform(sourceContent, result);
    System.out.println("\n--------------------------------- End SOAP Response ---------------------------------");

}


/*//*/
public static String soapMessageToString(SOAPMessage message) {
    String result = null;
    if (message != null) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            result = baos.toString();

        } catch (Exception e) {
            log.info("Error in conversion from soap to string:" + e);
            return null;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ioe) {
                    log.info("Error in baos:" + ioe);
                    return null;
                }
            }
        }
    }
    return result;
}

public static void doTrustToCertificates() throws Exception {
    // Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
     TrustManager[] trustAllCerts = new TrustManager[]{
             new X509TrustManager() {
                 public X509Certificate[] getAcceptedIssuers() {
                     return null;
                 }

                 public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                     return;
                 }

                 public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                     return;
                 }
             }
     };

     SSLContext sc = SSLContext.getInstance("SSL");
     sc.init(null, trustAllCerts, null);
     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
     HostnameVerifier hv = new HostnameVerifier() {
         public boolean verify(String urlHostName, SSLSession session) {
             if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
//                 LOGGER.info(DomainUtils.LOGID + ("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'."));
                 log.info("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
             }
             return true;
         }
     };
     HttpsURLConnection.setDefaultHostnameVerifier(hv);
 }


public static String convertToString(SOAPBody message) throws Exception {
    Document doc = message.extractContentAsDocument();
    StringWriter sw = new StringWriter();
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.transform(new DOMSource(doc), new StreamResult(sw));
    return sw.toString();
}


public static String convertHeaderString(SOAPHeader header) throws SOAPException, TransformerException {
	    Document doc = header.getOwnerDocument();
	    StringWriter sw = new StringWriter();
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.transform(new DOMSource(doc), new StreamResult(sw));
	    return sw.toString();
	}

}

