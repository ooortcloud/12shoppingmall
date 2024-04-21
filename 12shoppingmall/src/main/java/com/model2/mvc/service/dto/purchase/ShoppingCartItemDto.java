package com.model2.mvc.service.dto.purchase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ShoppingCartItemDto {

	private Integer cartNo;
	private Integer prodNo;
	private String userId;
	private String prodName;
}
