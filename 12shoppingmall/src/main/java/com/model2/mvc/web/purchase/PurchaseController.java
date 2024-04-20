package com.model2.mvc.web.purchase;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
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
		purchase.setTranCode("1");  // '���ſϷ�' �����ڵ�
		service.addPurchase(purchase);
		mv.addObject("purchase", purchase);
		return mv;
	}

	/// /updatePurchase.do ������ �� �� �ؾ� ��
	@RequestMapping("/getPurchase")
	public ModelAndView getPurchase(@RequestParam Integer tranNo) throws SQLException, Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/getPurchase.jsp");
		mv.addObject("purchase", service.getPurchase(tranNo) );  // Object�� ����Ǵµ� ���� ���������� OGNL�� ������?
		return mv;
	}
	
	// list������ ��ȸ �� �˻��� ��� ����.
	@RequestMapping("/listPurchase")        
	public ModelAndView listPurchase(@ModelAttribute Search search, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("forward:/purchase/listPurchase.jsp");
		
		// ���� ���� �� Query Parameter�� currentPage���� null�� �� 1���������� �����ϵ��� ����
		if(search.getCurrentPage() == null || search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		
		/// ����Ʈ ��������
		Map<String,Object> map = new HashMap<>();
		map.put("search", search);
		map.put("buyerId", ( (User)session.getAttribute("user") ).getUserId() );
		Map<String,Object> resultMap = service.getPurchaseList(map);
		
		/// �ϴ� ������ ����
		Page myPage = new Page(search.getCurrentPage(), (Integer) resultMap.get("totalCount"), pageUnit, pageSize);
		//  1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
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

	/// ���� ��� ����
	// auto binding ���� domain ��ü�� ����, post�ϴµ� �Ѱ谡 �ִ� datatype�� ���� binding ������ ���Ѵ�.
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
		// QueryString�� �ִ� ���� @RequestParam�� ����Ͽ� �޾���� �Ѵ�... (Header�� �ִ� ������ @ModelAttribute�� �������� �ʴµ�)
		purchase.setTranNo(tranNo);
		purchase.setOrderDate(Date.valueOf(orderDate));
		service.updatePurchase(purchase);
		
		ModelAndView mv  = new ModelAndView("forward:/purchase/updatePurchase.jsp");
		mv.addObject("purchase", purchase);
		return mv;
	}
	
	
	/*
	/// admin�� client���� ��ǰ ���� ��
	@GetMapping("/updateTranCodeByProd")
	public ModelAndView updateTranCodeByProd(@RequestParam Integer prodNo, @RequestParam String tranCode) throws SQLException {
		// controller���� �ٸ� ��Ʈ�ѷ��� ȣ���� ���� �ִ�! (forward ����ϸ� ���� request ���� ����)
		ModelAndView mv = new ModelAndView("forward:/product/listProduct/manage");
		Purchase purchase = new Purchase();
		Product product =  new Product();
		// prod_no�� ������Ʈ�ϴ� ��� 
		if( prodNo != null ) {
			product.setProdNo(prodNo);
			purchase.setTranNo(-1);  // null�� �� �� ������ -1�� ��ü ǥ��
		} 
		// tran_no�� ������Ʈ�ϴ� ���
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
	
	/// user�� ��ǰ ������ ��
	/*
	 *  @RequestParam :: QueryString���� parsing�� ������ ������.
	 *  		required :: �ش� Query Parameter�� �ʼ��� �����ؾ� �ϴ°�? (default = true)
	 */
	@GetMapping("/updateTranCode")
	public ModelAndView updateTranCode(@RequestParam(required = false) Integer tranNo, @RequestParam(required = false) Integer prodNo) throws SQLException {
		ModelAndView mv = new ModelAndView("forward:/purchase/listPurchase");
		Purchase purchase = new Purchase();
		purchase.setTranCode("3");

		// prod_no�� ������Ʈ����, tran_no�� ������Ʈ���� Ȯ�� (�� �޼ҵ�� �����, ��ۿϷ� �� �� ó���Ϸ��� ��)
		Product product = new Product();
		if( tranNo != null ) {
			product.setProdNo(-1);
			purchase.setTranNo(tranNo);
		} else {
			product.setProdNo(prodNo);
			purchase.setTranNo(-1);  // null�� �� �� ������ -1�� ��ü ǥ��
		}
		purchase.setPurchaseProd(product);
		service.updateTranCode(purchase);
		
		return mv;
	}
	
	@GetMapping("/shoppingCart")
	public ModelAndView shoppingCart() throws Exception {
		
		
	}
	
}
