package com.incomm.wmp.gprServices.controller;

import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.request.TransferFundsRequest;
import com.incomm.wmp.gprServices.request.UpdateAlertSettingsReq;
import com.incomm.wmp.gprServices.response.*;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
//import org.auth.cardmanagement.*;
import com.incomm.chstypes.*;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/gprServices")
public class CardController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String chsEndpointUrl;

    @Autowired
    @Qualifier("chsPort")
    private WebServiceTemplate templateCHSPort;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    StringEncryptor stringEncryptor;

    @Autowired
    VmsServiceUtil vmsServiceUtil;


    @RequestMapping(value = "/getCurrentMonthTransactions", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = CurrentMonthTransactionsResponse.class)
    })
    public ResponseEntity<Object> getCurrentMonthTransactions(@RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {

        logger.info("message = get current month transactions endpoint. Retrieving the session id, userEvent" + UserEvents.CURRENTMONTHTRANSACTIONS);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.CURRENTMONTHTRANSACTIONS);
        if (newSession != null) {
            try {

                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                GetCurrentMonthTransactionRequest getCurrentMonthTransactionRequest = new GetCurrentMonthTransactionRequest();
                getCurrentMonthTransactionRequest.setCustomerId(customerId);
                Header header = vmsServiceUtil.getCHSHeader(username, srcAppId, request);
                getCurrentMonthTransactionRequest.setHeader(header);

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                GetCurrentMonthTransactionResponse currMonthTxnResponse = (GetCurrentMonthTransactionResponse) templateCHSPort.marshalSendAndReceive(getCurrentMonthTransactionRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
 
               if (currMonthTxnResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                   logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                   CurrentMonthTransactionsResponse currentMonthTransactionsResponse = new CurrentMonthTransactionsResponse();
                   BeanUtils.copyProperties(currMonthTxnResponse, currentMonthTransactionsResponse);
                   currentMonthTransactionsResponse.setPreAuthTransactions(currMonthTxnResponse.getPreAuthTxnDetail());
                   currentMonthTransactionsResponse.setPostedTransactions(currMonthTxnResponse.getPostedTxnDetail());

                   BigDecimal camt = new BigDecimal(currMonthTxnResponse.getTotalCreditAmt());
                   camt.setScale(2);
                   String posAmt = camt.subtract(new BigDecimal(currMonthTxnResponse.getTotalDebitAmt())).toPlainString();
                   logger.debug("TotalPostedTransAmt:"+posAmt);
                   currentMonthTransactionsResponse.setPostedTransactionsAmount(posAmt);
                   return new ResponseEntity<>(currentMonthTransactionsResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured during get current month transactions, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(currMonthTxnResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_RETRIEVE_CURRENT_MONTH_TRANSACTIONS);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.CURRENTMONTHTRANSACTIONS);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);

            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.CURRENTMONTHTRANSACTIONS);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.CURRENTMONTHTRANSACTIONS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.CURRENTMONTHTRANSACTIONS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }

    }

    @RequestMapping(value = "/transferFunds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = TransferFundsResponse.class)
    })
    public ResponseEntity<Object> transferFunds(@RequestBody TransferFundsRequest transferFundsRequest, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {
        logger.info("message = transfer funds endpoint. Retrieving the session id, userEvent" + UserEvents.TRANSFERFUNDS);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.TRANSFERFUNDS);


        if (newSession != null) {
            try {
                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.TRANSFERFUNDS);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.TRANSFERFUNDS);
                CardToCardTransferRequest cardToCardTransferRequest = new CardToCardTransferRequest();
                cardToCardTransferRequest.setHeader(vmsServiceUtil.getCHSHeader(username, srcAppId, request));
                cardToCardTransferRequest.setCustomerId(customerId);
                BeanUtils.copyProperties(transferFundsRequest, cardToCardTransferRequest);

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.TRANSFERFUNDS);
                CardToCardTransferResponse transferResponse = (CardToCardTransferResponse) templateCHSPort.marshalSendAndReceive(cardToCardTransferRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.TRANSFERFUNDS);

                if (transferResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.TRANSFERFUNDS);
                    TransferFundsResponse transferFundsResponse = new TransferFundsResponse();
                    BeanUtils.copyProperties(transferResponse, transferFundsResponse);
                    return new ResponseEntity<>(transferFundsResponse, HttpStatus.OK);
                } else {
                    BaseResponse baseResponse = new BaseResponse();
                    logger.error("message = Error occured during transfer funds, userEvent=" + UserEvents.TRANSFERFUNDS);
                    BeanUtils.copyProperties(transferResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_TRANSFER_FUNDS);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }


            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS, userEvent=" + UserEvents.TRANSFERFUNDS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.TRANSFERFUNDS);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.TRANSFERFUNDS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.TRANSFERFUNDS);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.TRANSFERFUNDS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.TRANSFERFUNDS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.TRANSFERFUNDS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/updatePin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<BaseResponse> updatePin(@RequestBody com.incomm.wmp.gprServices.request.UpdatePinRequest
                                                          updatePin, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws
            UnknownHostException {
        logger.info("message = update pin endpoint. Retrieving the session id, userEvent" + UserEvents.UPDATEPIN);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.UPDATEPIN);
        if (newSession != null) {
            try {
                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.UPDATEPIN);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.UPDATEPIN);
                com.incomm.chstypes.UpdatePinRequest updatePinRequest = new com.incomm.chstypes.UpdatePinRequest();
                updatePinRequest.setHeader(vmsServiceUtil.getCHSHeader(username, srcAppId, request));
                updatePinRequest.setCustomerId(customerId);
                updatePinRequest.setOldPin(updatePin.getOldPin());
                updatePinRequest.setUpdatedPin(updatePin.getNewPin());

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.UPDATEPIN);
                UpdatePinResponse updatePinResponse = (UpdatePinResponse) templateCHSPort.marshalSendAndReceive(updatePinRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.UPDATEPIN);

                BaseResponse baseResponse = new BaseResponse();
                BeanUtils.copyProperties(updatePinResponse.getResponse(), baseResponse);

                if (updatePinResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.UPDATEPIN);
                    baseResponse.setRespCode(Constants.SUCCESS_UPDATE_PIN);
                    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured during update pin, userEvent=" + UserEvents.UPDATEPIN);
                    baseResponse.setRespCode(Constants.ERR_UPDATE_PIN);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS, userEvent=" + UserEvents.UPDATEPIN);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.UPDATEPIN);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.UPDATEPIN);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.UPDATEPIN);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.UPDATEPIN);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.UPDATEPIN);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.UPDATEPIN);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/getMonthlyTransactions", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = MonthlyStatementResponse.class)
    })
    public ResponseEntity<Object> getMonthlyTransactions(@RequestParam(value = "monthYear") String
                                                                 monthYear, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws
            UnknownHostException {
        logger.info("message = get monthly transactions endpoint. Retrieving the session id, userEvent" + UserEvents.MONTHLYTRANSACTION);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.MONTHLYTRANSACTION);
        if (newSession != null) {
            try {

                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                GetMonthlyStatementRequest getMonthlyStatementRequest = new GetMonthlyStatementRequest();
                getMonthlyStatementRequest.setHeader(vmsServiceUtil.getCHSHeader(username, srcAppId, request));
                getMonthlyStatementRequest.setCustomerId(customerId);
                getMonthlyStatementRequest.setMonthYear(monthYear);

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                GetMonthlyStatementResponse monthlyStatementResponse = (GetMonthlyStatementResponse) templateCHSPort.marshalSendAndReceive(getMonthlyStatementRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.MONTHLYTRANSACTION);

                if (monthlyStatementResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                    MonthlyStatementResponse monthlyStatementResponse1 = new MonthlyStatementResponse();
                    BeanUtils.copyProperties(monthlyStatementResponse, monthlyStatementResponse1);
                    monthlyStatementResponse1.setMonthlyStmtTransactions(monthlyStatementResponse.getTxnDetail());
                    return new ResponseEntity<>(monthlyStatementResponse1, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(monthlyStatementResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_MONTHLY_TRANSACTION);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.MONTHLYTRANSACTION);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.MONTHLYTRANSACTION);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.MONTHLYTRANSACTION);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.MONTHLYTRANSACTION);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.MONTHLYTRANSACTION);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/getCardDetails", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = CardDetailsResponse.class)
    })
    public ResponseEntity<Object> getCardDetails(@RequestHeader(value = "x-auth-token") String sessionId) throws
            UnknownHostException {
        logger.info("message = get card details endpoint. Retrieving the session id, userEvent" + UserEvents.CARDDETAILS);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.CARDDETAILS);
        if (newSession != null) {
            logger.info("message = retrieving the card details from session, userEvent" + UserEvents.CARDDETAILS);
            return new ResponseEntity<>(stringEncryptor.decrypt(newSession.getAttribute("cardDetails").toString()), HttpStatus.OK);
        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.CARDDETAILS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = UserDetailsResponse.class)
    })
    public ResponseEntity<Object> getUserDetails(@RequestHeader(value = "x-auth-token") String sessionId) throws
            UnknownHostException {
        logger.info("message = get user details endpoint. Retrieving the session id, userEvent" + UserEvents.USERDETAILS);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.USERDETAILS);
        if (newSession != null) {
            logger.info("message = retrieving the name from session, userEvent" + UserEvents.USERDETAILS);
            String firstname = stringEncryptor.decrypt(newSession.getAttribute("firstname").toString());
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
            userDetailsResponse.setFirstname(firstname);
            return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.USERDETAILS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/directDeposit", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = DirectDepositResponse.class)
    })
    public ResponseEntity<Object> directDeposit(@RequestHeader(value = "x-auth-token") String
                                                        sessionId, HttpServletRequest request) throws UnknownHostException {
        logger.info("message = direct deposit endpoint. Retrieving the session id, userEvent" + UserEvents.DIRECTDEPOSIT);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.DIRECTDEPOSIT);
        if (newSession != null) {
            try {
                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.DIRECTDEPOSIT);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.DIRECTDEPOSIT);
                DirectDepositFormRequest directDepositFormRequest = new DirectDepositFormRequest();
                directDepositFormRequest.setHeader(vmsServiceUtil.getCHSHeader(username, srcAppId, request));
                directDepositFormRequest.setCustomerId(customerId);

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.DIRECTDEPOSIT);
                DirectDepositFormResponse directDepositFormResponse = (DirectDepositFormResponse) templateCHSPort.marshalSendAndReceive(directDepositFormRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.DIRECTDEPOSIT);

                if (directDepositFormResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.DIRECTDEPOSIT);
                    DirectDepositResponse directDepositResponse = new DirectDepositResponse();
                    BeanUtils.copyProperties(directDepositFormResponse, directDepositResponse);
                    return new ResponseEntity<>(directDepositResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.DIRECTDEPOSIT);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(directDepositFormResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_DIRECT_DEPOSIT);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS, userEvent=" + UserEvents.DIRECTDEPOSIT);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.DIRECTDEPOSIT);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.DIRECTDEPOSIT);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.DIRECTDEPOSIT);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.DIRECTDEPOSIT);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + ", userEvent " + UserEvents.DIRECTDEPOSIT);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.DIRECTDEPOSIT);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }



    @RequestMapping(value = "/manageAlerts", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = AlertSettingsResponse.class)
    })
    public ResponseEntity<Object> retrieveAlerts(@RequestHeader(value = "x-auth-token") String
                                                                         sessionId, HttpServletRequest request) throws UnknownHostException {
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session for Retrieving Alert Settings, userEvent=" + UserEvents.MANAGEALERTS);

        if (newSession != null) {
            try {
                logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.MANAGEALERTS);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.debug("message = Setting header to request object, userEvent=" + UserEvents.MANAGEALERTS);
                Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);

                RetrieveAlertsRequest viewAlertsRequest = new RetrieveAlertsRequest();
                viewAlertsRequest.setHeader(chsHeader);
                viewAlertsRequest.setCustomerId(customerId);

                RetrieveAlertsResponse retrieveAlertsResponse = (RetrieveAlertsResponse) templateCHSPort.marshalSendAndReceive(viewAlertsRequest);
                if (retrieveAlertsResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.MANAGEALERTS);
                    AlertSettingsResponse alertSettingsResponse = new AlertSettingsResponse();
                    BeanUtils.copyProperties(retrieveAlertsResponse, alertSettingsResponse);
                    AlertInfo alertSettings = alertSettingsResponse.parseAlertSettingsInfo(retrieveAlertsResponse.getAlertsInfo());
                    alertSettingsResponse.setAlertInfo(alertSettings);
                    return new ResponseEntity<>(alertSettingsResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.MANAGEALERTS);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(retrieveAlertsResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_MANAGE_ALERTS);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }

            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.MANAGEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.MANAGEALERTS);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS WS, userEvent=" + UserEvents.MANAGEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.MANAGEALERTS);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS WS, userEvent=" + UserEvents.MANAGEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.MANAGEALERTS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            logger.error("message = Session expired userEvent=" + UserEvents.MANAGEALERTS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }

    }


    @RequestMapping(value = "/updateAlerts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = SetAlertsResponse.class)
    })
    public ResponseEntity<Object> updateProfile(@RequestBody UpdateAlertSettingsReq
                                                                   updateAlertsReq, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws
            UnknownHostException {
        logger.info("message =Update Alert Setting endpoint. Retrieving the session id, userEvent" + UserEvents.UPDATEALERTS);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.UPDATEALERTS);
        if (newSession != null) {
            try{
                logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.UPDATEALERTS);

                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                SetAlertsRequest saveAlertsRequest = new SetAlertsRequest();
                saveAlertsRequest.setHeader(vmsServiceUtil.getCHSHeader(username, srcAppId, request));
                saveAlertsRequest.setCustomerId(customerId);

                saveAlertsRequest.setEmailId(updateAlertsReq.getEmail());
                saveAlertsRequest.setCellphone(updateAlertsReq.getCellPhoneNo());

                saveAlertsRequest.setLoadOrCreditAlert(updateAlertsReq.getLoadOrCreditAlert());
                saveAlertsRequest.setLowBalAmt(updateAlertsReq.getLowBalAmount());
                saveAlertsRequest.setLowBalAlert(updateAlertsReq.getLowBalAlert());
                saveAlertsRequest.setNegativeBalAlert(updateAlertsReq.getNegativeBalAlert());
                saveAlertsRequest.setHighAuthAmt(updateAlertsReq.getHighAuthAmt());
                saveAlertsRequest.setHighAuthAlert(updateAlertsReq.getHighAuthAmtAlert());
                saveAlertsRequest.setDailyBalAlert(updateAlertsReq.getDailyBalAlert());
                saveAlertsRequest.setInsufficientAlert(updateAlertsReq.getInsufficientAlert());
                saveAlertsRequest.setIncorrectPinAlert(updateAlertsReq.getInCorrectPINAlert());

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.UPDATEALERTS);
                SetAlertsResponse saveAlertsResponse = (SetAlertsResponse) templateCHSPort.marshalSendAndReceive(saveAlertsRequest);
                logger.info("message = Response from CHS WS for, userEvent=" + UserEvents.UPDATEALERTS );

                BaseResponse response = new BaseResponse();
                BeanUtils.copyProperties(saveAlertsResponse.getResponse(),response);
                if (saveAlertsResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = successfully updated the Alerts, userEvent=" + UserEvents.UPDATEALERTS);
                    response.setRespCode(Constants.SUCCESS_UPDATE_ALERT);
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }else {
                    logger.error("message = Error occured while updating Alerts, userEvent=" + UserEvents.UPDATEALERTS);
                    response.setRespCode(Constants.ERR_UPDATE_ALERT);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEALERTS);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEALERTS);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEALERTS);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEALERTS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
          } else {
            logger.error("message = Session expired userEvent=" + UserEvents.UPDATEALERTS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    public String getChsEndpointUrl() {
        return chsEndpointUrl;
    }

    public void setChsEndpointUrl(String chsEndpointUrl) {
        this.chsEndpointUrl = chsEndpointUrl;
    }



}
