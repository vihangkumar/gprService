package com.incomm.wmp.gprServices.outbound.ingo.responses;

/**
 * Created by dvontela on 11/8/2016.
 */
public class IngoFindCustomerResponse extends BaseResponse {

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IngoFindCustomerResponse{");
        sb.append("customerId='").append(customerId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

