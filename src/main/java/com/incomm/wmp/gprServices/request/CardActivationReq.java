package com.incomm.wmp.gprServices.request;

/**
 * Created by snellipudi on 8/14/2017.
 */
public class CardActivationReq {

    private String cardNumber;

    private String pin;

    private String securityCode;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

}
