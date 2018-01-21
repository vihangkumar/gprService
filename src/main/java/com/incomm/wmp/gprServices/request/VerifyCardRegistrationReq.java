package com.incomm.wmp.gprServices.request;

/**
 * Created by snellipudi on 8/11/2017.
 */
public class VerifyCardRegistrationReq {

    String cardNumber;
    String cvv;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
