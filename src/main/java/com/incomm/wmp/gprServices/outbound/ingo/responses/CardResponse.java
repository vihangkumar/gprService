package com.incomm.wmp.gprServices.outbound.ingo.responses;

/**
 * @author dvontela
 */
public class CardResponse {
    private String cardId;
    private String cardNickname;
    private String cardProgram;
    private String customerId;
    private String lastFourDigits;
    private String expirationMonthYear;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNickname() {
        return cardNickname;
    }

    public void setCardNickname(String cardNickname) {
        this.cardNickname = cardNickname;
    }

    public String getCardProgram() {
        return cardProgram;
    }

    public void setCardProgram(String cardProgram) {
        this.cardProgram = cardProgram;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getExpirationMonthYear() {
        return expirationMonthYear;
    }

    public void setExpirationMonthYear(String expirationMonthYear) {
        this.expirationMonthYear = expirationMonthYear;
    }
}
