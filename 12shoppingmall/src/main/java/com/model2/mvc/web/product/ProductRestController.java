package com.model2.mvc.web.product;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Message;
import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RestController
@RequestMapping("/rest/product")
public class ProductRestController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService service;
	
	/// root WebApplicationContext�� ����� properties �� �ε�...
	// @Value("#{commonProperties['pageSize']}")
	@Value("${common.pageSize}")
	int pageSize;
	// @Value("#{commonProperties['pageUnit']}")
	@Value("${common.pageUnit}")
	int pageUnit;

	public ProductRestController() {
		// TODO Auto-generated constructor stub
	}

	@PostMapping("/json/addProduct")
	public Product addProduct(@RequestBody Product product ) throws Exception {
		
		// DB column ũ�⸦ ���߱� ���� �۾�
		String[] temp = product.getManuDate().split("-");
		product.setManuDate(temp[0] + temp[1] + temp[2]);
		
		int result = service.addProduct(product);

		if(result != 1)
			// null�� �ѱ����ν� ����ó��...
			return null;
		else
			// client�� ��ü������ view�� ���� ���ؼ� product data�� �״�� �Ѱ���� ��
			return product;
	}

	@GetMapping("/json/getProduct/{prodNo}/{menu}")
	public Map<String, Object> getProduct(@PathVariable Integer prodNo, @PathVariable String menu, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Product product = service.getProduct(prodNo);
		
		/// session ó���� ���߿�...

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("menu", menu);
		jsonMap.put("product", product);
		
		return jsonMap;
	}

	/// left.jsp�� request�� ����
	@GetMapping("/json/listProduct/{menu}")
	public Map<String, Object> getListProduct(@PathVariable String menu) throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(pageSize);
		Map<String, Object> map = service.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("list", map.get("list"));
		jsonMap.put("resultPage", resultPage);
		jsonMap.put("search", search);
		jsonMap.put("menu", menu);
		// jsonMap.put("title", "product");  << page navigator
		
		System.out.println("jsonMap" + jsonMap);
		
		return jsonMap;
	}
	
	/*
	JSON ��ü :: query string���� �÷� ����  << URL�� ������  @ModelAttribute
	JSON string :: body�� �״�� ��ܼ� ����  @RequestBody
	 */
	// REST ��ſ��� menu = search�� ��� ���� scroll �����ؾ� �� 
	@PostMapping("/json/listProduct/{menu}")
	public Map<String, Object> postListProduct(@RequestBody Search search, @PathVariable String menu) throws Exception {
		
		// ���� ���� �� Query Parameter�� currentPage���� null�� �� 1���������� �����ϵ��� ����
		if(search.getCurrentPage() == null || search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		Map<String, Object> map = service.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), (Integer) map.get("totalCount"),pageUnit, pageSize);
		
		//  1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
		if( (search.getCurrentPage() > resultPage.getPageUnit() ) && !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() )
			resultPage.setBeginUnitPage(1);
		
		// ���� scroll ������ ���� ���� page�� �Ѿ�ٰ� �������Ѿ� �� :: REST
		// ������ form data�� classic http ����� �ϱ� ������, ���� controller�� ����ȭ ������ �߻����� ����.
		// ������ �˻��ϸ� 1page�� static�ϰ� ȸ���ϱ� ������ all clear. 
		if(menu.equals("search"))
			resultPage.setCurrentPage( resultPage.getCurrentPage() + 1 );
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("list", map.get("list"));
		jsonMap.put("resultPage", resultPage);
		jsonMap.put("search", search);
		jsonMap.put("menu", menu);
		// jsonMap.put("title", "product");  << page navigator
		
		System.out.println("jsonMap" + jsonMap);
		
		return jsonMap;
	}
	
	@PostMapping("/json/listProduct/{menu}/autocomplete") 
	public Map<String, Object> postListProductAutoComplete(@RequestBody Search search, @PathVariable String menu) throws Exception {
		
		Map<String, Object> map = service.getProductList(search, "autocomplete");
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("list", map.get("list"));
		jsonMap.put("menu", menu);
		
		return jsonMap;
	}
	
	@PostMapping("/json/updateProduct")
	public Message updateProduct(@RequestBody Product product) throws Exception {
		
		int result = service.updateProduct(product);
		
		Message msg = new Message();
		
		if (result != 1)
			msg.setMsg("��ǰ ������ ����...");
		else
			msg.setMsg("���������� ��ǰ�� �����Ǿ����ϴ�!");
		
		return msg;
	}
	
	@PostMapping("/json/deleteProduct")
	public Message deleteProduct(@RequestBody Product product, HttpServletRequest request) throws Exception{
		
		if(product.getFileName().isEmpty()) {
			
			int result = service.deleteProduct( product.getProdNo());
			
			if(result != 1) {
				return new Message("��ǰ ������ ����... :: DB�� fileName�� ����Ǿ� ���� ����");
			} else {
				return new Message("ok");
			}
		}
		
		String rootPath = request.getServletContext().getRealPath("/");
		System.out.println(rootPath);
		
		File oldFile = new File( rootPath + "\\..\\resources\\static\\images\\uploadFiles\\" + product.getFileName() );
		if (oldFile.exists()) {
			System.out.println("image file�� ã�ҽ��ϴ�.");
			boolean b = oldFile.delete();  // ��ǰ ���� �� image file�� ���� ����...
			if( !b) {
				System.out.println("image file ������ ����...");
				return new Message("fail");
			} else {
				int result = service.deleteProduct( product.getProdNo());
				if(result != 1) {
					return new Message("��ǰ ������ ����... :: DB error");
				} else {
					return new Message("ok");
				}
			}
		} else {
			return new Message("image file�� ã�� ���߽��ϴ�...");
		}
	}
	
	/*
	 * produces :: server�� Content-Type ����
	 * consumes :: server�� Accept ����
	 */
	@PostMapping(path = "/checkInventory", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	// @RequestBody�� ����Ͽ� text/plain ���� �������� �ݵ�� 'String'���� �޾ƾ� ��.
	public String checkInventory(@RequestBody String prodNo) throws Exception {
		
		System.out.println(prodNo);
		Integer result = service.getProduct(Integer.parseInt(prodNo)).getInventory();
		
		// ��� ������ ��û ����
		if(result <= 0)
			return "0";
		else {
			System.out.println("flag");
			return String.valueOf(result);
		}
			
	}
}
