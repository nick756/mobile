package com.nova.sme.sme01.xml_reader_classes;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import java.io.Serializable;

/*
 ***************************
 *                         *
 *   Inner class of XML    *
 *   to get transactions   *
 *                         *
 ***************************
*/


@Root(name="record")
public class Record  implements Serializable {

    @Element(required = false, name = "tranCode")
    private String tranCode;
    public  String getTranCode(){return tranCode;}
    public  void   setTranCode(String tranCode){this.tranCode = tranCode;}

    @Element(required = false, name = "date")
    private String date;
    public  String getDate(){return date;}
    public  void   setDate(String date){this.date = date;}

    @Element(required = false, name = "type")
    private String type;
    public  String getType(){return type;}
    public  void   setType(String type){this.type = type;}

    @Element(required = false, name = "amount")
    private String amount;
    public  String getAmount(){return amount;}
    public  void   setAmount(String amount){this.amount = amount;}

    @Element(required = false, name = "descr")
    private String descr;
    public  String getDescr(){return descr;}
    public  void   setDescr(String descr){this.descr = descr;}

    @Element(required = false, name = "operator")
    private String operator;
    public  String getOperator(){return operator;}
    public  void   setOperator(String operator){this.operator = operator;}

}
