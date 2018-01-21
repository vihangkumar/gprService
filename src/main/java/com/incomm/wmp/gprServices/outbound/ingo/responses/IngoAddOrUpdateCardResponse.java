package com.incomm.wmp.gprServices.outbound.ingo.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dvontela
 */
public class IngoAddOrUpdateCardResponse extends BaseResponse{

    private List<CardResponse> cardList = new ArrayList<CardResponse>();

    public List<CardResponse> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardResponse> cardList) {
        this.cardList = cardList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IngoAddOrUpdateCardResponse{");
        sb.append("cardList=").append(cardList);
        sb.append('}');
        return sb.toString();
    }
}
