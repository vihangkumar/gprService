package com.incomm.wmp.gprServices.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;

/**
 * Created by dvontela on 8/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDetailsResponse implements Serializable{

    private String expiryDate;
    private String cardStatusDescription;
    private String spendingAcctNum;
    private String spendingAcctBalance;
    private String cardNumber;
    private String bin;
    private String savingsAcctNum;
    protected String cardStatus;
    protected String lastUsed;
    protected String cardActivatedOn;
    protected String savingsAcctInfo;
    protected String savingsAccountCreationDate;
    protected String savingsAccountEligibility;
    protected String savingsAccountReopenDate;
    protected String savingsAcctBalance;
    protected String activeDate;
    protected String availedTransferCount;
    protected String availableTransferCount;
    protected String processorCardToken;
    protected String accountId;

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardStatusDescription() {
        return cardStatusDescription;
    }

    public void setCardStatusDescription(String cardStatusDescription) {
        this.cardStatusDescription = cardStatusDescription;
    }

    public String getSpendingAcctNum() {
        return spendingAcctNum;
    }

    public void setSpendingAcctNum(String spendingAcctNum) {
        this.spendingAcctNum = spendingAcctNum;
    }

    public String getSpendingAcctBalance() {
        return spendingAcctBalance;
    }

    public void setSpendingAcctBalance(String spendingAcctBalance) {
        this.spendingAcctBalance = spendingAcctBalance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSavingsAcctNum() {
        return savingsAcctNum;
    }

    public void setSavingsAcctNum(String savingsAcctNum) {
        this.savingsAcctNum = savingsAcctNum;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getCardActivatedOn() {
        return cardActivatedOn;
    }

    public void setCardActivatedOn(String cardActivatedOn) {
        this.cardActivatedOn = cardActivatedOn;
    }

    public String getSavingsAcctInfo() {
        return savingsAcctInfo;
    }

    public void setSavingsAcctInfo(String savingsAcctInfo) {
        this.savingsAcctInfo = savingsAcctInfo;
    }

    public String getSavingsAccountCreationDate() {
        return savingsAccountCreationDate;
    }

    public void setSavingsAccountCreationDate(String savingsAccountCreationDate) {
        this.savingsAccountCreationDate = savingsAccountCreationDate;
    }

    public String getSavingsAccountEligibility() {
        return savingsAccountEligibility;
    }

    public void setSavingsAccountEligibility(String savingsAccountEligibility) {
        this.savingsAccountEligibility = savingsAccountEligibility;
    }

    public String getSavingsAccountReopenDate() {
        return savingsAccountReopenDate;
    }

    public void setSavingsAccountReopenDate(String savingsAccountReopenDate) {
        this.savingsAccountReopenDate = savingsAccountReopenDate;
    }

    public String getSavingsAcctBalance() {
        return savingsAcctBalance;
    }

    public void setSavingsAcctBalance(String savingsAcctBalance) {
        this.savingsAcctBalance = savingsAcctBalance;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getAvailedTransferCount() {
        return availedTransferCount;
    }

    public void setAvailedTransferCount(String availedTransferCount) {
        this.availedTransferCount = availedTransferCount;
    }

    public String getAvailableTransferCount() {
        return availableTransferCount;
    }

    public void setAvailableTransferCount(String availableTransferCount) {
        this.availableTransferCount = availableTransferCount;
    }

    public String getProcessorCardToken() {
        return processorCardToken;
    }

    public void setProcessorCardToken(String processorCardToken) {
        this.processorCardToken = processorCardToken;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(this);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
