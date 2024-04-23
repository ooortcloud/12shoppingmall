package com.model2.mvc.service.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.dto.purchase.ShoppingCartItemDto;


public interface PurchaseService {

	public int addPurchase(Purchase pruchase) throws Exception;
	
	public Purchase getPurchase(int tranNo) throws SQLException, Exception;
	
	public Map<String, Object> getPurchaseList(Map<String, Object> map) throws Exception;
		
	public int updateTranCode(Purchase pruchase) throws SQLException;
	
	public int updatePurchase(Purchase purchase) throws Exception;
	
	public Product getProduct(int prodNo) throws Exception;
	
	public User getUser(String userId) throws Exception;
	
	//==================================================================================
	//shoppingcart :: 결제 기능 세부 요소이기에 service 분리는 안 했다.
	//==================================================================================
	
	public List<ShoppingCartItemDto> getShoppingCartList(String userId) throws Exception;
	
	public int insertItem(ShoppingCartItem item) throws Exception;
	
	public int checkShoppingCart(ShoppingCartItem item) throws Exception; 
}
