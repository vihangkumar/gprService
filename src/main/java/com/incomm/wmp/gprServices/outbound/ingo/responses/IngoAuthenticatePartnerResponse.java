package com.incomm.wmp.gprServices.outbound.ingo.responses;

/**
 * Created by incomm on 11/3/16.
 */
public class IngoAuthenticatePartnerResponse extends BaseResponse{

    private String sessionID;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
