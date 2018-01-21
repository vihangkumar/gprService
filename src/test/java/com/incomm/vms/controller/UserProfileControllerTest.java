package com.incomm.vms.controller;

import com.incomm.vms.request.Address;
import com.incomm.vms.request.UpdatePasswordReq;
import com.incomm.vms.request.UpdateProfileRequest;
import org.auth.cardmanagement.PersonalInfoInquiryResponse;
import org.auth.cardmanagement.UpdatePasswordResponse;
import org.auth.cardmanagement.UpdatedPersonalInfoResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by dvontela on 8/17/2017.
 */
public class UserProfileControllerTest extends BaseRunner{

    @Before
    public void setup(){
        super.setUp();
    }

    @Test
    public void updatePassword() throws Exception {
        UpdatePasswordReq updatePwdRequest = new UpdatePasswordReq();
        updatePwdRequest.setUserName("abc");
        updatePwdRequest.setOldPassword("1234");
        updatePwdRequest.setPassword("3456");

        UpdatePasswordResponse updatePwdResponse = new UpdatePasswordResponse();
        updatePwdResponse.setResponse(getResponse());
        mockServices();
        when(templateUsermanagementPort.marshalSendAndReceive(any())).thenReturn(updatePwdResponse);
        ResponseEntity<Object> baseResponse = userProfileController.updatePassword(updatePwdRequest, "DFC",request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseResponse));
        Assert.assertNotNull(baseResponse.getBody());
    }

    @Test
    public void retrieveUserProfile() throws Exception {
        PersonalInfoInquiryResponse personalInfoResponse = new PersonalInfoInquiryResponse();
        personalInfoResponse.setResponse(getResponse());
        org.auth.cardmanagement.Address address = new org.auth.cardmanagement.Address();
        address.setZip("758");
        address.setState("968");
        address.setCountry("754");
        address.setCity("54");

        personalInfoResponse.setPhysicalAddress(address);
        personalInfoResponse.setMailingAddress(address);
        personalInfoResponse.setFirstName("abc");
        personalInfoResponse.setEmailId("aaa");
        personalInfoResponse.setPhoneNumber("adfad");
        personalInfoResponse.setMobileNumber("dasfdsa");
        personalInfoResponse.setSmsAlertStatus("1");


        mockServices();
        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(personalInfoResponse);
        ResponseEntity<Object> objectResponseEntity = userProfileController.retrieveUserProfile("072017", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());
    }

    @Test
    public void updateProfile() throws Exception {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        Address address = new Address();
        address.setZip("758");
        address.setState("968");
        address.setCountry("754");
        address.setCity("54");
        updateProfileRequest.setPhyAddrMatchesMailAddr(true);
        updateProfileRequest.setPhysicalAddress(address);
        updateProfileRequest.setEmailAddress("q3wer");
        updateProfileRequest.setFirstName("gfgf");
        updateProfileRequest.setPrimaryPhoneNumber("95656566");
        updateProfileRequest.setSecondaryPhoneNumber("5858888");

        UpdatedPersonalInfoResponse updatedPersonalInfoResponse = new UpdatedPersonalInfoResponse();
        updatedPersonalInfoResponse.setResponse(getResponse());
        mockServices();

        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(updatedPersonalInfoResponse);
        ResponseEntity<Object> baseResponse = userProfileController.updateProfile(updateProfileRequest, "DFC",request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseResponse));
        Assert.assertNotNull(baseResponse.getBody());
    }

}