package com.nova.sme.sme01.xml_reader_classes;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/*
 ************************
 *                      *
 *   Contains records   *
 *                      *
 ************************
*/

@Root(name="records")
public class Records implements Serializable {

/*
    @Attribute(name="name")
    private String name;
    public  String getName() {return this.name;}
    public  void   setName(String name) {this.name = name;}

*/

    @ElementList(required = false, inline=true, name="record")
    private List<Record> list;
    public  List<Record> getRecordsList(){return this.list;}

    public Records(){}
}
