package com.model2.mvc.web.purchase;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Message;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.dto.product.UpdateTranCodeByProdDto;
import com.model2.mvc.service.dto.product.UpdateTranCodeDto;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.AllDao;

@RestController
@RequestMapping("/rest/purchase")
public class PurchaseRestController {

	/*
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService service;
	*/
	
	@Autowired
	private AllDao allDao;

	@Value("${common.pageSize}")
	int pageSize;
	
	@Value("${common.pageUnit}")
	int pageUnit;
	
	public PurchaseRestController() {
		
	}
	
	@PostMapping("/updateTranCodeByProd")
	public Boolean updateTranCodeByProd(@RequestBody UpdateTranCodeByProdDto dto) throws SQLException {
		
		System.out.println(dto);
		Purchase purchase = new Purchase();
		Product product = new Product();
		if(dto.getProdNo() != null) {
			product.setProdNo(dto.getProdNo());
			purchase.setTranNo(-1);
		} else {
			product.setProdNo(-1);
		}
		
		purchase.setTranCode(dto.getTranCode());
		purchase.setPurchaseProd(product);
		int result = allDao.getPurchaseDao().updateTranCode(purchase);
		
		if (result == 1)
			return true;
		else
			return false;
	}
	
	@PostMapping("/updateTranCode")
	public boolean updateTranCode(@RequestBody UpdateTranCodeDto dto) throws Exception {
		
		System.out.println(dto);
		Purchase purchase = new Purchase();
		
		// prod_no�� ������Ʈ����, tran_no�� ������Ʈ���� Ȯ�� (�� �޼ҵ�� �����, ��ۿϷ� �� �� ó���Ϸ��� ��)
		Product product = new Product();
		product.setProdNo(-1);
		purchase.setPurchaseProd(product);
		purchase.setTranNo(dto.getTranNo());
		purchase.setTranCode("3");
		
		int result = allDao.getPurchaseDao().updateTranCode(purchase);
		
		if(result == 1)
			return true;
		else
			return false;
	}
	
	//==================================================================================
	//shoppingcart :: ���� ��� ���� ����̱⿡ controller �и��� �� �ߴ�.
	//==================================================================================
	
	@PostMapping("/addShoppingCart")
	public Message addShoppingCart(@RequestBody ShoppingCartItem item) throws Exception {
		
		System.out.println(item);
		
		int result = allDao.getShoppingCartDao().insertItem(item);
		
		if(result == 1) {
			System.out.println("����");
			return new Message("��ٱ��� ��� ����!");
		}
		else {
			System.out.println("����");
			return new Message("��ٱ��� ��� ����...");
		}	
	}
}
