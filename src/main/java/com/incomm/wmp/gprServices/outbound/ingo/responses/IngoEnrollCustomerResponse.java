package com.incomm.wmp.gprServices.outbound.ingo.responses;

/**
 * Created by dvontela on 11/9/2016.
 */
public class IngoEnrollCustomerResponse extends BaseResponse{

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
