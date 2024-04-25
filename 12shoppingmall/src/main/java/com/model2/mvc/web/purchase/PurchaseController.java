package com.model2.mvc.web.purchase;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.ShoppingCartItem;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.dto.purchase.ShoppingCartItemListDto;
import com.model2.mvc.service.dto.purchase.CheckOutDto;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService service;

	// @Value("#{commonProperties['pageSize']}")
	@Value("${common.pageSize}")
	int pageSize;
	// @Value("#{commonProperties['pageUnit']}")
	@Value("${common.pageUnit}")
	int pageUnit;
	
	public PurchaseController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/addPurchase")
	public ModelAndView addPurchase(@RequestParam Integer prodNo, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/addPurchaseView.jsp");
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(service.getProduct(prodNo) );
		purchase.setBuyer( service.getUser(  ((User) session.getAttribute("user")).getUserId()) );
		mv.addObject("purchase", purchase);
		return mv;
	}

	@PostMapping("/addPurchase")
	public ModelAndView addPurchase(@ModelAttribute Purchase purchase, @RequestParam Integer prodNo, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/addPurchase.jsp");

		System.out.println(purchase);
		purchase.setPurchaseProd( service.getProduct(prodNo) );
		purchase.setBuyer( service.getUser(( (User)session.getAttribute("user") ).getUserId()) );
		purchase.setTranCode("1");  // '구매완료' 상태코드
		service.addPurchase(purchase);
		mv.addObject("purchase", purchase);
		return mv;
	}

	/// /updatePurchase.do 때문에 둘 다 해야 함
	@RequestMapping("/getPurchase")
	public ModelAndView getPurchase(@RequestParam Integer tranNo) throws SQLException, Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/getPurchase.jsp");
		mv.addObject("purchase", service.getPurchase(tranNo) );  // Object로 저장되는데 과연 정상적으로 OGNL이 먹힐지?
		return mv;
	}
	
	// list에서는 조회 및 검색이 모두 사용됨.
	@RequestMapping("/listPurchase")        
	public ModelAndView listPurchase(@ModelAttribute Search search, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/listPurchase.jsp");
		
		// 최초 접근 시 Query Parameter인 currentPage값이 null일 때 1페이지에서 시작하도록 설정
		if(search.getCurrentPage() == null || search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		
		/// 리스트 가져오기
		Map<String,Object> map = new HashMap<>();
		map.put("search", search);
		map.put("buyerId", ( (User)session.getAttribute("user") ).getUserId() );
		Map<String,Object> resultMap = service.getPurchaseList(map);
		
		/// 하단 페이지 구성
		Page myPage = new Page(search.getCurrentPage(), (Integer) resultMap.get("totalCount"), pageUnit, pageSize);
		//  1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		if( (search.getCurrentPage() > myPage.getPageUnit() ) && !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() )
			myPage.setBeginUnitPage(1);
		
		mv.addObject("list", resultMap.get("list"));
		mv.addObject("resultPage", myPage);
		mv.addObject("title", "purchase");
		
		return mv;
	}

	@GetMapping("/updatePurchase")
	public ModelAndView updatePurchase(@RequestParam Integer tranNo) throws SQLException, Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/updatePurchaseView.jsp");
		Purchase purchase = service.getPurchase(tranNo);
		purchase.setDivyDate(purchase.getDivyDate().split(" ")[0]);
		mv.addObject("purchase", purchase );
		
		return mv;
	}

	/// 구매 목록 수정
	// auto binding 전용 domain 객체를 만들어서, post하는데 한계가 있는 datatype에 대한 binding 오류를 피한다.
//	@PostMapping("/updatePurchase")
//	public ModelAndView updatePurchase(@ModelAttribute PurchaseOnlyString purchaseEx, @RequestParam String orderDate) throws Exception {
//		ModelAndView mv  = new ModelAndView("forward:/purchase/updatePurchase.jsp");
//		Purchase purchase = new Purchase(purchaseEx);
//		purchase.setOrderDate(Date.valueOf(orderDate));
//		service.updatePurchase(purchase);   
//		
//		mv.addObject("purchase", purchase);
//		return mv;
//	}
	
	@PostMapping("/updatePurchase")
	public ModelAndView updatePurchase(@ModelAttribute Purchase purchase, @RequestParam Integer tranNo, @RequestParam String orderDate) throws Exception {
		// QueryString에 있는 값은 @RequestParam을 사용하여 받아줘야 한다... (Header에 있는 정보는 @ModelAttribute는 참조하지 않는듯)
		purchase.setTranNo(tranNo);
		purchase.setOrderDate(Date.valueOf(orderDate));
		service.updatePurchase(purchase);
		
		ModelAndView mv  = new ModelAndView("forward:/purchase/updatePurchase.jsp");
		mv.addObject("purchase", purchase);
		return mv;
	}
	
	
	/*
	/// admin이 client에게 상품 보낼 때
	@GetMapping("/updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@RequestParam Integer prodNo, @RequestParam String tranCode) throws SQLException {
		// controller에서 다른 컨트롤러를 호출할 수도 있다! (forward 사용하면 기존 request 유지 가능)
		ModelAndView mv = new ModelAndView("forward:/product/listProduct/manage");
		Purchase purchase = new Purchase();
		Product product =  new Product();
		// prod_no로 업데이트하는 경우 
		if( prodNo != null ) {
			product.setProdNo(prodNo);
			purchase.setTranNo(-1);  // null을 쓸 수 없으니 -1로 대체 표현
		} 
		// tran_no로 업데이트하는 경우
		else {
			// product.setTranNo(tranNo);
			product.setProdNo(-1);
		}
		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);
		service.updateTranCode(purchase);
		
		return mv;
	}
	*/
	
	/// user가 상품 구매할 때
	/*
	 *  @RequestParam :: QueryString에서 parsing한 데이터 가져옴.
	 *  		required :: 해당 Query Parameter가 필수로 존재해야 하는가? (default = true)
	 */
	@GetMapping("/updateTranCode")
	public ModelAndView updateTranCode(@RequestParam(required = false) Integer tranNo, @RequestParam(required = false) Integer prodNo) throws SQLException {
		ModelAndView mv = new ModelAndView("forward:/purchase/listPurchase");
		Purchase purchase = new Purchase();
		purchase.setTranCode("3");

		// prod_no로 업데이트인지, tran_no로 업데이트인지 확인 (이 메소드로 배송중, 배송완료 둘 다 처리하려고 함)
		Product product = new Product();
		if( tranNo != null ) {
			product.setProdNo(-1);
			purchase.setTranNo(tranNo);
		} else {
			product.setProdNo(prodNo);
			purchase.setTranNo(-1);  // null을 쓸 수 없으니 -1로 대체 표현
		}
		purchase.setPurchaseProd(product);
		service.updateTranCode(purchase);
		
		return mv;
	}
	
	//==================================================================================
	//shoppingcart :: 결제 기능 세부 요소이기에 controller 분리는 안 했다.
	//==================================================================================
		
	@GetMapping("/shoppingCartList")
	public ModelAndView shoppingCartList(HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/shoppingCart.jsp");
		
		String userId = ( (User)session.getAttribute("user") ).getUserId();
		mv.addObject("list", service.getShoppingCartList(userId));
		return mv;
	}
	
	
	/*
	// 장바구니 내 결제 기능 (다중 구매)
	@PostMapping("/checkOutOfTheCart")
	public ModelAndView checkOutOfTheCart(@ModelAttribute(name = "shoppingCartItemListDto") ShoppingCartItemListDto dto) {
		ModelAndView mv = new ModelAndView("forward:/purchase/checkOutShoppingCart.jsp");
		
		System.out.println(dto.getShoppingCartItemList());
		return mv;		
	}
	*/
	
	//Spring은 (아마도 정확히는 @RequestBody는) Content type 'application/x-www-form-urlencoded'를 지원하지 못한다고 한다.
	// org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
	@PostMapping(path="/checkOutOfTheCart")
	public ModelAndView checkOutOfTheCart(@ModelAttribute CheckOutDto dto, HttpSession session ) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/listPurchase");


		for(String str : dto.getSelected()) {
			String[] temp = str.split(" ");
			int prodNo = Integer.parseInt(temp[0]);
			int index = Integer.parseInt(temp[1]);
			int cartNo = Integer.parseInt(temp[2]);
			Integer numberOfPurchase = dto.getNumberOfPurchase()[index];
			
			// 하나라도 잘못되면 transaction에 의해 rollback되도록 유도
			Purchase purchase = new Purchase();
			purchase.setBuyer( service.getUser( ((User)session.getAttribute("user")).getUserId() ) );
			purchase.setPurchaseProd( service.getProduct(prodNo) );
			purchase.setNumberOfPurchase(numberOfPurchase);	
			purchase.setTranCode("1");
			
			// batch 등을 통한 query 최적화가 가능하긴 함. 하지만 현재 그럴만한 개발 비용 여유가 없기도 하고, 장바구니에 100개씩 넣고 주문할 일도 없고...
			if ((service.addPurchase(purchase) == 1) && (service.deleteShoppingCartItem(cartNo) == 1) ) {
				continue;
			} else {
				return null;
			}
		}		
		
		return mv;		
	}
}
