package com.nova.sme.sme01.transactions;

/*
 *************************************************
 *                                               *
 *   Deserialization of XML packet.              *
 *   Base class of getting possible operations   *
 *                                               *
 *************************************************
*/

import com.nova.sme.sme01.xml.xmllogin.Profile;

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

    @Element
    private Profile profile;
    public  Profile getProfile(){return profile;}
    public  List<Operation> getOperationsList() {return profile.getOperationsList(); }

    public GetOperations(){}

    public boolean equals(GetOperations obj) {

        if (!this.code.equals(obj.getCode()))
            return false;

        if (!this.id.equals(obj.getId()))
            return false;

        if(!this.originator.equals(obj.getOriginator()))
            return false;

        if (!this.description.equals(obj.getDescription()))
            return false;

        if (!this.profile.getName().equals(obj.getProfile().getName()))
            return false;

        List<Operation> olist = obj.getOperationsList();
        List<Operation> list  = getOperationsList();
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
}
