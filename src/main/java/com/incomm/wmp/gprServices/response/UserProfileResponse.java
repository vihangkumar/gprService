package com.incomm.wmp.gprServices.response;

public class UserProfileResponse{

    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String mobileNumber;
    private String smsAlertStatus;

    private AddressResponse physicalAddress;
    private AddressResponse mailingAddress;

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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSmsAlertStatus() {
        return smsAlertStatus;
    }

    public void setSmsAlertStatus(String smsAlertStatus) {
        this.smsAlertStatus = smsAlertStatus;
    }

    public AddressResponse getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(AddressResponse physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public AddressResponse getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(AddressResponse mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
