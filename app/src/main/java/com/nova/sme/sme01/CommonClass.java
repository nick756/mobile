package com.nova.sme.sme01;

import com.nova.sme.sme01.miscellanea.Parameters;

import java.io.Serializable;

/*
 **********************************************
 *                                            *
 *   Used for inter activities communication  *
 *         passes parameters                  *
 *                                            *
 **********************************************
 */

public class CommonClass implements Serializable {
    public String curr_language;

    public String code;
    public String id;// 3
    public String originator;
    public String descr;

    public String name;
    public String role;
    public String company;
    public String companyID;
    public String sender;

    public String dateFrom;
    public String dateTill;
    public String operationName;
    public int    help_id;

    public CommonClass(String code, String id, String originator,  String descr, String name, String role, String company, String companyID) {
        this.code       = code;
        this.id         = id;
        this.originator = originator;
        this.descr      = descr;
        this.name       = name;
        this.role       = role;
        this.company    = company;
        this.companyID  = companyID;
    }
/*
    public void getFromCommonClass(CommonClass c_c){
        this.setId(c_c.id);
        this.setOrdinator(c_c.originator);
        this.setDescription(c_c.descr);
        this.setName(c_c.name);
        this.setRole(c_c.role);
        this.setCompany(c_c.company);
        this.setcompanyID(c_c.companyID);
        this.setLangauge(c_c.curr_language);
    }

*/

    public CommonClass(Parameters params) {
        this.code       = "0";
        this.id         = params.getId();
        this.originator = params.getOriginator();
        this.descr      = params.getDescription();
        this.name       = params.getName();
        this.role       = params.getRole();
        this.company    = params.getCompany();
        this.companyID  = params.getcompanyID();
    }
    public CommonClass() {

    }
}
