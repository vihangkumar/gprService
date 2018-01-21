package com.incomm.wmp.gprServices.outbound.ingo.responses;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvontela on 11/9/2016.
 */
public class IngoAddOrUpdateTokenizedCardResponse extends BaseResponse {
    private List<CardResponse> cardList = new ArrayList<CardResponse>();

    public List<CardResponse> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardResponse> cardList) {
        this.cardList = cardList;
    }
}
