package com.incomm.wmp.gprServices.response;


public class PostedTransactionsResponse {

    protected String transDate;

	protected String transType;

    protected String transAmount;

    protected String transDescription;

    protected String referenceNumber;

    protected String merchant;

    protected String city;

    protected String state;

    protected String accountNumber;

    protected String transferAccountNumber;
    
    protected String transferCardNumberLastFourDigits;

    protected String cardNumberLastFourDigits;

    protected String accountBalance;


    public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
        this.transDate = transDate;
        if (transDate != null) {
            this.transDate = transDate.trim();
        }
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
        this.transType = transType;
        if (transType != null) {
            this.transType = transType.trim();
        }
	}

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransDescription() {
		return transDescription;
	}
	public void setTransDescription(String transDescription) {
		this.transDescription = transDescription;

	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransferAccountNumber() {
        return transferAccountNumber;
    }

    public void setTransferAccountNumber(String transferAccountNumber) {
        this.transferAccountNumber = transferAccountNumber;
    }

    public String getTransferCardNumberLastFourDigits() {
		return transferCardNumberLastFourDigits;
	}
	public void setTransferCardNumberLastFourDigits(
			String transferCardNumberLastFourDigits) {
		this.transferCardNumberLastFourDigits = transferCardNumberLastFourDigits;
	}
	public String getCardNumberLastFourDigits() {
        return cardNumberLastFourDigits;
    }

    public void setCardNumberLastFourDigits(String cardNumberLastFourDigits) {
        this.cardNumberLastFourDigits = cardNumberLastFourDigits;
    }
}
