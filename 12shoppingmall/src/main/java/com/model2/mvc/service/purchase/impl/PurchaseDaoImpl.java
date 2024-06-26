package com.model2.mvc.service.purchase.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.user.UserDao;

// @Repository("purchaseDaoImpl")  << @Mapper interface�� ��ü
public class PurchaseDaoImpl implements PurchaseDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	@Autowired
	@Qualifier("userDaoImpl")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;
	
	public PurchaseDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	*/

	@Override
	public int insertPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}

	@Override
	public Purchase findPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		Purchase purchase = sqlSession.selectOne("PurchaseMapper.findPurchase", tranNo); 
		purchase.setBuyer(userDao.getUser(purchase.getBuyer().getUserId() ) );
		purchase.setPurchaseProd(productDao.findProduct(purchase.getPurchaseProd().getProdNo() ) );
		return purchase;
	}
   
	
	@Override // Search, User
	public List<Purchase> getPurchaseList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}
	
	public int getTotalCount(String buyerId) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
	}

	@Override
	public int updateTranCode(Purchase purchase) throws SQLException {
		// TODO Auto-generated method stub
		return sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}
	
	@Override
	public int updatePurchase(Purchase purchase) throws Exception {
		return sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}
}
