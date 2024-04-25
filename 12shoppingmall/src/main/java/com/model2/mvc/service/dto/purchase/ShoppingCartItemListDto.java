package com.model2.mvc.service.dto.purchase;

import java.util.List;

import com.model2.mvc.service.domain.ShoppingCartItem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShoppingCartItemListDto {
	private List<ShoppingCartItem> shoppingCartItemList;
}
