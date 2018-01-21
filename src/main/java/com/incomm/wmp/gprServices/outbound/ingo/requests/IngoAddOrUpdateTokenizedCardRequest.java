package com.incomm.wmp.gprServices.outbound.ingo.requests;

import javax.validation.constraints.NotNull;


/**
 * @author dvontela
 */
public class IngoAddOrUpdateTokenizedCardRequest extends BaseRequest{

    @NotNull
    private String customerId;
    @NotNull
    private String cardToken;
    @NotNull
    private String cardBin;
    private String lastFourOfCard;
    private String expirationMonthYear;
    @NotNull
    private String cardNickname;
    private String nameOnCard;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getLastFourOfCard() {
        return lastFourOfCard;
    }

    public void setLastFourOfCard(String lastFourOfCard) {
        this.lastFourOfCard = lastFourOfCard;
    }

    public String getExpirationMonthYear() {
        return expirationMonthYear;
    }

    public void setExpirationMonthYear(String expirationMonthYear) {
        this.expirationMonthYear = expirationMonthYear;
    }

    public String getCardNickname() {
        return cardNickname;
    }

    public void setCardNickname(String cardNickname) {
        this.cardNickname = cardNickname;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
