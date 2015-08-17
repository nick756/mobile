package com.nova.sme.sme01.miscellanea;

//import android.sax.Element;

import android.content.res.Resources;

import com.nova.sme.sme01.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by User on 25.07.2015.
 */
public class DOMParser {

    DOMParser(String xml, boolean tf) {
        //Get the DOM Builder Factory
        DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
        DocumentBuilder        builder  = null;
        Document               document = null;

        String err="";
        //Get the DOM Builder
        try {
            builder  = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xml)));//ClassLoader.getSystemResourceAsStream(xml));
        } catch(javax.xml.parsers.ParserConfigurationException e) {
            err = e.toString();
        } catch(org.xml.sax.SAXException e) {
            err = e.toString();
        } catch(java.io.IOException e) {
            err = e.toString();
        } catch(Exception e) {
            err = e.toString();
        }
        if (err.length() > 0)
            return;



        //Iterating through the nodes and extracting the data.
        NodeList nodeList = document.getChildNodes();// .getDocumentElement().getChildNodes();
        NamedNodeMap attributes;

        String str, content;
        for (int i = 0; i < nodeList.getLength(); i++) {
            //We have encountered an <employee> tag.
            Node node = nodeList.item(i);
            str = node.getNodeName();

            attributes = node.getAttributes();
            if (node instanceof Element) {
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);
                    str        = cNode.getNodeName();

                    if (cNode instanceof Element) {
                        content = cNode.getLastChild().getTextContent().trim();
                        str     = cNode.getNodeName();
                    }
                }
            } else if (node instanceof Attributes) {

            }

        }
    }

    public DOMParser(String fileName, InputStream is) {//"res/layout/buttons.xml"
        //Get the DOM Builder Factory
        DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
        DocumentBuilder        builder  = null;
        Document               document = null;

        String err="";
        //Get the DOM Builder
        try {


            File fXmlFile = new File(fileName);
            builder       = factory.newDocumentBuilder();
            document      = builder.parse(fXmlFile);

            document.getDocumentElement().normalize();
/*
            NodeList nList = document.getElementsByTagName("*");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (eElement.hasAttribute(ANDROID_ID))
                        System.out.println("ID: "
                                + eElement.getAttribute(ANDROID_ID));
                }
            }
*/
        } catch(javax.xml.parsers.ParserConfigurationException e) {
            err = e.toString();
        } catch(org.xml.sax.SAXException e) {
            err = e.toString();
        } catch(java.io.IOException e) {
            err = e.toString();
        } catch(Exception e) {
            err = e.toString();
        }
        if (err.length() > 0)
            return;



        //Iterating through the nodes and extracting the data.
        NodeList nodeList = document.getChildNodes();// .getDocumentElement().getChildNodes();
        NamedNodeMap attributes;

        String str, content;
        for (int i = 0; i < nodeList.getLength(); i++) {
            //We have encountered an <employee> tag.
            Node node = nodeList.item(i);
            str = node.getNodeName();

            attributes = node.getAttributes();
            if (node instanceof Element) {
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);
                    str        = cNode.getNodeName();

                    if (cNode instanceof Element) {
                        content = cNode.getLastChild().getTextContent().trim();
                        str     = cNode.getNodeName();
                    }
                }
            } else if (node instanceof Attributes) {

            }

        }
    }

}
