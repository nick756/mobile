package com.nova.sme.sme01.xml_reader_classes;

/*
 ***************************
 *                         *
 *   Inner class of XML    *
 *   supportedOperations   *
 *                         *
 ***************************
*/

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="operation")
public class Operation implements Serializable {

    @Element(required = false, name = "code")
    private String code;
    public String getCode() {
        return code;
    }
    public void   setCode(String code) {this.code = code;}

    @Element(required = false, name = "name")
    private String name;
    public String getName() {
        return name;
    }
    public void   setName(String name) {this.name = name;}

    @Element(required = false, name = "inbound")
    private String inbound;
    public String getInbound() {
        return inbound;
    }
    public void   setInbound(String inbound) {this.inbound = inbound;}

    @Element(required = false, name = "outbound")
    private String outbound;
    public String getOutbound() {
        return outbound;
    }
    public void   setOutbound(String outbound) {this.outbound = outbound;}

    @Element(required = false, name = "type")
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {this.type = type;}


    public boolean equals(Operation obj) {
        if (!this.code.equals(obj.getCode()))
            return false;
        if (!this.name.equals(obj.getName()))
            return false;
        if (!this.inbound.equals(obj.getInbound()))
            return false;
        if(!this.outbound.equals(obj.getOutbound()))
            return false;
        return this.type.equals(obj.getType());
    }


    Operation() {

    }

}