package com.nova.sme.sme01;

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
    public CommonClass() {

    }
}
