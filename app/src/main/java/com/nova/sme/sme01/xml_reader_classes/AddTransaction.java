package com.nova.sme.sme01.xml_reader_classes;


import org.simpleframework.xml.Element;

public class AddTransaction extends BaseXML {

    @Element(required = false, name = "transactionID")
    private String transactionID;
    public  String gettransactionId() {return transactionID;}

}
