package com.incomm.wmp.gprServices.outbound.ingo.requests;

import javax.validation.constraints.NotNull;

/**
 * @author dvontela
 * @author Allen - modified
 *
 */
public class IngoAuthenticateOBORequest extends BaseRequest {

    @NotNull
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
