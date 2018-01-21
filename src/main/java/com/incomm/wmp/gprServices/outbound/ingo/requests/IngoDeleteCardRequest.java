package com.incomm.wmp.gprServices.outbound.ingo.requests;

import javax.validation.constraints.NotNull;

/**
 * @author dvontela
 */
public class IngoDeleteCardRequest extends BaseRequest {

    @NotNull
    private String customerId;
    @NotNull
    private String cardId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
