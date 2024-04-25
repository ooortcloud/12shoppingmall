package com.model2.mvc.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ShoppingCartItem {

	private Integer cartNo;
	private String userId;
	private Integer prodNo;
	
	private String prodName;
	private String selected;  // true :: ���� ���
	private Integer numberOfPurchase;
}
