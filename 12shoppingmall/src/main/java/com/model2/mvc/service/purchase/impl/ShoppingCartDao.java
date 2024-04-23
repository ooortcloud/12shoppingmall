package com.model2.mvc.service.purchase.impl;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.dto.purchase.ShoppingCartItemDto;


// package 위치 바꾸고 싶은데, 바꾸면 자꾸 `Invalid bound statement (not found)` error남 ㅠㅠ
@Mapper
public interface ShoppingCartDao {

	public int insertItem(ShoppingCartItem item) throws Exception;
	
	public List<ShoppingCartItemDto> getShoppingCartList(String userId) throws Exception;
	
	public int checkShoppingCart(ShoppingCartItem item) throws Exception;
	
	public int deleteShoppingCartItem(Integer cartNo) throws Exception;
}
