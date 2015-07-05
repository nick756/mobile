package com.nova.sme.sme01.miscellanea;

/*
 **************************************************
 *                                                *
 *   Parameters of the application.               *
 *   To be saved, restored from the file          *
 *                                                *
 **************************************************
 */

public class Parameters {

    private String language;// EN MY
    public String getLanguage() {return this.language;}
    public void setLangauge(String lan) {this.language = lan;}


    private String id;
    public  String getId() {return this.id;}
    public  void   setId(String id){this.id = id;}


    private String companyID;
    public  String companyID() {return this.companyID;}
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

    private AllowedOperations  allowed_operations;

    public Parameters() {
        allowed_operations = new AllowedOperations();

    }

    private class AllowedOperations {

    }

}
