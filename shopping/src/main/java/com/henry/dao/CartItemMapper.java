package com.henry.dao;

import com.henry.pojo.CartItem;
import com.henry.pojo.CartItemDetailDTOItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CartItemMapper {

    @Select("SELECT * FROM cart_item WHERE cart_id=#{cartId} AND product_id=#{productId}")
    public CartItem selectByCartIdAndProductId(Integer cartId, Integer productId);

    @Insert("INSERT INTO cart_Item(cart_id, product_id, quantity, added_price) " +
            "VALUES(#{cartId}, #{productId}, #{quantity}, #{addedPrice})")
    public int insertCartItem(CartItem cartItem);

    @Update("UPDATE cart_Item SET " +
            "cart_id = #{cartId} ," +
            "product_id = #{productId}, " +
            "quantity = #{quantity}, " +
            "added_price = #{addedPrice} " +
            "WHERE item_id = #{itemId}")
    public int updateCartItem(CartItem cartItem);

    @Select(" SELECT " +
            " p.id AS productID, " +
            " p.name AS name, " +
            " p.price AS price, " +
            " p.stock_quantity AS stock_quantity, " +
            " p.is_active AS isActive, " +
            " ci.quantity AS quantity " +
            " FROM cart_item ci " +
            " JOIN product p ON ci.product_id = p.id" +
            " WHERE ci.cart_id = #{cartId};")
    public List<CartItemDetailDTOItem> selectCartItemDetailDTOsByCartId(Integer cartId);

    @Select("SELECT * FROM cart_item WHERE cart_id=#{cartId};")
    public List<CartItem> selectByCartId(Integer cartId);

    @Delete("DELETE FROM cart_item WHERE cart_id=#{cartId};")
    public void deleteByCartId(Integer cartId);

    @Delete("DELETE FROM cart_item WHERE cart_id=#{cartId} AND product_id=#{productId}")
    public void deleteByCartIdAndProductId(Integer cartId, Integer productId);
}
