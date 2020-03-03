package com.gospelnweke.businesscategorry.model;

public class BusinessUnit {

    private String businessName;
    private String businessCaption;
    private String businessPic;
    private String businessCategory;


    public BusinessUnit() {

    }

    public BusinessUnit( String businessName,String businessCaption, String businessPic, String businessCategory) {
        this.businessName = businessName;
        this.businessCaption = businessCaption;
        this.businessPic = businessPic;
        this.businessCategory=businessCategory;
    }

    public String getBusinessCaption() {
        return businessCaption;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public String getBusinessCategory() { return businessCategory; }

    public String getBusinessName() { return businessName; }
}
