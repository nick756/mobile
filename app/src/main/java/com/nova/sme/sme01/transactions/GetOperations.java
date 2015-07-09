package com.nova.sme.sme01.transactions;

/*
 *************************************************
 *                                               *
 *   Deserialization of XML packet.              *
 *   Base class of getting possible operations   *
 *                                               *
 *************************************************
*/

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="result")
public class GetOperations {

    @Attribute
    private String code;
    public String getCode() {return this.code;}

    @Attribute
    private String id;
    public  String getId() {return this.id;}

    @Element(required = false, name="orginator")
    private String originator;
    public  String getOriginator(){return this.originator;}

    @Element
    private String description;
    public  String getDescription(){return this.description;}

    @ElementList(required=false, name="supportedOperations")
    private List<Operation> list;
    public List<Operation> getOperationsList(){return this.list;}

//    @Element(required=false, name="supportedOperations")
//    SupportedOperations supportedOperations;
//    public SupportedOperations getSupportedOpeartions(){return this.supportedOperations;}

}
