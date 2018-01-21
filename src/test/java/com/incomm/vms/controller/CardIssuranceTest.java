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
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

public class CardIssuranceTest extends BaseRunner{

    @Before
    public void setup() {
        super.setUp();
    }

    @Test
    public void cardFound() throws Exception {
      CardFoundResponse cardFoundResponse= new CardFoundResponse();
      cardFoundResponse.setResponse(getResponse());
      mockServices();
      when(templateCardIssuerPort.marshalSendAndReceive(any())).thenReturn(cardFoundResponse);
      ResponseEntity<Object> objectResponseEntity =  cardIssuanceController.cardFound("ddsww", request);
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResponseEntity));
        Assert.assertNotNull(objectResponseEntity.getBody());




    }
}
