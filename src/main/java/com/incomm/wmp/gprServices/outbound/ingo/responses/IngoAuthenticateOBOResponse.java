package com.incomm.wmp.gprServices.outbound.ingo.responses;

/**
 * Created by dvontela on 11/9/2016.
 */
public class IngoAuthenticateOBOResponse extends BaseResponse {

    private String ssoToken;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }
}
