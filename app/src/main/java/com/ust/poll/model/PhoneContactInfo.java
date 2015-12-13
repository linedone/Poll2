// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.model;

import java.io.Serializable;

public class PhoneContactInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int PhoneContactID;
    private String ContactName;
    private String ContactNumber;

    public PhoneContactInfo(){

    }
    public PhoneContactInfo(int PhoneContactID, String ContactName, String ContactNumber){
        this.PhoneContactID = PhoneContactID;
        this.ContactName = ContactName;
        this.ContactNumber = ContactNumber;
    }

    public int getPhoneContactID() {
        return PhoneContactID;
    }
    public void setPhoneContactID(int PhoneContactID) {
        this.PhoneContactID = PhoneContactID;
    }

    public String getContactName() {
        return ContactName;
    }
    public void setContactName(String ContactName) {
        this.ContactName = ContactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }
    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }
}
