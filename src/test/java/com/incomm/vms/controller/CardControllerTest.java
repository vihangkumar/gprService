package com.incomm.vms.controller;

import com.incomm.vms.request.TransferFundsRequest;
import com.incomm.vms.request.UpdatePinRequest;
import com.incomm.vms.response.BaseResponse;
import org.auth.cardmanagement.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by dvontela on 8/17/2017.
 */
public class CardControllerTest extends BaseRunner {

    @Before
    public void setup() {
        super.setUp();
    }

    @Test
    public void getCurrentMonthTransactions() throws Exception {
        /*
        mockServices();
        GetCurrentMonthTransactionResponse getCurrentMonthTransactionResponse = new GetCurrentMonthTransactionResponse();
        getCurrentMonthTransactionResponse.setResponse(getResponse());
        getCurrentMonthTransactionResponse.setAvailableBalAmt("12212");
        getCurrentMonthTransactionResponse.setLedgerBalAmt("12");
        getCurrentMonthTransactionResponse.setPostedTxnDetail("weww");
        getCurrentMonthTransactionResponse.setLedgerBalAmt("erfe");
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(getCurrentMonthTransactionResponse);
        ResponseEntity<Object> objectResponseEntity = cardController.getCurrentMonthTransactions("abcd", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
        */
    }

    @Test
    public void transferFunds() throws Exception {
        TransferFundsRequest transferFundsRequest = new TransferFundsRequest();
        transferFundsRequest.setPin("258");
        transferFundsRequest.setSocialSecurityNumber("5896");
        transferFundsRequest.setToCardNumber("589633244777");
        transferFundsRequest.setTransactionAmt("58");

        CardToCardTransferResponse transferFundsResponse = new CardToCardTransferResponse();
        transferFundsResponse.setResponse(getResponse());
        transferFundsResponse.setCardBalance("256");
        mockServices();
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(transferFundsResponse);
        ResponseEntity<Object> objectResponseEntity = cardController.transferFunds(transferFundsRequest, "072017", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void updatePin() throws Exception {

        UpdatePinRequest updatePinRequest = new UpdatePinRequest();
        updatePinRequest.setOldPin("5454");
        updatePinRequest.setNewPin("5454");
        updatePinRequest.setConfirmPin("254568");

        UpdatePinResponse updatePinResponse = new UpdatePinResponse();
        updatePinResponse.setResponse(getResponse());
        mockServices();
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(updatePinResponse);
        ResponseEntity<BaseResponse> objectResponseEntity = cardController.updatePin(updatePinRequest, "072017", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void getMonthlyTransactions() throws Exception {

        GetMonthlyStatementResponse monthlyStatementResponse = new GetMonthlyStatementResponse();
        monthlyStatementResponse.setResponse(getResponse());
        monthlyStatementResponse.setAvailableBalAmt("85414");
        monthlyStatementResponse.setCustomerId("9685456454");
        monthlyStatementResponse.setLedgerBalAmt("75485454");
        monthlyStatementResponse.setTxnDetail("5465669556254");
        mockServices();
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(monthlyStatementResponse);
        ResponseEntity<Object> objectResponseEntity = cardController.getMonthlyTransactions("072017", "ddsww", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void getCardDetails() throws Exception {
        mockServices();
        ResponseEntity<Object> objectResponseEntity = cardController.getCardDetails("072017");
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void getUserDetails() throws Exception {
        mockServices();
        ResponseEntity<Object> objectResponseEntity = cardController.getUserDetails("072017");
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void directDeposit() throws Exception {

        DirectDepositFormResponse directDepositFormResponse = new DirectDepositFormResponse();
        directDepositFormResponse.setResponse(getResponse());
        directDepositFormResponse.setRoutingNumber("546857548");
        directDepositFormResponse.setAccountNumber("546897568");
        directDepositFormResponse.setCustomerId("5468956");
        mockServices();
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(directDepositFormResponse);
        ResponseEntity<Object> objectResponseEntity = cardController.directDeposit("072017", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

}