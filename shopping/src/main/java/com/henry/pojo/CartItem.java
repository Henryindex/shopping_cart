package com.henry.pojo;

public class CartItem {
    // CartItem.id
    private Integer itemId;
    // Cart.id
    private Integer cartId;
    // Product.id
    private Integer productId;
    // How many item you add
    private Integer quantity;
    // Price of Added
    private Integer addedPrice;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAddedPrice() {
        return addedPrice;
    }

    public void setAddedPrice(Integer addedPrice) {
        this.addedPrice = addedPrice;
    }
}
