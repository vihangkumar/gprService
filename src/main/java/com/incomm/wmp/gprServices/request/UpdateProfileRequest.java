package com.incomm.wmp.gprServices.request;

public class UpdateProfileRequest {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String primaryPhoneNumber;
    private String secondaryPhoneNumber;

    private Address physicalAddress;
    private Address mailAddress;

    private boolean phyAddrMatchesMailAddr;
    private String smsAlertStatus;
    private boolean acceptSMS;
    private boolean acceptemailAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }


    public boolean isPhyAddrMatchesMailAddr() {
        return phyAddrMatchesMailAddr;
    }

    public void setPhyAddrMatchesMailAddr(boolean phyAddrMatchesMailAddr) {
        this.phyAddrMatchesMailAddr = phyAddrMatchesMailAddr;
    }

    public Address getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(Address physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Address getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(Address mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getSmsAlertStatus() {
        return smsAlertStatus;
    }

    public void setSmsAlertStatus(String smsAlertStatus) {
        this.smsAlertStatus = smsAlertStatus;
    }

    public boolean isAcceptSMS() {
        return acceptSMS;
    }

    public void setAcceptSMS(boolean acceptSMS) {
        this.acceptSMS = acceptSMS;
    }

    public boolean isAcceptemailAddress() {
        return acceptemailAddress;
    }

    public void setAcceptemailAddress(boolean acceptemailAddress) {
        this.acceptemailAddress = acceptemailAddress;
    }
}
