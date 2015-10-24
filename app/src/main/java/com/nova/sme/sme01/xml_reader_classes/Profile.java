package com.nova.sme.sme01.xml_reader_classes;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 ***************************
 *                         *
 *   Inner class of XML    *
 *   Used in GetOperations *
 *   as an element         *
 *                         *
 ***************************
*/

@Root(name="profile")
public class Profile implements Serializable {
    @Attribute(name="name")
    private String name;
    public  String getName() {return this.name;}
    public  void   setName(String name) {this.name = name;}

    @ElementList(required = false, inline=true, name="operation")
    private List<Operation> list;
    public  List<Operation> getOperationsList(){renameInOut();return this.list;}

    Profile(){}

    private void renameInOut() {
        if (this.list == null)
            return;

        Operation operation;
        String operationType;
        for (int j = 0; j < this.list.size(); j ++) {
            operation     = list.get(j);
            operationType = operation.getName();

            if (operationType.indexOf("KELUAR:") == 0) { //OUT
                operationType = operationType.replace("KELUAR:", "");//"OUT");
                operation.setName(operationType.trim());
                list.set(j, operation);
            } else if (operationType.indexOf("MASUK:") == 0) {//IN
                operationType = operationType.replace("MASUK:", "");//"IN");
                operation.setName(operationType.trim());
                list.set(j, operation);
            } else if (operationType.indexOf("OUT:") == 0) { //OUT
                operationType = operationType.replace("OUT:", "");//"OUT");
                operation.setName(operationType.trim());
                list.set(j, operation);
            } else if (operationType.indexOf("IN:") == 0) {//IN
                operationType = operationType.replace("IN:", "");//"IN");
                operation.setName(operationType.trim());
                list.set(j, operation);
            }
        }
    }

    public void sort() {
        if (this.list != null)
            if (this.list.size() > 1)
                Collections.sort(this.list, new CompareByInBoundAndType());// CustomComparator());
    }

    private class CustomComparator implements Comparator<Operation> {
        @Override
        public int compare(Operation o1, Operation o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private class CompareByInBoundAndType implements Comparator<Operation> {
        @Override
        public int compare(Operation o1, Operation o2) {
            int in_compare = o2.getInbound().compareTo(o1.getInbound());
            if (in_compare != 0) {
                return in_compare;
            } else {
                return o1.getType().compareTo(o2.getType());
            }
        }
    }

    //getInbound()
    //getType
    /*
public class PersonComparator implements Comparator<? extends Person> {

  public int compare(Person p1, Person p2) {
     int nameCompare = p1.name.compareToIgnoreCase(p2.name);
     if (nameCompare != 0) {
        return nameCompare;
     } else {
       return Integer.valueOf(p1.age).compareTo(Integer.valueOf(p2.age));
     }
  }
}

     */
}
