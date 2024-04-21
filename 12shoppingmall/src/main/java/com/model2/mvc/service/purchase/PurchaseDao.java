package com.model2.mvc.service.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.domain.User;

@Mapper
public interface PurchaseDao {
	
	public int insertPurchase(Purchase purchase) throws Exception;
	
	public Purchase findPurchase(int tranNo) throws Exception;
	
	// DAO에서는 단건 처리 기반으로 작성해야 나중에 조립할 때 유연성이 좋다. 통합은 service layer에서 진행하는 것이 좋음.
	// 하지만 무엇보다도 @Mapper에서는 return type 기반으로 proxy 객체를 generate하기 떄문에, 단건 조회로 진행해야 처리하기 편리함.
	public List<Purchase> getPurchaseList(Map<String ,Object> map) throws Exception;
	
	public int getTotalCount(String buyerId) throws Exception;

	public int updateTranCode(Purchase purchase) throws SQLException;
	
	public int updatePurchase(Purchase purchase) throws Exception;
}
