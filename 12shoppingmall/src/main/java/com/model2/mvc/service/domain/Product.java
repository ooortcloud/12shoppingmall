package com.model2.mvc.service.domain;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {
	
	private String fileName;  // thumbnail
	private String manuDate;
	private Integer price;
	private String prodDetail;
	private String prodName;
	private Integer prodNo;
	private Date regDate;
	private Integer inventory;
	
	private String proTranCode;  // 현재 상태

	public Product() {
		
	}
}
