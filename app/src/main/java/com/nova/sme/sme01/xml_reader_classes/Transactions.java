package com.nova.sme.sme01.xml_reader_classes;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import java.io.Serializable;
import java.util.List;


/*
 ****************************
 *                          *
 *   Transactions are sent  *
 *   by Server              *
 *                          *
 ****************************
*/


@Root(name="result")
public class Transactions implements Serializable {
    // --- must be used inheritance finally here
    @Attribute
    private String code;
    public  String getCode() {return this.code;}

    @Attribute
    private String id;
    public  String getId() {return this.id;}

    @Element(required = false, name="originator")
    private String originator;
    public  String getOriginator(){return this.originator;}

    @Element(required = false, name = "resDescription")
    private String res_description;
    public  String getResDescription() {return res_description;}
    // --- must be used inheritance finally here

    @Element(required = false, name = "recordCount")
    private String recordCount;
    public  String getRecordCount() {return recordCount;}

    @Element(required=false, name="records")
    private Records records;
    public  Records getRecords(){return records;}
    public List<Record> getRecordsList() {return records.getRecordsList(); }

    public Transactions(){}
}
