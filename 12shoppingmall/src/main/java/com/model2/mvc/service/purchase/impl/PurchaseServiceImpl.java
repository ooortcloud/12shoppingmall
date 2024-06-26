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
import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.dto.purchase.ShoppingCartItemDto;
import com.model2.mvc.service.purchase.AllDao;
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", allDao.getPurchaseDao().getPurchaseList(map));
		result.put("totalCount", allDao.getPurchaseDao().getTotalCount( String.valueOf(map.get("buyerId")) ));
		return result;
	}

	@Override
	public int updateTranCode(Purchase purchase) throws SQLException {
		// TODO Auto-generated method stub
		return allDao.getPurchaseDao().updateTranCode(purchase);
	}
	
	@Override
	public int updatePurchase(Purchase purchase) throws Exception {
		
		// 구매 수량 수정사항 반영
		int beforeNumberOfPurchase = allDao.getPurchaseDao().findPurchase(purchase.getTranNo()).getNumberOfPurchase(); 
		int afterNumberOfPurchase = purchase.getNumberOfPurchase();
		int changed = afterNumberOfPurchase - beforeNumberOfPurchase;
		Product product = allDao.getProductDao().findProduct(purchase.getPurchaseProd().getProdNo());
		product.setInventory( product.getInventory() - changed );
		
		if(allDao.getProductDao().updateProduct(product) != 1) {
			return -1;
		} else {
			return allDao.getPurchaseDao().updatePurchase(purchase);
		}
	}
	
	@Override
	public Product getProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return allDao.getProductDao().findProduct(prodNo);
	}

	@Override
	public User getUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		return allDao.getUserDao().getUser(userId);
	}

	//==================================================================================
	//shoppingcart :: 결제 기능 세부 요소이기에 service 분리는 안 했다.
	//==================================================================================
	
	@Override
	public List<ShoppingCartItemDto> getShoppingCartList(String userId) throws Exception {
		return allDao.getShoppingCartDao().getShoppingCartList(userId);
	}
	
	@Override
	public int insertItem(ShoppingCartItem item) throws Exception {
		// TODO Auto-generated method stub
		return allDao.getShoppingCartDao().insertItem(item);
	}
	
	public int checkShoppingCart(ShoppingCartItem item) throws Exception {
		return allDao.getShoppingCartDao().checkShoppingCart(item);
	}

	@Override
	public int deleteShoppingCartItem(Integer cartNo) throws Exception {
		// TODO Auto-generated method stub
		return allDao.getShoppingCartDao().deleteShoppingCartItem(cartNo);
	}	
}
