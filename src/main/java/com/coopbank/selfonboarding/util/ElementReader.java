package com.coopbank.selfonboarding.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
public class ElementReader {

    // Method to get the value of a specified element in the XML document
    public static String getElementValue(Document doc, String elementName) {
        String value = "";
        try {
            NodeList nodeList = doc.getElementsByTagName(elementName);
            if (nodeList != null && nodeList.getLength() > 0) {
                Element element = (Element) nodeList.item(0);
                if (element != null && element.getFirstChild() != null) {
                    value = element.getFirstChild().getNodeValue();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return value;
    }

}


