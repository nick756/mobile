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
    public  List<Record> getRecordsList(){renameInOut(); return this.list;}

    public Records(){}

    private void renameInOut() {
        if (this.list == null)
            return;

        Record record;
        String operationType;
        for (int j = 0; j < this.list.size(); j ++) {
            record        = list.get(j);
            operationType = record.getType();

            if (operationType.indexOf("KELUAR") == 0) { //OUT
                operationType = operationType.replace("KELUAR", "OUT");
                record.setType(operationType);
                list.set(j, record);
            } else if (operationType.indexOf("MASUK") == 0) {//IN
                operationType = operationType.replace("MASUK", "IN");
                record.setType(operationType);
                list.set(j, record);
            }
        }
    }
}
