package com.incomm.wmp.gprServices.controller;

//import com.incomm.gprServices.config.SecurityQuestionsConfig;

import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.request.*;
import com.incomm.wmp.gprServices.response.BaseResponse;
import com.incomm.wmp.gprServices.response.VerifyRegisteredCardPinResponse;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
//import org.auth.cardmanagement.*;

import com.incomm.chstypes.*;

import org.jasypt.encryption.StringEncryptor;
import org.omg.CORBA.portable.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

//import com.incomm.gprServices.config.SecurityQuestionsConfig;

@RestController
@RequestMapping("/gprServices")
@ConfigurationProperties(prefix = "gprServices")
public class UserAccountCreationController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    HttpServletRequest httpServletRequest;

    private static final String SITE_SECRET = "6LdyEDMUAAAAAAw7Ufqa7tKh4e2VXdTJvoeUUfc3";
    private static final String SECRET_PARAM = "secret";
    private static final String RESPONSE_PARAM = "response";
    private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";



   /* @Autowired
    SecurityQuestionsConfig securityQuestionsConfig;*/

    @Autowired
    VmsServiceUtil vmsServiceUtil;
    @Autowired
    StringEncryptor stringEncryptor;
    private String userManagementEndpointUrl;
    @Autowired
    @Qualifier("chsPort")
    private WebServiceTemplate templateCHSPort;
    @Autowired
    @Qualifier("usermanagementPort")
    private WebServiceTemplate templateUsermanagementPort;



    Map<String,String> securityquestions1= new HashMap<String, String>();
    Map<String,String> securityquestions2= new HashMap<String, String>();
    Map<String,String> securityquestions3= new HashMap<String, String>();

    public Map<String, String> getSecurityquestions1() {
        return securityquestions1;
    }

    public void setSecurityquestions1(Map<String, String> securityquestions1) {
        this.securityquestions1 = securityquestions1;
    }

    public Map<String, String> getSecurityquestions2() {
        return securityquestions2;
    }

    public void setSecurityquestions2(Map<String, String> securityquestions2) {
        this.securityquestions2 = securityquestions2;
    }

    public Map<String, String> getSecurityquestions3() {
        return securityquestions3;
    }

    public void setSecurityquestions3(Map<String, String> securityquestions3) {
        this.securityquestions3 = securityquestions3;
    }

    public String getUserManagementEndpointUrl() {
        return userManagementEndpointUrl;
    }

    public void setUserManagementEndpointUrl(String userManagementEndpointUrl) {
        this.userManagementEndpointUrl = userManagementEndpointUrl;
    }



    /**
     * this method is for creating a UserAccount
     *
     * @param userAccountInfo
     * @return
     */

    @RequestMapping(value = "/createUserAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = SaveUserCredentialsResponse.class)
    })
    public ResponseEntity<Object> createUserAccount(@RequestHeader(value = "x-src-app-name") String srcAppId, @RequestParam(value="customerId") String customerId, @RequestBody UserAccountInfoReq userAccountInfo, HttpServletRequest request) {


        logger.info("message = entered into createUserAccount, userEvent" + UserEvents.CREATEUSERACCOUNT);
        Map<String, String> securityQuestionsFirstMap =  getSecurityquestions1();
        Map<String, String> securityQuestionsSecondMap = getSecurityquestions2();
        Map<String, String> securityQuestionsThirdMap = getSecurityquestions3();


        try {

         /*   logger.info("message = doing Google recapcha'server side validation"+ UserEvents.CREATEUSERACCOUNT);
            JSONObject jsonObject = performRecaptchaSiteVerify(userAccountInfo.getRequest());
            boolean success = jsonObject.getBoolean("success");
            if(success) {*/


                logger.debug("message = Setting header to request object, userEvent=" + UserEvents.CREATEUSERACCOUNT);
                //getting header from vmsServiceUtil
                Header chsHeader = vmsServiceUtil.getCHSHeader(userAccountInfo.getUsername(), srcAppId, request);
                SaveUserCredentialsRequest createUserAccountRequest = new SaveUserCredentialsRequest();
                createUserAccountRequest.setHeader(chsHeader);

                createUserAccountRequest.setCustomerId(customerId);
                createUserAccountRequest.setUserName(userAccountInfo.getUsername().trim().toLowerCase());
                createUserAccountRequest.setPassword(userAccountInfo.getPassword());

                //createUserAccountRequest.setSecurityQuestionOne(userAccountInfo.getSecurityQuestionOne());
                if (securityQuestionsFirstMap.containsValue(userAccountInfo.getSecurityQuestionOne())) {
                    createUserAccountRequest.setSecurityQuestionOne(userAccountInfo.getSecurityQuestionOne());
                    createUserAccountRequest.setSecurityAnswerOne(userAccountInfo.getSecurityAnswerOne().toLowerCase());
                }
                if (securityQuestionsSecondMap.containsValue(userAccountInfo.getSecurityQuestionTwo())) {
                    createUserAccountRequest.setSecurityQuestionTwo(userAccountInfo.getSecurityQuestionTwo());
                    createUserAccountRequest.setSecurityAnswerTwo(userAccountInfo.getSecurityAnswerTwo().toLowerCase());
                }
                if (securityQuestionsThirdMap.containsValue(userAccountInfo.getSecurityQuestionThree())) {
                    createUserAccountRequest.setSecurityQuestionThree(userAccountInfo.getSecurityQuestionThree());
                    createUserAccountRequest.setSecurityAnswerThree(userAccountInfo.getSecurityAnswerThree().toLowerCase());
                }
                SaveUserCredentialsResponse createUserAccountResponse = (SaveUserCredentialsResponse) templateUsermanagementPort.marshalSendAndReceive(createUserAccountRequest);

                if (createUserAccountResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = CreateUserAccount Response Successfully");
                    return new ResponseEntity<>(createUserAccountResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.CREATEUSERACCOUNT);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(createUserAccountResponse.getResponse(), baseResponse);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
          /*  }else{
                logger.error("message = google Recapcha validation failed, userEvent=" + UserEvents.CREATEUSERACCOUNT);
                // BaseResponse baseResponse = new BaseResponse();
                // BeanUtils.copyProperties(createUserAccountResponse.getResponse(), baseResponse);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }*/

        } catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.CREATEUSERACCOUNT);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.CREATEUSERACCOUNT);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = Session has no content, userEvent=" + UserEvents.CREATEUSERACCOUNT);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }catch (NullPointerException e) {
            logger.error("message = Session has no content, userEvent=" + UserEvents.CREATEUSERACCOUNT);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.CREATEUSERACCOUNT);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.LOGIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

/*
    private JSONObject performRecaptchaSiteVerify(String recaptchaResponseToken)
            throws IOException {
        URL url = new URL(SITE_VERIFY_URL);
        StringBuilder postData = new StringBuilder();
        addParam(postData, SECRET_PARAM, SITE_SECRET);
        addParam(postData, RESPONSE_PARAM, recaptchaResponseToken);
        return postAndParseJSON(url, postData.toString());
    }

    private StringBuilder addParam(
            StringBuilder postData, String param, String value)
            throws UnsupportedEncodingException {
        if (postData.length() != 0) {
            postData.append("&");
        }
        return postData.append(
                String.format("%s=%s",
                        URLEncoder.encode(param, StandardCharsets.UTF_8.displayName()),
                        URLEncoder.encode(value, StandardCharsets.UTF_8.displayName())));
    }

    private JSONObject postAndParseJSON(URL url, String postData) throws IOException {
        try{
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty(
                    "charset", StandardCharsets.UTF_8.displayName());
            urlConnection.setRequestProperty(
                    "Content-Length", Integer.toString(postData.length()));
            urlConnection.setUseCaches(false);
            urlConnection.getOutputStream()
                    .write(postData.getBytes(StandardCharsets.UTF_8));
            JSONTokener jsonTokener = new JSONTokener(urlConnection.getInputStream());
            return new JSONObject(jsonTokener);

        } catch (JSONException e) {
            logger.error("message = JSON Exception Occurred while calling google Recapcha, userEvent=" + UserEvents.RESETTINGPASSWORD);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e   + " userEvent " + UserEvents.RESETTINGPASSWORD);
            //needs to be change
            return null;
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling google Recapcha, userEvent=" + UserEvents.RESETTINGPASSWORD);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e   + " userEvent " + UserEvents.RESETTINGPASSWORD);
            //needs to be change
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
    }
*/



    /**
     * this method for reseting Password
     *
     * @param
     * @param resetPasswordReq
     * @return
     */
  /*  @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<Object> resetPassword(@RequestHeader(value = "x-src-app-name") String srcAppId, @RequestParam(value="username") String username, @RequestBody ResetPasswordReq resetPasswordReq, HttpServletRequest request) throws UnknownHostException {

            try {
                logger.debug("message = Setting header to request object, userEvent=" + UserEvents.RESETPASSWORD);

                //Build chsHeader
                Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);
                //Creating a new object for the cardLostReqest to set the header and customerId.
                SetNewPasswordRequest pwdRequest = new SetNewPasswordRequest();
                pwdRequest.setHeader(chsHeader);
                pwdRequest.setUsername(username);
                pwdRequest.setPassword(resetPasswordReq.getNewPassword());
                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.RESETPASSWORD);
                SetNewPasswordResponse setNewPasswordResponse = (SetNewPasswordResponse) templateUsermanagementPort.marshalSendAndReceive(pwdRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.RESETPASSWORD);
                //writing the response to the baseResponse
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(setNewPasswordResponse.getResponse().getRespCode());
                baseResponse.setRespMessage(setNewPasswordResponse.getResponse().getRespMessage());
                if (setNewPasswordResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.RESETPASSWORD);
                    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.RESETPASSWORD);
                    BeanUtils.copyProperties(setNewPasswordResponse.getResponse(), baseResponse);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }

            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.RESETPASSWORD);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.RESETPASSWORD);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.RESETPASSWORD);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.RESETPASSWORD);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }*/

    /**
     *
     *this metod makes two calls for forget password (validates security questions and resets passwords)
     *
     * @param username
     * @param srcAppId
     * @param resettingPasswordReq
     * @param request
     * @return
     * @throws UnknownException
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<Object> resettingPassword(@RequestHeader(value = "x-src-app-name") String srcAppId, @RequestBody ResettingPasswordReq resettingPasswordReq, HttpServletRequest request) throws UnknownException{
        logger.info("message = verify resettingPassword, userEvent" + UserEvents.RESETTINGPASSWORD);
        BaseResponse baseResponse = new BaseResponse();
        try{
            logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.RESETTINGPASSWORD);
            Header chsHeader = vmsServiceUtil.getCHSHeader(resettingPasswordReq.getUsername(), srcAppId, request);
            RecoverPasswordValidateSecurityQuestionsRequest rpvsqreq = new RecoverPasswordValidateSecurityQuestionsRequest();
            rpvsqreq.setHeader(chsHeader);
            rpvsqreq.setUsername(resettingPasswordReq.getUsername());
            rpvsqreq.setSecurityQuestionOne(resettingPasswordReq.getSecurityQuestion());
            rpvsqreq.setSecurityAnswerOne(resettingPasswordReq.getSecurityAnswer().toLowerCase());
            RecoverPasswordValidateSecurityQuestionsResponse rpvsqresp = (RecoverPasswordValidateSecurityQuestionsResponse) templateUsermanagementPort.marshalSendAndReceive(rpvsqreq);
            /*//writing the response to the baseResponse
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(rpvsqresp.getResponse().getRespCode());
            baseResponse.setRespMessage(rpvsqresp.getResponse().getRespMessage());*/

            if (rpvsqresp.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                //Creating a new object for the cardLostReqest to set the header and customerId.
                SetNewPasswordRequest pwdRequest = new SetNewPasswordRequest();
                pwdRequest.setHeader(chsHeader);
                pwdRequest.setUsername(resettingPasswordReq.getUsername());
                pwdRequest.setPassword(resettingPasswordReq.getNewPassword());
                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.RESETTINGPASSWORD);
                SetNewPasswordResponse setNewPasswordResponse = (SetNewPasswordResponse) templateUsermanagementPort.marshalSendAndReceive(pwdRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.RESETTINGPASSWORD);
                //writing the response to the baseResponse
                baseResponse.setRespCode(setNewPasswordResponse.getResponse().getRespCode());
                baseResponse.setRespMessage(setNewPasswordResponse.getResponse().getRespMessage());
                if (setNewPasswordResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.RESETTINGPASSWORD);
                    baseResponse.setRespCode(Constants.SUCCESS_RESET_PASSWORD);
                    return new ResponseEntity<>(baseResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.RESETTINGPASSWORD);
                    BeanUtils.copyProperties(setNewPasswordResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_RESET_PASSWORD);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } else {
                logger.error("message = Error occured wrong security Questions, userEvent=" + UserEvents.RESETTINGPASSWORD);
                BeanUtils.copyProperties(rpvsqresp.getResponse(), rpvsqresp);
                baseResponse.setRespCode(rpvsqresp.getResponse().getRespCode());
                baseResponse.setRespMessage(rpvsqresp.getResponse().getRespMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }
        }catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.RESETTINGPASSWORD);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.RESETTINGPASSWORD);
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.RESETTINGPASSWORD);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.RESETTINGPASSWORD);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.RESETTINGPASSWORD);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.RESETTINGPASSWORD);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    /**
     * this method is for verifying Card Registration
     *
     * @param verifyCardRegistrationReq
     * @return
     */

    @RequestMapping(value = "/verifyCardRegistrationStatus", method = RequestMethod.POST)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = GetRegistartionStatusResponse.class)
    })
    public ResponseEntity<Object> verifyCardRegistrationStatus(@RequestBody VerifyCardRegistrationReq verifyCardRegistrationReq, @RequestHeader(value = "x-src-app-name") String srcAppId, HttpServletRequest request) throws UnknownHostException {
        logger.info("message = entered into verifyCardRegistrationStatus" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
        try {
            logger.debug("message = Setting header to request object, userEvent=" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            // Verifying the CardNumber and CVV.
            if (StringUtils.isNotBlank(verifyCardRegistrationReq.getCardNumber()) &&
                    StringUtils.isNotBlank(verifyCardRegistrationReq.getCvv())) {
                //Build chsHeader
                Header chsHeader = vmsServiceUtil.getCHSHeader(Constants.USERNAME, srcAppId, request);
                GetRegistartionStatusRequest registartionStatusRequest = new GetRegistartionStatusRequest();
                registartionStatusRequest.setHeader(chsHeader);
                registartionStatusRequest.setCardNumber(verifyCardRegistrationReq.getCardNumber());
                logger.info("card: " + verifyCardRegistrationReq.getCardNumber() + "CVV: " + verifyCardRegistrationReq.getCvv());
                registartionStatusRequest.setCvv(verifyCardRegistrationReq.getCvv());
                logger.info("Message= Adding card info to the Session, userEvent="+ UserEvents.VERIFYCARDREGISTRATIONSTATUS);

                GetRegistartionStatusResponse registartionStatusResponse = (GetRegistartionStatusResponse) templateUsermanagementPort.marshalSendAndReceive(registartionStatusRequest);
                if (registartionStatusResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = processing the verifyregistration status response, userEvent" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
                    //processPreloginRequest(verifyCardRegistrationReq, srcAppId);
                    return new ResponseEntity<>(registartionStatusResponse, HttpStatus.OK);
                } else {
                    logger.error("message = Error occured, userEvent=" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(registartionStatusResponse.getResponse(), baseResponse);
                    baseResponse.setRespCode(Constants.ERR_VERIFY_CARD_STATUS);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VERIFYCARDREGISTRATIONSTATUS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



/*    *//**
     * This method is for creating session for prelogged in calls
     * @param verifyCardRegistrationReq
     * @param srcAppId
     *//*
    public void processPreloginRequest(VerifyCardRegistrationReq verifyCardRegistrationReq, String srcAppId) {
        logger.info("Message = Creating session for SignUp calls");
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("Adding  cardNumber, CVV, SrcAppId to the session");
        newSession.setAttribute("cardNumber", stringEncryptor.encrypt(verifyCardRegistrationReq.getCardNumber()));
        newSession.setAttribute("cvv", stringEncryptor.encrypt(verifyCardRegistrationReq.getCvv()));
        newSession.setAttribute("srcAppId",srcAppId);
        logger.info("message = Adding the session traceId to the session which is used to trace the calls after the login");
        newSession.setAttribute("session-TraceId", MDC.get(SESSION_ID));
    }*/

    /**
     * this is method for getSecurityQuestions
     * @param sessionId
     * @param request
     * @return
     * @throws UnknownHostException
     */
    @RequestMapping(value = "/getSecurityQuestions", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = RecoverPasswordSecurityQuestionsResponse.class)
    })
    public ResponseEntity<Object> getSecurityQuestions(@RequestParam(value = "username") String username, @RequestHeader(value = "x-src-app-name") String srcAppId, HttpServletRequest request) throws UnknownHostException {

        logger.info("message = verify getSecurityQuestions. Retrieving the session id, userEvent" + UserEvents.GETSECURITYQUESTIONS);

        try {
            logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
            Header chsHeader = vmsServiceUtil.getCHSHeader(username, srcAppId, request);
            RecoverPasswordSecurityQuestionsRequest recoverPwdSecQuestionReq = new RecoverPasswordSecurityQuestionsRequest();
            recoverPwdSecQuestionReq.setHeader(chsHeader);
            recoverPwdSecQuestionReq.setUsername(username);
            RecoverPasswordSecurityQuestionsResponse recoverPasswordSecurityQuestionsResponse = (RecoverPasswordSecurityQuestionsResponse) templateUsermanagementPort.marshalSendAndReceive(recoverPwdSecQuestionReq);

            if (recoverPasswordSecurityQuestionsResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
                // processForgetPasswordRequest(username,srcAppId);
                return new ResponseEntity<>(recoverPasswordSecurityQuestionsResponse, HttpStatus.OK);
            } else {
                logger.error("message = Error occured, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
                BaseResponse baseResponse = new BaseResponse();
                BeanUtils.copyProperties(recoverPasswordSecurityQuestionsResponse.getResponse(), baseResponse);
                baseResponse.setRespCode(Constants.ERR_GET_SECURITY_QUESTIONS);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }
        } catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.GETSECURITYQUESTIONS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.GETSECURITYQUESTIONS);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.GETSECURITYQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.GETSECURITYQUESTIONS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

 /*   public void processForgetPasswordRequest(String username, String srcAppId){
        logger.info("Message = Creating session for forgot password calls");
        HttpSession newSession = httpServletRequest.getSession(true);
        logger.info("Adding username, SrcAppId to the session");
        newSession.setAttribute("username",  stringEncryptor.encrypt(username));
        newSession.setAttribute("srcAppId",srcAppId);
        logger.info("message = Adding the session traceId to the session which is used to trace the calls after the login");
        newSession.setAttribute("session-TraceId", MDC.get(SESSION_ID));
    }*/



    /**
     * This is method for validating Security Questfion
     *
     * @param verifyCardRegistrationReq
     * @return
     */
    @RequestMapping(value = "/validateSecurityQuestion", method = RequestMethod.POST)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<BaseResponse> validateSecurityQuestion(@RequestHeader(value = "x-src-app-name") String srcAppId,
                                                                 @RequestBody ValidateSecurityQuestionReq verifyCardRegistrationReq, HttpServletRequest request) throws UnknownHostException {

        try {
            logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.VALIDATEQUESTIONS);


            logger.debug("message = Setting header to request object, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            //getting header from vmsServiceUtil
            Header chsHeader = vmsServiceUtil.getCHSHeader(verifyCardRegistrationReq.getUsername(), srcAppId, request);

            RecoverPasswordValidateSecurityQuestionsRequest rpvsqreq = new RecoverPasswordValidateSecurityQuestionsRequest();
            rpvsqreq.setHeader(chsHeader);
            rpvsqreq.setUsername(verifyCardRegistrationReq.getUsername());
            rpvsqreq.setSecurityQuestionOne(verifyCardRegistrationReq.getSecurityQuestion());
            rpvsqreq.setSecurityAnswerOne(verifyCardRegistrationReq.getSecurityAnswer().toLowerCase());
            RecoverPasswordValidateSecurityQuestionsResponse rpvsqresp = (RecoverPasswordValidateSecurityQuestionsResponse) templateUsermanagementPort.marshalSendAndReceive(rpvsqreq);

            //RecoverPasswordSecurityQuestionsResponse rpvsqresp = (RecoverPasswordSecurityQuestionsResponse) templateUsermanagementPort.marshalSendAndReceive(rpvsqreq);
            //writing the response to the baseResponse
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(rpvsqresp.getResponse().getRespCode());
            baseResponse.setRespMessage(rpvsqresp.getResponse().getRespMessage());

            if (rpvsqresp.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.VALIDATEQUESTIONS);
                return new ResponseEntity<>(baseResponse, HttpStatus.OK);
            } else {
                logger.error("message = Error occured, userEvent=" + UserEvents.VALIDATEQUESTIONS);
                //BeanUtils.copyProperties(rpvsqresp.getResponse(), baseResponse);
                baseResponse.setRespCode(Constants.ERR_VALIDATE_SECURITY_QUESTIONS);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }

        } catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @RequestMapping(value = "/verifyRegisteredCardPin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = VerifyRegisteredCardPinResponse.class)
    })
    public ResponseEntity<Object> verifyRegisteredCardPin(@RequestBody VerifyRegisteredCardPinRequest
                                                                  cardPinRequest, @RequestHeader(value = "x-src-app-name") String srcAppId, HttpServletRequest request) throws
            UnknownHostException {
        try {
            logger.info("message = verify registered card pin endpoint, userEvent" + UserEvents.VERIFYREGISTEREDCARDPIN);

            logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.VERIFYREGISTEREDCARDPIN);
            com.incomm.chstypes.ValidateCardUsingCardPinAndCVVRequest validateCardUsingCardPinAndCVVRequest = new com.incomm.chstypes.ValidateCardUsingCardPinAndCVVRequest();
            validateCardUsingCardPinAndCVVRequest.setHeader(vmsServiceUtil.getCHSHeader(Constants.USERNAME, srcAppId, request));
            validateCardUsingCardPinAndCVVRequest.setCardNumber(cardPinRequest.getCardNumber());
            validateCardUsingCardPinAndCVVRequest.setPin(cardPinRequest.getPin());
            validateCardUsingCardPinAndCVVRequest.setCvv(cardPinRequest.getCvv());

            logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.VERIFYREGISTEREDCARDPIN);
            ValidateCardUsingCardPinAndCVVResponse validateCardUsingCardPinAndCVVResponse = (ValidateCardUsingCardPinAndCVVResponse) templateCHSPort.marshalSendAndReceive(validateCardUsingCardPinAndCVVRequest);
            logger.info("message = Received response from outbound, userEvent=" + UserEvents.VERIFYREGISTEREDCARDPIN);

            if (validateCardUsingCardPinAndCVVResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                logger.info("message = populating the success response from outbound, userEvent=" + UserEvents.VERIFYREGISTEREDCARDPIN);
                VerifyRegisteredCardPinResponse verifyRegisteredCardPinResponse = new VerifyRegisteredCardPinResponse();
                //newSession.setAttribute("customerId", stringEncryptor.encrypt(validateCardUsingCardPinAndCVVResponse.getCustomerId()));
                BeanUtils.copyProperties(validateCardUsingCardPinAndCVVResponse, verifyRegisteredCardPinResponse);
                return new ResponseEntity<>(verifyRegisteredCardPinResponse, HttpStatus.OK);
            } else {
                logger.error("message = Error occured, userEvent=" + UserEvents.VERIFYREGISTEREDCARDPIN);
                BaseResponse baseResponse = new BaseResponse();
                BeanUtils.copyProperties(validateCardUsingCardPinAndCVVResponse.getResponse(), baseResponse);
                baseResponse.setRespCode(Constants.ERR_VALIDATE_REGISTERED_CARD_PIN);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }
        }
        catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling VMS WS, userEvent=" + UserEvents.VALIDATEQUESTIONS);
            logger.error("message = URL " + userManagementEndpointUrl + " error " + e + " userEvent " + UserEvents.VALIDATEQUESTIONS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



    }


}




