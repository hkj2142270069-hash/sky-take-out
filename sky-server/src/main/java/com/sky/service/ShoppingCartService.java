package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
/*
* 购物车数据查询
 */
    List<ShoppingCart> showShoppingCart();

    /*
    * 购物车数据删除
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /*
    * 清空购物车
     */
    void cleanShoppingCart();
}
