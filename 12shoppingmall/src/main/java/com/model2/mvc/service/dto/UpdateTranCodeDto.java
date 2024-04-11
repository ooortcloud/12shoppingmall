package com.model2.mvc.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UpdateTranCodeDto {

	private Integer tranNo;
	private String tranCode;
	
}
