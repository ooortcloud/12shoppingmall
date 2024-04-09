package com.model2.mvc.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.ProductService;

@Controller
public class RootController {

	public RootController() {
		System.out.println("RootController instance generated...!!!!!");
	}
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService service;
	
	/// root WebApplicationContext�� ����� properties �� �ε�...
	// @Value("#{commonProperties['pageSize']}")
	@Value("${common.pageSize}")
	int pageSize;	
	
	/// ���� welcome-page�� �����ϱ� ���� ����...
	// welcome-page�� �̸��� 'index'�� �ϸ� Spring ���ο��� controller�� �����ϰ� �ش� view�� �ɾƹ���...
	@GetMapping("/")
	public String listProduct(Model model, HttpServletRequest request) throws Exception {
		
		System.out.println("flag");
		
		// �ֽ� list�� �������� �������� ���� �ּ����� ������ ����
		Search search = new Search();
		search.setPageSize(pageSize);
		search.setCurrentPage(1);
		Map<String, Object> map = service.getProductList(search);

		model.addAttribute("list", map.get("list") );

		// welcome-page�� forward
		HttpSession session = request.getSession();
		if(session == null || session.getAttribute("user") == null )  // login���� ���� ����ڴ� ����ó��
			// 'index.jsp'�� view �̸��� ������ Controller �����ϰ� �ٷ� ����Ź���...
			return "forward:/not_login_index.jsp";
		else
			return "forward:/main.jsp";  
	}
	
}
