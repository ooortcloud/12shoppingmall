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
import com.model2.mvc.service.purchase.AllDao;
import com.model2.mvc.service.purchase.PurchaseService;

@RestController
@RequestMapping("/rest/purchase")
public class PurchaseRestController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService service;

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
		int result = service.updateTranCode(purchase);
		
		if (result == 1)
			return true;
		else
			return false;
	}
	
	@PostMapping("/updateTranCode")
	public boolean updateTranCode(@RequestBody UpdateTranCodeDto dto) throws Exception {
		
		System.out.println(dto);
		Purchase purchase = new Purchase();
		
		// prod_no로 업데이트인지, tran_no로 업데이트인지 확인 (이 메소드로 배송중, 배송완료 둘 다 처리하려고 함)
		Product product = new Product();
		product.setProdNo(-1);
		purchase.setPurchaseProd(product);
		purchase.setTranNo(dto.getTranNo());
		purchase.setTranCode("3");
		
		int result = service.updateTranCode(purchase);
		
		if(result == 1)
			return true;
		else
			return false;
	}
	
	//==================================================================================
	//shoppingcart :: 결제 기능 세부 요소이기에 controller 분리는 안 했다.
	//==================================================================================
	
	@PostMapping("/addShoppingCart")
	public Message addShoppingCart(@RequestBody ShoppingCartItem item) throws Exception {
		
		System.out.println(item);
		
		int result = service.insertItem(item);
		
		if(result == 1) {
			System.out.println("성공");
			return new Message("장바구니 등록 성공!");
		}
		else {
			System.out.println("실패");
			return new Message("장바구니 등록 실패...");
		}	
	}
	
	@PostMapping("/checkShoppingCart")
	public Message checkShoppingCart(@RequestBody ShoppingCartItem item) throws Exception {
		
		int result = service.checkShoppingCart(item);
		
		if(result == 1)
			return new Message("이미 장바구니에 존재하는 상품입니다.");
		else
			return new Message("ok");
	}
	
	@PostMapping("/deleteShoppingCartItem")
	public Message deleteShoppingCartItem(@RequestBody ShoppingCartItem item) throws Exception {
		
		int result = service.deleteShoppingCartItem(item.getCartNo());
		
		if(result == 1)
			return new Message("ok");
		else
			return new Message("정상적으로 삭제가 진행되지 못했습니다...");
	}
}
