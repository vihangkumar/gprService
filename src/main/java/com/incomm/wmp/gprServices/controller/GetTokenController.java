package com.incomm.wmp.gprServices.controller;


import com.incomm.chstypes.GetCurrentMonthTransactionRequest;
import com.incomm.chstypes.GetCurrentMonthTransactionResponse;
import com.incomm.chstypes.Header;
import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.outbound.ingo.service.IngoServiceClient;

import com.incomm.wmp.gprServices.response.BaseResponse;



import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/gprServices")
public class GetTokenController {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String ingoUrl;

    @Autowired
     private IngoServiceClient ingoServiceClient;



    /*@RequestMapping(value = "/getIngoSessionToken", method = RequestMethod.GET)
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

    }*/





}
