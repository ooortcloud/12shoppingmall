package com.model2.mvc.service.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UpdateTranCodeByProdDto {

	private Integer prodNo;
	private String tranCode;
}