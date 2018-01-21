/*
package com.incomm.vms.controller;

import com.incomm.vms.request.ResetPasswordReq;
import com.incomm.vms.request.UserAccountInfoReq;
import com.incomm.vms.response.BaseResponse;
import org.auth.cardmanagement.SaveUserCredentialsResponse;
import org.auth.cardmanagement.SetNewPasswordRequest;
import org.auth.cardmanagement.SetNewPasswordResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

public class UserAccountCreationTest extends BaseRunner{

    @Before
    public void setup(){
        super.setUp();
    }

    @Test
    public void resetPassword() throws Exception{
        ResetPasswordReq resetPasswordReq= new ResetPasswordReq();
        //resetPasswordReq.setUsername("sarah");
        resetPasswordReq.setNewPassword("Emo1234");

        SetNewPasswordResponse setNewPasswordResponse= new SetNewPasswordResponse();
        setNewPasswordResponse.setResponse(getResponse());
        mockServices();
        when(templateUsermanagementPort.marshalSendAndReceive(any())).thenReturn(setNewPasswordResponse);
        ResponseEntity<Object> objectResponseEntity= userAccountCreationController.resetPassword(resetPasswordReq,"DFC", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());

    }

*/
/*    @Test
    public void createUserAccount() throws Exception{
        UserAccountInfoReq userAccountInfoReq= new UserAccountInfoReq();
        userAccountInfoReq.setUsername("sarah123");
        userAccountInfoReq.setPassword("Emo1234");
        userAccountInfoReq.setConfirmPassword("Emo1234");
        userAccountInfoReq.setSecurityQuestionOne("What was your childhood nickname");
        userAccountInfoReq.setSecurityAnswerOne("atlanta");
        userAccountInfoReq.setSecurityQuestionTwo("What is the last name of your favorite 10th grade teacher");
        userAccountInfoReq.setSecurityAnswerTwo("atlanta");
        userAccountInfoReq.setSecurityQuestionThree("What is your maternal grandmother's maiden name");
        userAccountInfoReq.setSecurityAnswerThree("atlanta");

        SaveUserCredentialsResponse saveUserCredentialsResponse= new SaveUserCredentialsResponse();
        saveUserCredentialsResponse.setResponse(getResponse());
        mockServices();
        when(templateUsermanagementPort.marshalSendAndReceive(any())).thenReturn(saveUserCredentialsResponse);
        ResponseEntity<Object> objectResponseEntity= userAccountCreationController.createUserAccount("DFC",userAccountInfoReq, request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
    }*//*

}
*/
