package com.nova.sme.sme01.xml.xmllogin;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 ***************************
 *                         *
 *   Deserializing of XML  *
 *                         *
 ***************************
 */

@Root(name="result")
public class XML_Login {


    @Attribute
    private String code;
    public String getCode() {return code;}

    @Attribute
    private String id;
    public  String getId() {return id;}

    @Element
    private String originator;
    public  String getOriginator(){return originator;}

    @Element
    private String description;
    public  String getDescription(){return description;}

    @Element
    private Operator operator;
    public Operator getOperator() {return operator;}

    // constructor for error hadling
    public XML_Login(String error_code) {
        this.code = error_code;
        operator  = new Operator();
    }

    public XML_Login() {
    }
}