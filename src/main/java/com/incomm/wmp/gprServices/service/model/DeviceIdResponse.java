package com.incomm.wmp.gprServices.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceIdResponse {
    private Device device;

    @JsonProperty("response_id")
    private String responseId;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return "DeviceIdResponse{" +
                "device=" + device +
                ", responseId='" + responseId + '\'' +
                '}';
    }

}
