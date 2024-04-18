package com.model2.mvc.service.purchase.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
//=============================================================/
//우선순위 1 : 메소드에 설정된 @Transactional
//우선순위 2 : 클래스에 설정된 @Transactional
//우선순위 3 : 인터페이스에 설정된 @Transactional
//======================== 추가된 부분  ==========================/
@Transactional  // transaction metadata 추가
public class PurchaseServiceImpl implements PurchaseService {

	// spring boot로 오면서 Mapper 간 code 공유가 안됨... => design pattern 적용 (이름 기억 안남)
	@Autowired
	// private PurchaseDao purchaseDao;
	private AllDao allDao;
	
	public PurchaseServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	*/

	@Override
	public int addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub

		// 구매한 수량만큼 차감해야 함
		Product product = purchase.getPurchaseProd();		
		int remainQuantity = product.getInventory() - purchase.getNumberOfPurchase();
		product.setInventory(remainQuantity);
		purchase.setPurchaseProd(product);
		
		// 정상적으로 수량이 변경된 경우에만 구매 list에 추가
		if( allDao.getProductDao().decreaseInventory(product) == 1 ) {
			return allDao.getPurchaseDao().insertPurchase(purchase);
		} else {
			return 0;
		}
	}

	@Override
	public Purchase getPurchase(int tranNo) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return allDao.getPurchaseDao().findPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return allDao.getPurchaseDao().getPurchaseList(map);
	}

	@Override
	public int updateTranCode(Purchase purchase) throws SQLException {
		// TODO Auto-generated method stub
		return allDao.getPurchaseDao().updateTranCode(purchase);
	}
	
	@Override
	public int updatePurchase(Purchase purchase) throws Exception {
		return allDao.getPurchaseDao().updatePurchase(purchase);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		return allDao.getProductDao().findProduct(prodNo);
	}
	
	@Override
	public User getUser(String userId) throws Exception {
		return allDao.getUserDao().getUser(userId);
	}
}
