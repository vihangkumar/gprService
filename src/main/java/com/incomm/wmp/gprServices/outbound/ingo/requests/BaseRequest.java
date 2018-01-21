package com.incomm.wmp.gprServices.outbound.ingo.requests;


import javax.validation.constraints.NotNull;

/**
 * @author Allen
 * <p>Base requests for all Ingo Requests</p>
 */
public class BaseRequest {

    @NotNull
    private String deviceID;
    private String sessionID;
    private String platformType;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
}
