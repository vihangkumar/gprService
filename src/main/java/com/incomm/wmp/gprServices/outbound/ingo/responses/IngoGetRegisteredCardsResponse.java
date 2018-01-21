package com.incomm.wmp.gprServices.outbound.ingo.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvontela on 11/9/2016.
 */
public class IngoGetRegisteredCardsResponse extends BaseResponse {
    private List<CardResponse> cardList = new ArrayList<CardResponse>();

    public List<CardResponse> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardResponse> cardList) {
        this.cardList = cardList;
    }
}
