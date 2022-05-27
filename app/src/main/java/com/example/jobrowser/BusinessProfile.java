package com.example.jobrowser;

public class BusinessProfile {
    private String businessName;
    private boolean office;
    private boolean lab;
    private boolean factory;
    private boolean workHome;
    private String description;

    public BusinessProfile(String businessName,
                           boolean office, boolean lab,
                           boolean factory,
                           boolean workHome,
                           String description)
    {
        this.businessName = businessName;
        this.office = office;
        this.lab = lab;
        this.factory = factory;
        this.workHome = workHome;
        this.description = description;
    }

}
