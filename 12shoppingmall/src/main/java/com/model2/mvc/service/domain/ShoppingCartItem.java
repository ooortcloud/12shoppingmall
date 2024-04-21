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
	private boolean selected;  // true :: ���� ���
	private String prodName;
	private Integer numberOfPurchase;
}
