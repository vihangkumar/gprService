package com.incomm.wmp.gprServices.response;


public class AlertInfo {
	
	private String emailAddress;
	
	private String mobilePhoneNumber;
	
	private boolean loadOrCreditEmailAlert;
	
	private boolean loadOrCreditSMSAlert;
	
	private boolean lowBalanceEmailAlert;
	
	private boolean lowBalanceSMSAlert;
	
	private String lowBalanceAmount;
	
	private boolean negativeBalanceEmailAlert;
	
	private boolean negativeBalanceSMSAlert;
	
    private boolean highAuthAmountEmailAlert;
	
	private boolean highAuthAmountSMSAlert;
	
	private String highAuthAmount;
	
	private boolean dailyBalanceEmailAlert;
	
	private boolean dailyBalanceSMSAlert;
	
	private boolean insufficientFundsEmailAlert;
	
	private boolean insufficientFundsSMSAlert;
	
	private boolean incorrectPinUsageEmailAlert;
	
	private boolean incorrectPinUsageSMSAlert;

    private boolean fast50SMSAlert;

    private boolean fast50EmailAlert;

    private boolean federalStateTaxRefundAlertSMSAlert;

    private boolean federalStateTaxRefundAlertEmailAlert;


	public boolean isLoadOrCreditEmailAlert() {
		return loadOrCreditEmailAlert;
	}

	public void setLoadOrCreditEmailAlert(boolean loadOrCreditEmailAlert) {
		this.loadOrCreditEmailAlert = loadOrCreditEmailAlert;
	}

	public boolean isLoadOrCreditSMSAlert() {
		return loadOrCreditSMSAlert;
	}

	public void setLoadOrCreditSMSAlert(boolean loadOrCreditSMSAlert) {
		this.loadOrCreditSMSAlert = loadOrCreditSMSAlert;
	}

	public boolean isLowBalanceEmailAlert() {
		return lowBalanceEmailAlert;
	}

	public void setLowBalanceEmailAlert(boolean lowBalanceEmailAlert) {
		this.lowBalanceEmailAlert = lowBalanceEmailAlert;
	}

	public boolean isLowBalanceSMSAlert() {
		return lowBalanceSMSAlert;
	}

	public void setLowBalanceSMSAlert(boolean lowBalanceSMSAlert) {
		this.lowBalanceSMSAlert = lowBalanceSMSAlert;
	}

	public String getLowBalanceAmount() {
		return lowBalanceAmount;
	}

	public void setLowBalanceAmount(String lowBalanceAmount) {
		this.lowBalanceAmount = lowBalanceAmount;
	}

	public boolean isNegativeBalanceEmailAlert() {
		return negativeBalanceEmailAlert;
	}

	public void setNegativeBalanceEmailAlert(boolean negativeBalanceEmailAlert) {
		this.negativeBalanceEmailAlert = negativeBalanceEmailAlert;
	}

	public boolean isNegativeBalanceSMSAlert() {
		return negativeBalanceSMSAlert;
	}

	public void setNegativeBalanceSMSAlert(boolean negativeBalanceSMSAlert) {
		this.negativeBalanceSMSAlert = negativeBalanceSMSAlert;
	}

	public boolean isHighAuthAmountEmailAlert() {
		return highAuthAmountEmailAlert;
	}

	public void setHighAuthAmountEmailAlert(boolean highAuthAmountEmailAlert) {
		this.highAuthAmountEmailAlert = highAuthAmountEmailAlert;
	}

	public boolean isHighAuthAmountSMSAlert() {
		return highAuthAmountSMSAlert;
	}

	public void setHighAuthAmountSMSAlert(boolean highAuthAmountSMSAlert) {
		this.highAuthAmountSMSAlert = highAuthAmountSMSAlert;
	}

	public String getHighAuthAmount() {
		return highAuthAmount;
	}

	public void setHighAuthAmount(String highAuthAmount) {
		this.highAuthAmount = highAuthAmount;
	}

	public boolean isDailyBalanceEmailAlert() {
		return dailyBalanceEmailAlert;
	}

	public void setDailyBalanceEmailAlert(boolean dailyBalanceEmailAlert) {
		this.dailyBalanceEmailAlert = dailyBalanceEmailAlert;
	}

	public boolean isDailyBalanceSMSAlert() {
		return dailyBalanceSMSAlert;
	}

	public void setDailyBalanceSMSAlert(boolean dailyBalanceSMSAlert) {
		this.dailyBalanceSMSAlert = dailyBalanceSMSAlert;
	}

	public boolean isInsufficientFundsEmailAlert() {
		return insufficientFundsEmailAlert;
	}

	public void setInsufficientFundsEmailAlert(boolean insufficientFundsEmailAlert) {
		this.insufficientFundsEmailAlert = insufficientFundsEmailAlert;
	}

	public boolean isInsufficientFundsSMSAlert() {
		return insufficientFundsSMSAlert;
	}

	public void setInsufficientFundsSMSAlert(boolean insufficientFundsSMSAlert) {
		this.insufficientFundsSMSAlert = insufficientFundsSMSAlert;
	}

	public boolean isIncorrectPinUsageEmailAlert() {
		return incorrectPinUsageEmailAlert;
	}

	public void setIncorrectPinUsageEmailAlert(boolean incorrectPinUsageEmailAlert) {
		this.incorrectPinUsageEmailAlert = incorrectPinUsageEmailAlert;
	}

	public boolean isIncorrectPinUsageSMSAlert() {
		return incorrectPinUsageSMSAlert;
	}

	public void setIncorrectPinUsageSMSAlert(boolean incorrectPinUsageSMSAlert) {
		this.incorrectPinUsageSMSAlert = incorrectPinUsageSMSAlert;
	}

    public boolean isFast50SMSAlert() {
        return fast50SMSAlert;
    }

    public void setFast50SMSAlert(boolean fast50SMSAlert) {
        this.fast50SMSAlert = fast50SMSAlert;
    }

    public boolean isFast50EmailAlert() {
        return fast50EmailAlert;
    }

    public void setFast50EmailAlert(boolean fast50EmailAlert) {
        this.fast50EmailAlert = fast50EmailAlert;
    }

    public boolean isFederalStateTaxRefundAlertSMSAlert() {
        return federalStateTaxRefundAlertSMSAlert;
    }

    public void setFederalStateTaxRefundAlertSMSAlert(boolean federalStateTaxRefundAlertSMSAlert) {
        this.federalStateTaxRefundAlertSMSAlert = federalStateTaxRefundAlertSMSAlert;
    }

    public boolean isFederalStateTaxRefundAlertEmailAlert() {
        return federalStateTaxRefundAlertEmailAlert;
    }

    public void setFederalStateTaxRefundAlertEmailAlert(boolean federalStateTaxRefundAlertEmailAlert) {
        this.federalStateTaxRefundAlertEmailAlert = federalStateTaxRefundAlertEmailAlert;
    }

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

}
