package com.model2.mvc.service.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.user.UserDao;

import lombok.Getter;

@Getter
@Component
public class AllDao {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private PurchaseDao purchaseDao;
	
	@Autowired
	private com.model2.mvc.service.purchase.ShoppingCartDao shoppingCartDao;
}
