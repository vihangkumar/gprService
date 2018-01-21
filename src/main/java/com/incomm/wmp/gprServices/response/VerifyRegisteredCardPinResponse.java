package com.incomm.wmp.gprServices.response;

public class VerifyRegisteredCardPinResponse {

    private String customerId;
    private String cardStatus;
    private String cardStatusDesc;
    private String expiryDate;
    private String activeDate;
    private String ledgerBalAmt;
    private String availableBalAmt;
    private String initialLoad;
    private String zip;
    private String phyZip;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardStatusDesc() {
        return cardStatusDesc;
    }

    public void setCardStatusDesc(String cardStatusDesc) {
        this.cardStatusDesc = cardStatusDesc;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getLedgerBalAmt() {
        return ledgerBalAmt;
    }

    public void setLedgerBalAmt(String ledgerBalAmt) {
        this.ledgerBalAmt = ledgerBalAmt;
    }

    public String getAvailableBalAmt() {
        return availableBalAmt;
    }

    public void setAvailableBalAmt(String availableBalAmt) {
        this.availableBalAmt = availableBalAmt;
    }

    public String getInitialLoad() {
        return initialLoad;
    }

    public void setInitialLoad(String initialLoad) {
        this.initialLoad = initialLoad;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhyZip() {
        return phyZip;
    }

    public void setPhyZip(String phyZip) {
        this.phyZip = phyZip;
    }




}
