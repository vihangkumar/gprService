package com.incomm.wmp.gprServices.controller;

import com.incomm.wmp.gprServices.constants.Constants;
import com.incomm.wmp.gprServices.constants.UserEvents;
import com.incomm.wmp.gprServices.request.UpdatePasswordReq;
import com.incomm.wmp.gprServices.request.UpdateProfileRequest;
import com.incomm.wmp.gprServices.response.AddressResponse;
import com.incomm.wmp.gprServices.response.BaseResponse;
import com.incomm.wmp.gprServices.response.UserProfileResponse;
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
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.net.UnknownHostException;


@RestController
@RequestMapping("/gprServices")
public class UserProfileController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String chsEndpointUrl;

    @Autowired
    @Qualifier("chsPort")
    private WebServiceTemplate templateCHSPort;

    @Autowired
    @Qualifier("usermanagementPort")
    private WebServiceTemplate templateUsermanagementPort;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    StringEncryptor stringEncryptor;

    @Autowired
    VmsServiceUtil vmsServiceUtil;

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {  @ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordReq updatePwdReq, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {

        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session for update password, userEvent=" + UserEvents.UPDATEPASSWORD);
        if (newSession != null) {
            try {
                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.UPDATEPASSWORD);
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                Header chsHeader = vmsServiceUtil.getCHSHeader(username,srcAppId,request);
                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.UPDATEPASSWORD);
                com.incomm.chstypes.UpdatePasswordRequest updatePwdRequest = new com.incomm.chstypes.UpdatePasswordRequest();
                updatePwdRequest.setHeader(chsHeader);
                updatePwdRequest.setUsername(username);
                updatePwdRequest.setOldPassword(updatePwdReq.getOldPassword());
                updatePwdRequest.setPassword(updatePwdReq.getPassword());

                logger.info("message = Calling CHS WS for updating password, userEvent=" + UserEvents.UPDATEPASSWORD);
                UpdatePasswordResponse updatePwdResponse = (UpdatePasswordResponse) templateUsermanagementPort.marshalSendAndReceive(updatePwdRequest);
                logger.info("message = Response from CHS WS for, userEvent=" + UserEvents.UPDATEPASSWORD + ":" + updatePwdResponse.getResponse().getRespCode());

                BaseResponse response = new BaseResponse();
                BeanUtils.copyProperties(updatePwdResponse.getResponse(),response);
                if (updatePwdResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = successfully updated the password, userEvent=" + UserEvents.UPDATEPASSWORD);
                    response.setRespCode(Constants.SUCCESS_UPDATE_PASSWORD);
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }else {
                    logger.error("message = Error occured while updating password, userEvent=" + UserEvents.UPDATEPASSWORD);
                    response.setRespCode(Constants.ERR_UPDATE_PASSWORD);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }

            } catch (WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPASSWORD);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPASSWORD);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPASSWORD);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPASSWORD);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            }catch (Exception e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPASSWORD);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPASSWORD);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            logger.error("message = Session expired, userEvent=" + UserEvents.UPDATEPASSWORD);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    @RequestMapping(value = "/retrieveUserProfile", method = RequestMethod.GET)
    @ApiResponses(value = {  @ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = UserProfileResponse.class)
    })
    public ResponseEntity<Object> retrieveUserProfile(@RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException{
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session for Retriving User Profile, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);

        if (newSession != null) {
            try {
                logger.debug("message = Populating the outbound request object, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.debug("message = Setting header to request object, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                Header chsHeader = vmsServiceUtil.getCHSHeader(username,srcAppId,request);
                PersonalInfoInquiryRequest personalInfoRequest = new PersonalInfoInquiryRequest();
                personalInfoRequest.setHeader(chsHeader);
                personalInfoRequest.setCustomerId(customerId);

                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                PersonalInfoInquiryResponse personalInfoResponse = (PersonalInfoInquiryResponse) templateCHSPort.marshalSendAndReceive(personalInfoRequest);
                logger.info("message = Received response from outbound, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);

                if (personalInfoResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = processing the User Profile response to store the email and phone number in session, userEvent"+UserEvents.RETRIEVEUSERPROFILE);
                    processPersonalInfoResponse(personalInfoResponse);
                    UserProfileResponse userProfileResponse = new UserProfileResponse();
                    BeanUtils.copyProperties(personalInfoResponse,userProfileResponse);

                    AddressResponse physicalAddress = new AddressResponse();
                    BeanUtils.copyProperties(personalInfoResponse.getPhysicalAddress(),physicalAddress);
                    userProfileResponse.setPhysicalAddress(physicalAddress);

                    AddressResponse mailingAddress = new AddressResponse();
                    BeanUtils.copyProperties(personalInfoResponse.getMailingAddress(),mailingAddress);
                    userProfileResponse.setMailingAddress(mailingAddress);

                    return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);

                } else {
                    logger.error("message = Error occured while retrieving the user profile, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                    BaseResponse baseResponse = new BaseResponse();
                    BeanUtils.copyProperties(personalInfoResponse.getResponse(),baseResponse);
                    baseResponse.setRespCode(Constants.ERR_RETRIEVE_USER_PROFILE);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
                }

            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.RETRIEVEUSERPROFILE);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS WS, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.RETRIEVEUSERPROFILE);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS WS, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.RETRIEVEUSERPROFILE);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            logger.error("message = Session expired, userEvent=" + UserEvents.RETRIEVEUSERPROFILE);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }

    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {  @ApiResponse(code = 401, message = "UNAUTHORIZED", response = BaseResponse.class),
            @ApiResponse(code = 200, message = "OK", response = BaseResponse.class)
    })
    public ResponseEntity<Object> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest, @RequestHeader(value = "x-auth-token") String sessionId, HttpServletRequest request) throws UnknownHostException {
        logger.info("message = update profile endpoint. Retrieving the session id, userEvent" + UserEvents.UPDATEPROFILE);
        HttpSession newSession = httpServletRequest.getSession(false);
        logger.info("message = validating the session, userEvent" + UserEvents.UPDATEPROFILE);
        if (newSession != null) {
            try {
                logger.info("message = Retrieving values from session, userEvent=" + UserEvents.UPDATEPROFILE);
                String customerId = stringEncryptor.decrypt(newSession.getAttribute("customerId").toString());
                String username = stringEncryptor.decrypt(newSession.getAttribute("username").toString());
                String srcAppId = newSession.getAttribute("srcAppId").toString();

                logger.info("message = Populating the outbound request object, userEvent=" + UserEvents.UPDATEPROFILE);
                UpdatedPersonalInfoRequest updatedPersonalInfoRequest = new UpdatedPersonalInfoRequest();
                updatedPersonalInfoRequest.setCustomerId(customerId);
                updatedPersonalInfoRequest.setHeader(vmsServiceUtil.getCHSHeader(username,srcAppId,request));
                updatedPersonalInfoRequest.setEmailId(updateProfileRequest.getEmailAddress().trim());
                updatedPersonalInfoRequest.setPhoneNumber(updateProfileRequest.getPrimaryPhoneNumber().trim());
                updatedPersonalInfoRequest.setMobileNumber(updateProfileRequest.getSecondaryPhoneNumber().trim());

                Address physicalAddr = new Address();
                BeanUtils.copyProperties(updateProfileRequest.getPhysicalAddress(), physicalAddr);

                updatedPersonalInfoRequest.setPhysicalAddress(physicalAddr);
                Address mailAddress = new Address();
                if (updateProfileRequest.isPhyAddrMatchesMailAddr()) {
                    BeanUtils.copyProperties(updateProfileRequest.getPhysicalAddress(), mailAddress);
                    updatedPersonalInfoRequest.setMailingAddress(mailAddress);
                } else {
                    BeanUtils.copyProperties(updateProfileRequest.getMailAddress(), mailAddress);
                    updatedPersonalInfoRequest.setMailingAddress(mailAddress);
                }
                logger.info("message = Making a call to the outbound, userEvent=" + UserEvents.UPDATEPROFILE);
                UpdatedPersonalInfoResponse updatedPersonalInfoResponse = (UpdatedPersonalInfoResponse) templateCHSPort.marshalSendAndReceive(updatedPersonalInfoRequest);
                logger.info("message = Response from CHS WS for, userEvent=" + UserEvents.UPDATEPROFILE );

                BaseResponse response = new BaseResponse();
                BeanUtils.copyProperties(updatedPersonalInfoResponse.getResponse(),response);
                if (updatedPersonalInfoResponse.getResponse().getRespCode().equals(Constants.SUCCESS_RESP_CODE)) {
                    logger.info("message = successfully updated the profile, userEvent=" + UserEvents.UPDATEPROFILE);
                    response.setRespCode(Constants.SUCCESS_UPDATE_USER_PROFILE);
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }else {
                    logger.error("message = Error occured while updating profile, userEvent=" + UserEvents.UPDATEPROFILE);
                    response.setRespCode(Constants.ERR_UPDATE_USER_PROFILE);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } catch (javax.xml.ws.WebServiceException e) {
                logger.error("message = WebServiceException Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPROFILE);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode(Constants.ERR_CHS);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
            } catch (IOException e) {
                logger.error("message = IO Exception Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPROFILE);
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
            } catch (Exception e) {
                logger.error("message = Unexpected Exception Occurred while calling CHS WS, userEvent=" + UserEvents.UPDATEPROFILE);
                logger.error("message = URL " + chsEndpointUrl + " error " + e + " userEvent " + UserEvents.UPDATEPROFILE);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            logger.error("message = Session expired, userEvent=" + UserEvents.UPDATEPROFILE);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(Constants.ERR_SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        }
    }

    private void processPersonalInfoResponse(PersonalInfoInquiryResponse personalInfoResponse) {
        HttpSession newSession = httpServletRequest.getSession(false);

        newSession.setAttribute("emailId", stringEncryptor.encrypt(personalInfoResponse.getEmailId()));
        newSession.setAttribute("mobileNumber", stringEncryptor.encrypt(personalInfoResponse.getMobileNumber()));
        newSession.setAttribute("phoneNumber", stringEncryptor.encrypt(personalInfoResponse.getPhoneNumber()));
    }

    public String getChsEndpointUrl() {
        return chsEndpointUrl;
    }

    public void setChsEndpointUrl(String chsEndpointUrl) {
        this.chsEndpointUrl = chsEndpointUrl;
    }

}
