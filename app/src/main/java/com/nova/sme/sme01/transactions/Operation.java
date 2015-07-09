package com.nova.sme.sme01.transactions;

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

@Root(name="operation")
public class Operation {

    @Element(required=false, name = "code")
    private String code;
    public String getCode() {return code;}

    @Element(required=false, name = "name")
    private String name;
    public String getName() {return name;}

    @Element(required=false, name = "inbound")
    private String inbound;
    public String getInbound() {return inbound;}

    @Element(required=false, name = "outbound")
    private String outbound;
    public String getOutbound() {return outbound;}

    @Element(required=false, name = "type")
    private String type;
    public String getType() {return type;}
}
