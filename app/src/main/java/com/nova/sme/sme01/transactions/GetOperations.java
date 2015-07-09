package com.nova.sme.sme01.transactions;

/*
 *************************************************
 *                                               *
 *   Deserialization of XML packet.              *
 *   Base class of getting possible operations   *
 *                                               *
 *************************************************
*/

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Root(name="result")
public class GetOperations implements Serializable {

    @Attribute
    private String code;
    public String getCode() {return this.code;}

    @Attribute
    private String id;
    public  String getId() {return this.id;}

    @Element(required = false, name="originator")
    private String originator;
    public  String getOriginator(){return this.originator;}

    @Element
    private String description;
    public  String getDescription(){return this.description;}

    @ElementList(required=false, name="supportedOperations")
    private ArrayList<Operation> list;
    public  ArrayList<Operation> getOperationsList(){return this.list;}

    public GetOperations(){}

    public GetOperations(GetOperations obj) {
        this.code        = obj.getCode();
        this.id          = obj.getId();
        this.originator  = obj.getOriginator();
        this.description = obj.getDescription();

        this.list = new ArrayList<Operation>(obj.getOperationsList());
    }

    public boolean equals(GetOperations obj) {
        if (!this.code.equals(obj.getCode()))
            return false;

        if (!this.id.equals(obj.getId()))
            return false;

        if(!this.originator.equals(obj.getOriginator()))
            return false;

        if (!this.description.equals(obj.getDescription()))
            return false;


        ArrayList<Operation> olist = obj.getOperationsList();
        if (list == null && olist != null)
            return false;
        if (list != null && olist == null)
            return false;

        if (list != null && olist != null) {
            if (list.size() != olist.size())
                return false;

            for (int i = 0; i < list.size(); i++)
                if (!list.get(i).equals(olist.get(i)))
                    return false;
        }

        return true;
    }

/*
    public String getSerializedString() {
        String    str = new String();
        Operation operation;
        int       size;

        if (list != null) {
            if ((size = list.size()) > 0) {
                for(int i = 0; i < size; i ++) {
                    operation = list.get(i);
                    str += "code="     + operation.getCode()     + ";";
                    str += "name="     + operation.getName()     + ";";
                    str += "inbound="  + operation.getInbound()  + ";";
                    str += "outbound=" + operation.getOutbound() + ";";
                    str += "type="     + operation.getType()     + ";";
               }
            }
        }
        return str;
    }

    public List<Operation> getDeserialized(String str) {
        String          s;
        List<Operation> list = null;
        Operation       operation;
        int             pos, pos1, counter = 0, cnt;
        if (str.length() > 0) {
            while(true) {
                if ((++counter) > 1000) {
                    // error something wrong with string
                    return null;
                }
                pos       = str.indexOf(";");
                if (pos == -1)
                    break;

                operation = new Operation();
                cnt = 0;
                while(true) {
                    if ((++cnt) > 1000) {
                        // error something wrong with string
                        return null;
                    }
                    if (str.indexOf("code=") == 0) {
                        operation.setCode(str.substring(5, pos - 1));
                    } else if (str.indexOf("name=") == 0) {
                        operation.setName(str.substring(5, pos - 1));
                    } else if (str.indexOf("inbound=") == 0) {
                        operation.setInbound(str.substring(8, pos - 1));
                    } else if (str.indexOf("outbound=") == 0) {
                        operation.setOutbound(str.substring(9, pos - 1));
                    } else if (str.indexOf("type=") == 0) {
                        operation.setType(str.substring(5, pos - 1));
                        list.add(operation);
                        break;
                    } else {
                        break;
                    }
                    str = str.substring(pos + 1);
                }


//                str = str.substring(pos + 1);
                if (str.length() == 0)
                    break;
            }
        }
        return list;
    }
    */
}
