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
	
	// DAO������ �ܰ� ó�� ������� �ۼ��ؾ� ���߿� ������ �� �������� ����. ������ service layer���� �����ϴ� ���� ����.
	// ������ �������ٵ� @Mapper������ return type ������� proxy ��ü�� generate�ϱ� ������, �ܰ� ��ȸ�� �����ؾ� ó���ϱ� ����.
	public List<Purchase> getPurchaseList(Map<String ,Object> map) throws Exception;
	
	public int getTotalCount(String buyerId) throws Exception;

	public int updateTranCode(Purchase purchase) throws SQLException;
	
	public int updatePurchase(Purchase purchase) throws Exception;
}
