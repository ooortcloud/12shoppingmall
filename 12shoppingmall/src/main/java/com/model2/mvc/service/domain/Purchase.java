package com.model2.mvc.service.domain;


import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Purchase {
	
	private User buyer;   // buyer_id
	private String divyAddr;   // demailaddr
	private String divyDate;  // dlvy_date
	private String divyRequest;   // dlvy_request
	private Date orderDate;  // order_data
	private String paymentOption;  // payment_option
	private Product purchaseProd;  // prod_no
	private String receiverName;  // receiver_name
	private String receiverPhone;  // receiver_phone
	private String tranCode;  // tran_status_code (1~3)
	private Integer tranNo;  // tran_no 
	private Integer numberOfPurchase;  // 구매 수량
	
}