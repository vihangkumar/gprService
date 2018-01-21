package com.incomm.wmp.gprServices.response;

/**
 * Created by dvontela on 11/2/2017.
 */
public class LoginResponse {

    private BaseResponse response;

    private String gprCardStatus;

    public String getGprCardStatus() {
        return gprCardStatus;
    }

    public void setGprCardStatus(String gprCardStatus) {
        this.gprCardStatus = gprCardStatus;
    }

    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }
}
