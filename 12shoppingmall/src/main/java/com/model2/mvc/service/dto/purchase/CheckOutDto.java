package com.model2.mvc.service.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckOutDto {
	private String[] selected;
	private Integer[] numberOfPurchase;
}
