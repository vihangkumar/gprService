package com.incomm.wmp.gprServices.controller;


import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.request.CardActivationReq;
import com.incomm.wmp.gprServices.response.BaseResponse;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
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


@RestController
@RequestMapping("/gprServices")
public class CardIssuanceController {


    protected Logger logger = LoggerFactory.getLogger(getClass());



    private String cardIssueEndpointUrl;


    @Autowired
    @Qualifier("chsPort")
    private WebServiceTemplate templateCHSPort;

    @Autowired
    @Qualifier("cardIssuerPort")
    private WebServiceTemplate templateCardIssuerPort;

    @Autowired
    HttpServletRequest httpServletRequest;


    @Autowired
    StringEncryptor stringEncryptor;

    @Autowired
    VmsServiceUtil vmsServiceUtil;

    public String getCardIssueEndpointUrl() {
        return cardIssueEndpointUrl;
    }

    public void setCardIssueEndpointUrl(String cardIssueEndpointUrl) {
        this.cardIssueEndpointUrl = cardIssueEndpointUrl;
    }

    @RequestMapping(value = "/cardLost", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<BaseResponse> cardLost(@RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = verify cardLost. Retrieving the session id, userEvent" + UserEvents.CARDLOST);
        if (newSession != null) {
            try {
                logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.CARDLOST);

                // In this scenario i don't need to verify the username.
                if (newSession.getAttribute("customerId") != null &&
                        newSession.getAttribute("username") != null &&
                        StringUtils.isNotEmpty(newSession.getAttribute("username").toString()) &&
                        StringUtils.isNotEmpty(newSession.getAttribute("customerId").toString())) {

                    //Decrypting the values in the session
                    String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                    String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                    String srcAppId = newSession.getAttribute("srcAppId").toString();

                    logger.debug("message = Setting header to request object, userEvent=" + UserEvents.CARDLOST);
                    //getting header from vmsServiceUtil
                    Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);

                    //Creating a new object for the cardLostReqest to set the header and customerId.
                    CardLostRequest cardLostRequest = new CardLostRequest();
                    cardLostRequest.setHeader(chsHeader);
                    cardLostRequest.setCustomerId(customerId);

                    logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.CARDLOST);
                    CardLostResponse cardLostResponse = (CardLostResponse) templateCardIssuerPort.marshalSendAndReceive(cardLostRequest);
                    logger.info("message = Received response from outbound, userEvent=" + UserEvents.CARDLOST);
                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setRespCode(cardLostResponse.getResponse().getRespCode());
                    baseResponse.setRespMessage(cardLostResponse.getResponse().getRespMessage());

                    if (cardLostResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                        logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.CARDLOST);
                        baseResponse.setRespCode(Constants.SUCCESS_CARD_LOST);
                        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
                    } else {
                        logger.error("message = Error occured, userEvent=" + UserEvents.CARDLOST);
                        //BeanUtils.copyProperties(cardLostResponse.getResponse(), baseResponse);
                        baseResponse.setRespCode(Constants.ERR_CARD_LOST);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                    }
                } else {
                    logger.error("message = Session has no content, userEvent=" + UserEvents.CARDLOST);
                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(baseResponse);
                }

            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.CARDLOST);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDLOST);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.CARDLOST);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDLOST);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.CARDLOST);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDLOST);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.CARDLOST);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }

    }


    @RequestMapping(value = "/cardFound", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<Object> cardFound(@RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = verify cardFound. Retrieving the session id, userEvent" + UserEvents.CARDFOUND);

        if (newSession != null) {
            try {
                logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.CARDFOUND);
                // Verifying the session
                // In this scenario i don't need to verify the username.
                if (newSession.getAttribute("customerId") != null &&
                        newSession.getAttribute("username") != null &&
                        StringUtils.isNotEmpty(newSession.getAttribute("username").toString()) &&
                        StringUtils.isNotEmpty(newSession.getAttribute("customerId").toString())) {

                    logger.debug("message  =  session Found successfully !!! " + sessionId);
                    String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                    String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                    String srcAppId = newSession.getAttribute("srcAppId").toString();
                    logger.debug("message = Setting header to request object, userEvent=" + UserEvents.CARDFOUND);

                    //Build chsHeader
                    Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);

                    //Creating a new object for the cardLostReqest to set the header and customerId.
                    CardFoundRequest cardFoundRequest = new CardFoundRequest();
                    cardFoundRequest.setHeader(chsHeader);
                    cardFoundRequest.setCustomerId(customerId);
                    logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.CARDFOUND);
                    CardFoundResponse cardFoundResponse = (CardFoundResponse) templateCardIssuerPort.marshalSendAndReceive(cardFoundRequest);
                    logger.info("message = Received response from outbound, userEvent=" + UserEvents.CARDFOUND);
                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setRespCode(cardFoundResponse.getResponse().getRespCode());
                    baseResponse.setRespMessage(cardFoundResponse.getResponse().getRespMessage());


                    if (cardFoundResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                        logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.CARDFOUND);
                        baseResponse.setRespCode(Constants.SUCCESS_CARD_FOUND);
                        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
                    } else {
                        logger.error("message = Error occured, userEvent=" + UserEvents.CARDFOUND);
                        BeanUtils.copyProperties(cardFoundResponse.getResponse(), baseResponse);
                        baseResponse.setRespCode(Constants.ERR_CARD_FOUND);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }

            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.CARDFOUND);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDFOUND);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.CARDFOUND);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDFOUND);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.CARDFOUND);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.CARDFOUND);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            }
        }else {
            logger.error("message = Session expired, userEvent=" + UserEvents.CARDFOUND);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }


    @RequestMapping(value = "/activateCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = CardLostResponse.class)
    })
    public ResponseEntity<Object> activateCard(@RequestHeader(value = "x-auth-token") String sessionId,
                                               @RequestBody CardActivationReq cardActivationReq, HttpServletRequest request) throws UnknownHostException {
        logger.info("message = Activate Card endpoint. Retrieving the session id, userEvent" + UserEvents.ACTIVATECARD);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.UPDATEPIN);
        // Verifying the session
        if (newSession != null) {
            try {
                if (newSession.getAttribute("customerId") != null &&
                        newSession.getAttribute("username") != null &&
                        StringUtils.isNotEmpty(newSession.getAttribute("username").toString()) &&
                        StringUtils.isNotEmpty(newSession.getAttribute("customerId").toString())) {
                    logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.ACTIVATECARD);
                    String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                    String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                    String srcAppId = newSession.getAttribute("srcAppId").toString();

                    logger.debug("message = Setting header to request object, userEvent=" + UserEvents.ACTIVATECARD);

                    Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);
                    CardActivationRequest cardActRequest = new CardActivationRequest();
                    cardActRequest.setHeader(chsHeader);
                    cardActRequest.setCustomerId(customerId);
                    cardActRequest.setCardNumber(cardActivationReq.getCardNumber());
                    cardActRequest.setPin(cardActivationReq.getPin());
                    cardActRequest.setCvv2(cardActivationReq.getSecurityCode());
                    CardActivationResponse cardActivationResponse = (CardActivationResponse) templateCardIssuerPort.marshalSendAndReceive(cardActRequest);
                    //writing the response code and message to baseresponse object
                    if (cardActivationResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                        logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.ACTIVATECARD);
                        return new ResponseEntity<>(cardActivationResponse, HttpStatus.OK);
                    } else {
                        logger.info("message = ActivateCard Response Failed");
                        BaseResponse baseResponse = new BaseResponse();
                        baseResponse.setRespCode(Constants.ERR_ACTIVATE_CARD);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                    }

                } else {
                    logger.error("message = Session has no content, userEvent=" + UserEvents.ACTIVATECARD);
                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(baseResponse);
                }

        } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.ACTIVATECARD);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.ACTIVATECARD);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.ACTIVATECARD);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.ACTIVATECARD);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.ACTIVATECARD);
                logger.error("message = URL " + cardIssueEndpointUrl + " error " + e + " userEvent " + UserEvents.ACTIVATECARD);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }
        else {
            logger.error("message = Session expired, userEvent=" + UserEvents.ACTIVATECARD);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }


}
