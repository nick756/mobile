package com.nova.sme.sme01.xml.xmllogin;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 ****************************************
 *                                      *
 *   Inner class of XML Deserializator  *
 *                                      *
 ***************************************
 */

@Root(name="operator")
public class Operator {
    @Element(required=false, name = "name")
    private String name;
    public String getName() {return name;}

    @Element(required=false, name = "role")
    private String role;
    public String getRole() {return role;}

    @Element(required=false, name = "company")
    private String company;
    public String getCompany() {return company;}

    @Element(required=false, name = "companyID")
    private String companyID;
    public String getCompanyID() {return companyID;}
}
