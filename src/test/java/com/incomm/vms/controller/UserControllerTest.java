package com.incomm.vms.controller;

import com.incomm.vms.request.LoginRequest;
import com.incomm.vms.response.BaseResponse;
//import org.auth.cardmanagement.Header;
//import org.auth.cardmanagement.Response;
//import org.auth.cardmanagement.ValidateAndAuthenticateRevisedResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by dvontela on 8/17/2017.
 */
public class UserControllerTest extends BaseRunner{

    @Before
    public void setup(){
        super.setUp();
    }
/*
    @Test
    public void loginRevised() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("Demo1234");
        loginRequest.setUsername("mmtest60");

        ValidateAndAuthenticateRevisedResponse vaaResp = new ValidateAndAuthenticateRevisedResponse();
        vaaResp.setAccountId("23456753");
        vaaResp.setResponse(getResponse());
        vaaResp.setCustomerId("123456877");
        vaaResp.setActiveDate("1234");
        vaaResp.setBin("13431");
        vaaResp.setFirstName("1234rds");
        vaaResp.setCardNumber("12345678976");

        mockServices();

        when(templateCHSPort.marshalSendAndReceive(any())).thenReturn(vaaResp);
        ResponseEntity<BaseResponse> baseResponse = userController.loginRevised(loginRequest, "DFC",request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseResponse));
        Assert.assertNotNull(baseResponse.getBody());

    }
*/
}