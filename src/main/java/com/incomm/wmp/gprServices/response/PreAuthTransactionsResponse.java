package com.incomm.wmp.gprServices.response;


public class PreAuthTransactionsResponse {

    protected String transDate;

    protected String transAmount;

    protected String transDescription;

    protected String referenceNumber;

    protected String merchant;

    protected String city;

    protected String state;

    protected String cardNumberLastFourDigits;


    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
        if (transDate != null) {
            this.transDate = transDate.trim();
        }
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
        if (transAmount != null) {
            this.transAmount = transAmount.trim();
        }
    }

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCardNumberLastFourDigits() {
        return cardNumberLastFourDigits;
    }

    public void setCardNumberLastFourDigits(String cardNumberLastFourDigits) {
        this.cardNumberLastFourDigits = cardNumberLastFourDigits;
    }
}
