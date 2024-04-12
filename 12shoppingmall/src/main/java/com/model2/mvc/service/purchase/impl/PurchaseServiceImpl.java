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

	@Autowired
	// @Qualifier("purchaseDaoImpl")
	@Qualifier("purchaseDao")  // @Mapper interface의 구현체를 받자.
	private PurchaseDao purchaseDao;
	
	public PurchaseServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	*/

	@Override
	public int addPurchase(Purchase pruchase) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDao.insertPurchase(pruchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return purchaseDao.findPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDao.getPurchaseList(map);
	}

	@Override
	public int updateTranCode(Purchase purchase) throws SQLException {
		// TODO Auto-generated method stub
		return purchaseDao.updateTranCode(purchase);
	}
	
	@Override
	public int updatePurchase(Purchase purchase) throws Exception {
		return purchaseDao.updatePurchase(purchase);
	}

	@Override
	public Product getProduct(int prodNo) throws SQLException {
		return purchaseDao.getProduct(prodNo);
	}
	
	@Override
	public User getUser(String userId) throws Exception {
		return purchaseDao.getUser(userId);
	}
}
