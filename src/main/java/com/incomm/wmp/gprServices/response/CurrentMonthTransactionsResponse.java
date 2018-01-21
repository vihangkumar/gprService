package com.incomm.wmp.gprServices.response;

import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dvontela on 8/11/2017.
 */
public class CurrentMonthTransactionsResponse {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String totalDebitAmt;
    protected String totalCreditAmt;
    protected String preAuthHoldAmt;
    protected String ledgerBalAmt;
    protected String availableBalAmt;
    protected String postedTransactionsAmount;
    protected List<PostedTransactionsResponse> postedTransDetails;
    protected List<PreAuthTransactionsResponse> preAuthTransDetails;

    public String getTotalDebitAmt() {
        return totalDebitAmt;
    }

    public void setTotalDebitAmt(String totalDebitAmt) {
        this.totalDebitAmt = totalDebitAmt;
    }

    public String getTotalCreditAmt() {
        return totalCreditAmt;
    }

    public void setTotalCreditAmt(String totalCreditAmt) {
        this.totalCreditAmt = totalCreditAmt;
    }

    public String getPreAuthHoldAmt() {
        return preAuthHoldAmt;
    }

    public void setPreAuthHoldAmt(String preAuthHoldAmt) {
        this.preAuthHoldAmt = preAuthHoldAmt;
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

    public List<PostedTransactionsResponse> getPostedTransDetails() {
        return postedTransDetails;
    }

    public void setPostedTransDetails(List<PostedTransactionsResponse> postedTransDetails) {
        this.postedTransDetails = postedTransDetails;
    }

    public List<PreAuthTransactionsResponse> getPreAuthTransDetails() {
        return preAuthTransDetails;
    }

    public void setPreAuthTransDetails(List<PreAuthTransactionsResponse> preAuthTransDetails) {
        this.preAuthTransDetails = preAuthTransDetails;
    }

    public String getPostedTransactionsAmount() {
        return postedTransactionsAmount;
    }

    public void setPostedTransactionsAmount(String postedTransactionsAmount) {
        this.postedTransactionsAmount = postedTransactionsAmount;
    }



    public void setPostedTransactions(String postedTransDetails) {
        ArrayList<PostedTransactionsResponse> transDetailList = new ArrayList<PostedTransactionsResponse>();
        String[] postedTransactionsArray  = postedTransDetails.split("\\|\\|");
        logger.debug("Posted Transactions Array size is : " + postedTransactionsArray.length);

        if(postedTransactionsArray.length > 0) {
            for(String postedTrans : postedTransactionsArray ) {
                String[] postedDetail = postedTrans.split("~", -1);
                if(postedDetail.length >= 13) {
                    PostedTransactionsResponse pt = new PostedTransactionsResponse();
                    pt.setTransDate(postedDetail[0].trim());
                    pt.setTransType(postedDetail[1].trim());
                    pt.setTransDescription(postedDetail[3].trim());
                    pt.setReferenceNumber(postedDetail[4].trim());
                    pt.setMerchant(postedDetail[5].trim());
                    pt.setCity(postedDetail[6].trim());
                    pt.setState(postedDetail[7].trim());
                    pt.setTransAmount(postedDetail[8].trim());
                    pt.setAccountBalance(postedDetail[9].trim());
                    pt.setAccountNumber(postedDetail[10].trim());
                    pt.setTransferAccountNumber(postedDetail[11].trim());
                    pt.setCardNumberLastFourDigits(postedDetail[12].trim());
                    pt.setTransferCardNumberLastFourDigits(postedDetail[13].trim());
                    transDetailList.add(pt);
                }
            }
        }
        setPostedTransDetails(transDetailList);
    }

    public void setPreAuthTransactions(String preAuthTransDetails) {
        ArrayList<PreAuthTransactionsResponse> transDetailList = new ArrayList<PreAuthTransactionsResponse>();
        String[] pendingTransactionsArray  = preAuthTransDetails.split("\\|\\|");
        logger.debug("PreAuth Transactions Array size is : " + pendingTransactionsArray.length);
        if(pendingTransactionsArray.length > 0) {
            for(String preAuthTrans : pendingTransactionsArray ) {
                String[] preAuthDetail = preAuthTrans.split("~", -1);
                if(preAuthDetail.length >= 9) {
                    PreAuthTransactionsResponse pat = new PreAuthTransactionsResponse();
                    pat.setTransDate(preAuthDetail[0].trim());
                    pat.setTransDescription(preAuthDetail[2].trim());
                    pat.setReferenceNumber(preAuthDetail[3].trim());
                    pat.setMerchant(preAuthDetail[4].trim());
                    pat.setCity(preAuthDetail[5].trim());
                    pat.setState(preAuthDetail[6].trim());
                    pat.setTransAmount(preAuthDetail[7].trim());
                    pat.setCardNumberLastFourDigits(preAuthDetail[8].trim());
                    transDetailList.add(pat);
                }
            }
        }
        setPreAuthTransDetails(transDetailList);
    }


}
