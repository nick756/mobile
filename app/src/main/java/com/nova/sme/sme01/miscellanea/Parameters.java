package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Parameters of the application.               *
 *   To be saved, restored from the file          *
 *                                                *
 **************************************************
 */

import com.nova.sme.sme01.xml.xmllogin.Operator;
import com.nova.sme.sme01.xml.xmllogin.XML_Login;

import java.io.Serializable;

import static java.sql.DriverManager.println;

public class Parameters implements Serializable  {

    private String language = "EN";// EN MY
    public  String getLanguage() {
        if (this.language == null)
            return "EN";
        return this.language;
    }
    public  void setLangauge(String lan) {this.language = lan;}


    private String id;
    public  String getId() {return this.id;}
    public  void   setId(String id){this.id = id;}


    private String companyID;
    public  String getcompanyID() {return this.companyID;}
    public  void   setcompanyID(String cid){this.companyID = cid;}


    private String originator;
    public  String getOriginator(){return this.originator;}
    public  void   setOrdinator(String ordinator){this.originator = ordinator;}


    private String description;
    public  String getDescription(){return this.description;}
    public  void   setDescription(String description){this.description = description;}


    private String name;
    public  String getName() {return this.name;}
    public  void   setName(String name){this.name = name;}


    private String role;
    public  String getRole() {return this.role;}
    public  void   setRole(String role) {this.role = role;}


    private String company;
    public  String getCompany() {return this.company;}
    public  void   setCompany(String company) {this.company = company;}

    public void getFromXML(XML_Login xml_login) {
       // this.language
        this.id          = xml_login.getId();
        this.originator  = xml_login.getOriginator();
        this.description = xml_login.getDescription();

        Operator operator = xml_login.getOperator();
        if (operator != null) {
            this.name      = operator.getName();
            this.role      = operator.getRole();
            this.company   = operator.getCompany();
            this.companyID = operator.getCompanyID();
        } else {
            this.name      = "no data";
            this.role      = "no data";
            this.company   = "no data";
            this.companyID = "no data";
        }
    }

/*
    public String getSerializedString() {
        String str = "";

        str += "language="    + prepareToWrite(this.language)    + ";";
        str += "id="          + prepareToWrite(this.id)          + ";";
        str += "companyID="   + prepareToWrite(this.companyID)   + ";";
        str += "originator="  + prepareToWrite(this.originator)  + ";";
        str += "description=" + prepareToWrite(this.description) + ";";
        str += "name="        + prepareToWrite(this.name)        + ";";
        str += "role="        + prepareToWrite(this.role)        + ";";
        str += "company="     + prepareToWrite(this.company)     + ";";

        return str;
    }
    public void takeSerializedString(String str) {
        String s, val_name, val;
        int    pos, pos1;
        int    counter = 0;

        while(true) {
            pos = str.indexOf(";");
            if (pos == -1) break;
            s = str.substring(0, pos);
            pos1 = s.indexOf("=");
            if (pos1 == -1)
                break;
            val_name = s.substring(0, pos1);
            val      = s.substring(pos1 + 1);
            if (val_name.equals("language"))
                this.language = val;
            else if(val_name.equals("id"))
                this.id = val;
            else if(val_name.equals("companyID"))
                this.companyID = val;
            else if(val_name.equals("originator"))
                this.originator = val;
            else if(val_name.equals("description"))
                this.description = val;
            else if(val_name.equals("name"))
                this.name = val;
            else if(val_name.equals("role"))
                this.role = val;
            else if(val_name.equals("company"))
                this.company = val;
            else
                println("error " + val_name); // error

            str = str.substring(pos + 1);
            if (str.length() == 0)
                break;

            if ((++counter) > 1000) // something wrong happened
                break;
        }
    }

    private String prepareToWrite(String val) {
        if (val == null)
            return "no data";
        if (val.length() == 0)
            return "no data";

        return val;
    }
*/
}
