package com.nova.sme.sme01.miscellanea;

import com.nova.sme.sme01.xml.xmllogin.Operator;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 ***************************
 *                         *
 *   Base calss for  XML   *
 *   Deserialization       *
 *                         *
 ***************************
 */

@Root(name="result")
public class BaseXML {

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

}
