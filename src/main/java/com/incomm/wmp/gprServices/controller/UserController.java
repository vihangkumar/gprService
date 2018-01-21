package com.incomm.wmp.gprServices.controller;

import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.request.LoginRequest;
import com.incomm.wmp.gprServices.response.BaseResponse;
import com.incomm.wmp.gprServices.response.CardDetailsResponse;
import com.incomm.wmp.gprServices.response.LoginResponse;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import com.incomm.wmp.gprServices.service.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
//import org.auth.cardmanagement.Header;
//import org.auth.cardmanagement.ValidateAndAuthenticateRevisedRequest;
//import org.auth.cardmanagement.ValidateAndAuthenticateRevisedResponse;

import com.incomm.chstypes.*;




import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.incomm.wmp.gprServices.logging.MDCInterceptor.SESSION_ID;


@RestController
@RequestMapping("/gprServices")
@ConfigurationProperties(prefix = "gprServices")
public class UserController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String chsEndpointUrl;

    private List<Integer> bin;
    @Autowired
    @Qualifier("chsPort")
    private WebServiceTemplate templateCHSPort;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    StringEncryptor stringEncryptor;

    @Autowired
    VmsServiceUtil vmsServiceUtil;

    @Autowired
    KountService KountService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = LoginResponse.class)})
    public ResponseEntity<Object> loginRevised(@RequestBody LoginRequest loginRequest, @RequestHeader(value = "x-src-app-name") String srcAppId,HttpServletRequest request) {

        ValidateAndAuthenticateRevisedResponse vaaResp;

        try {
            logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.LOGIN);
            String kountSessionId = httpServletRequest.getHeader("x-kount-session-id");

           /* Header chsHeader = KountService.getCHSKountHeader(loginRequest.getUsername(),srcAppId,request,kountSessionId);*/

            ValidateAndAuthenticateRevisedRequest vaaReq = new ValidateAndAuthenticateRevisedRequest();
            vaaReq.setHeader(vmsServiceUtil.getCHSHeader(loginRequest.getUsername().toLowerCase().trim(), srcAppId, request));

            vaaReq.setUserName(loginRequest.getUsername().toLowerCase().trim());
            vaaReq.setPassword(loginRequest.getPassword());

            logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.LOGIN);
            vaaResp = (ValidateAndAuthenticateRevisedResponse) templateCHSPort.marshalSendAndReceive(vaaReq);
            logger.info("message = Received response from outbound, userEvent=" + UserEvents.LOGIN);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(vaaResp.getResponse().getRespCode());
            baseResponse.setRespMessage(vaaResp.getResponse().getRespMessage());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setResponse(baseResponse);
            loginResponse.setGprCardStatus(vaaResp.getGprCardStatus());
            if(vaaResp.getResponse().getRespCode().equals("00")){
                logger.info("message = processing the login response, userEvent"+UserEvents.LOGIN);
                processResponse(vaaResp, vaaReq);
                return new ResponseEntity<>(loginResponse,HttpStatus.OK);
            }
            else {
                baseResponse.setRespCode(Constants.ERR_INVALID_CREDENTIALS);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
            }

        } catch (javax.xml.ws.WebServiceException e) {
            logger.error("message = WebServiceException Occurred while calling VMS WS, userEvent=" + UserEvents.LOGIN);
            logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.LOGIN);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_CHS);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        } catch (IOException e) {
            logger.error("message = IO Exception Occurred while calling CHS, userEvent=" + UserEvents.LOGIN);
            logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.LOGIN);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } catch (Exception e) {
            logger.error("message = Unexpected Exception Occurred while calling CHS, userEvent=" + UserEvents.LOGIN);
            logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.LOGIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<Object> refresh(@RequestHeader(value = "x-auth-token") String sessionId) {
        logger.info("message = Refresh endpoint. Retrieving the session id, userEvent" + UserEvents.REFRESH);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.REFRESH);
        if (newSession != null) {
            logger.debug("message = Refreshing the session, userEvent" + UserEvents.REFRESH);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } else {
            logger.error("message = Session expired, userEvent" + UserEvents.REFRESH);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Object> logout(@RequestHeader(value = "x-auth-token") String sessionId) {
        logger.info("message = logout endpoint. Retrieving the session id, userEvent" + UserEvents.LOGOUT);
        HttpSession newSession = httpServletRequest.getSession(false);

        if (newSession != null) {
            logger.debug("message = Invalidating the session, userEvent" + UserEvents.LOGOUT);
            newSession.invalidate();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } else {
            logger.error("message = Session expired, userEvent" + UserEvents.LOGOUT);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public void processResponse(ValidateAndAuthenticateRevisedResponse vaaR, ValidateAndAuthenticateRevisedRequest loginReq) {

        HttpSession newSession = httpServletRequest.getSession(true);
        logger.info("Adding customer id, card and user details to the session");

        newSession.setAttribute("customerId", stringEncryptor.encrypt(vaaR.getCustomerId()));

        CardDetailsResponse cardDetailsResponse = new CardDetailsResponse();
        BeanUtils.copyProperties(vaaR,cardDetailsResponse);

        newSession.setAttribute("cardDetails",stringEncryptor.encrypt(cardDetailsResponse.toString()));
        newSession.setAttribute("firstname",stringEncryptor.encrypt(vaaR.getFirstName()));

        newSession.setAttribute("username",stringEncryptor.encrypt(loginReq.getUserName()));
        newSession.setAttribute("srcAppId",loginReq.getHeader().getSrcAppId());

        logger.info("message = Adding the session traceid to the session which is used to trace the calls after the login");
        newSession.setAttribute("session-TraceId", MDC.get(SESSION_ID));
    }

    public String getChsEndpointUrl() {
        return chsEndpointUrl;
    }

    public void setChsEndpointUrl(String chsEndpointUrl) {
        this.chsEndpointUrl = chsEndpointUrl;
    }

    public List<Integer> getBin() {
        return bin;
    }

    public void setBin(List<Integer> bin) {
        this.bin = bin;
    }
}
