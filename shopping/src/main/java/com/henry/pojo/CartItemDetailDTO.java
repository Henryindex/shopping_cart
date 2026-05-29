package com.henry.pojo;

import java.util.List;

public class CartItemDetailDTO {

    private Integer totalAmount;
    private List<CartItemDetailDTOItem> items;

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItemDetailDTOItem> getItems() {
        return items;
    }

    public void setItems(List<CartItemDetailDTOItem> items) {
        this.items = items;
    }
}
