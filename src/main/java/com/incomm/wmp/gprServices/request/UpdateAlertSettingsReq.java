package com.incomm.wmp.gprServices.request;

public class UpdateAlertSettingsReq {


    String cellPhoneNo;
    String email;
    String loadOrCreditAlert;
    String lowBalAlert;
    String lowBalAmount;
    String negativeBalAlert;
    String highAuthAmtAlert;
    String highAuthAmt;
    String dailyBalAlert;
    String insufficientAlert;
    String inCorrectPINAlert;



    public String getCellPhoneNo() {
        return cellPhoneNo;
    }

    public void setCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoadOrCreditAlert() {
        return loadOrCreditAlert;
    }

    public void setLoadOrCreditAlert(String loadOrCreditAlert) {
        this.loadOrCreditAlert = loadOrCreditAlert;
    }

    public String getLowBalAlert() {
        return lowBalAlert;
    }

    public void setLowBalAlert(String lowBalAlert) {
        this.lowBalAlert = lowBalAlert;
    }

    public String getLowBalAmount() {
        return lowBalAmount;
    }

    public void setLowBalAmount(String lowBalAmount) {
        this.lowBalAmount = lowBalAmount;
    }

    public String getNegativeBalAlert() {
        return negativeBalAlert;
    }

    public void setNegativeBalAlert(String negativeBalAlert) {
        this.negativeBalAlert = negativeBalAlert;
    }

    public String getHighAuthAmtAlert() {
        return highAuthAmtAlert;
    }

    public void setHighAuthAmtAlert(String highAuthAmtAlert) {
        this.highAuthAmtAlert = highAuthAmtAlert;
    }

    public String getHighAuthAmt() {
        return highAuthAmt;
    }

    public void setHighAuthAmt(String highAuthAmt) {
        this.highAuthAmt = highAuthAmt;
    }

    public String getDailyBalAlert() {
        return dailyBalAlert;
    }

    public void setDailyBalAlert(String dailyBalAlert) {
        this.dailyBalAlert = dailyBalAlert;
    }

    public String getInsufficientAlert() {
        return insufficientAlert;
    }

    public void setInsufficientAlert(String insufficientAlert) {
        this.insufficientAlert = insufficientAlert;
    }

    public String getInCorrectPINAlert() {
        return inCorrectPINAlert;
    }

    public void setInCorrectPINAlert(String inCorrectPINAlert) {
        this.inCorrectPINAlert = inCorrectPINAlert;
    }




}
