package com.nova.sme.sme01.xml_reader_classes;

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
    protected String code;
    public String getCode() {return code;}

    @Attribute
    protected String id;
    public  String getId() {return id;}

    @Element
    protected String originator;
    public  String getOriginator(){return originator;}

    @Element(required = false, name = "description") // obsolete
    protected String description;
    public  String getDescription(){return description;}

    @Element(required = false, name = "resDescription")
    protected String res_description;
    public  String getResDescription() {return res_description;}
}
