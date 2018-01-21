package com.incomm.vms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.wmp.gprServices.controller.CardController;
import com.incomm.wmp.gprServices.controller.CardIssuanceController;
import com.incomm.wmp.gprServices.controller.UserAccountCreationController;
import com.incomm.wmp.gprServices.controller.UserProfileController;
import com.incomm.wmp.gprServices.util.VmsServiceUtil;
import com.incomm.chstypes.Header;
import com.incomm.chstypes.Response;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by dvontela on 8/15/2017.
 */

public class BaseRunner {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpSession httpSession;

    @Mock
    StringEncryptor stringEncryptor;

    @Mock
    VmsServiceUtil vmsServiceUtil;

    @InjectMocks
    public UserControllerTest userController;

    @InjectMocks
    public CardController cardController;

    @InjectMocks
    public UserAccountCreationController userAccountCreationController;

    @InjectMocks
    public CardIssuanceController cardIssuanceController;

    @InjectMocks
    public UserProfileController userProfileController;

    @Mock
    protected WebServiceTemplate templateCHSPort;

    @Mock
    protected WebServiceTemplate templateCardIssuerPort;

    @Mock
    protected WebServiceTemplate templateUsermanagementPort;

    MockHttpServletRequest request = new MockHttpServletRequest();

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

    }

    public Header getHeader(){
        Header chsHeader = new Header();
        chsHeader.setSrcAppId("DFC");
        return chsHeader;
    }

    public Response getResponse(){
        Response response = new Response();
        response.setRespCode("00");
        response.setRespMessage("SUCCESS");
        return response;
    }

    public void mockServices() throws Exception{
        when(httpSession.getAttribute(any())).thenReturn("12558852522");
        when(stringEncryptor.decrypt(any())).thenReturn("12365747");
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        when(vmsServiceUtil.getCHSHeader(any(),any(),any())).thenReturn(getHeader());
    }
}
