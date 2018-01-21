package com.incomm.wmp.gprServices.response;

import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by dvontela on 8/11/2017.
 */
public class MonthlyStatementResponse {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String totalDebitAmt;
    protected String totalCreditAmt;
    protected String ledgerBalAmt;
    protected String availableBalAmt;

    protected List<PostedTransactionsResponse> monthlyStmtTransCardDetails;

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

    public void setMonthlyStmtTransactions(String monthlyTransDetails) {

        ArrayList<PostedTransactionsResponse> monthlyFundTransDetailList = new ArrayList<PostedTransactionsResponse>();
        ArrayList<PostedTransactionsResponse> monthlyCardTransDetailList = new ArrayList<PostedTransactionsResponse>();
        String[] monthlyTransactionsArray  = monthlyTransDetails.split("\\|\\|");
        logger.debug("Monthly Transactions Array size is : " + monthlyTransactionsArray.length);
        if(monthlyTransactionsArray.length > 0) {
            for(String postedTrans : monthlyTransactionsArray ) {
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

                    monthlyCardTransDetailList.add(pt);
                }
            }
        }
        setMonthlyStmtTransCardDetails(monthlyCardTransDetailList);
    }

    public List<PostedTransactionsResponse> getMonthlyStmtTransCardDetails() {
        return monthlyStmtTransCardDetails;
    }

    public void setMonthlyStmtTransCardDetails(List<PostedTransactionsResponse> monthlyStmtTransCardDetails) {
        this.monthlyStmtTransCardDetails = monthlyStmtTransCardDetails;
    }


}
