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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/// root WebApplicationContext에 저장된 properties 값 로드...
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
		
		// DB column 크기를 맞추기 위한 작업
		String[] temp = product.getManuDate().split("-");
		product.setManuDate(temp[0] + temp[1] + temp[2]);
		
		int result = service.addProduct(product);

		if(result != 1)
			// null을 넘김으로써 예외처리...
			return null;
		else
			// client가 자체적으로 view를 띄우기 위해서 product data를 그대로 넘겨줘야 함
			return product;
	}

	@GetMapping("/json/getProduct/{prodNo}/{menu}")
	public Map<String, Object> getProduct(@PathVariable Integer prodNo, @PathVariable String menu, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Product product = service.getProduct(prodNo);
		
		/// session 처리는 나중에...

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("menu", menu);
		jsonMap.put("product", product);
		
		return jsonMap;
	}

	/// left.jsp로 request할 때만
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
	JSON 객체 :: query string으로 올려 보냄  << URL에 의존적  @ModelAttribute
	JSON string :: body에 그대로 담겨서 보냄  @RequestBody
	 */
	// REST 통신에서 menu = search의 경우 무한 scroll 지원해야 함 
	@PostMapping("/json/listProduct/{menu}")
	public Map<String, Object> postListProduct(@RequestBody Search search, @PathVariable String menu) throws Exception {
		
		// 최초 접근 시 Query Parameter인 currentPage값이 null일 때 1페이지에서 시작하도록 설정
		if(search.getCurrentPage() == null || search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		Map<String, Object> map = service.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), (Integer) map.get("totalCount"),pageUnit, pageSize);
		
		//  1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		if( (search.getCurrentPage() > resultPage.getPageUnit() ) && !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() )
			resultPage.setBeginUnitPage(1);
		
		// 무한 scroll 구현을 위해 다음 page로 넘어갔다고 인지시켜야 함 :: REST
		// 어차피 form data는 classic http 통신을 하기 때문에, 기존 controller와 동기화 문제가 발생하지 않음.
		// 어차피 검색하면 1page로 static하게 회귀하기 때문에 all clear. 
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
			msg.setMsg("상품 수정에 실패...");
		else
			msg.setMsg("성공적으로 상품이 수정되었습니다!");
		
		return msg;
	}
	
	@PostMapping("/json/deleteProduct")
	public Message deleteProduct(@RequestBody Product product, HttpServletRequest request) throws Exception{
		
		System.out.println("path :: " + request.getServletContext().getRealPath("/images/uploadFiles") + "\\" + product.getFileName());
		File oldFile = new File( request.getServletContext().getRealPath("/images/uploadFiles") + "\\" + product.getFileName() );
		if (oldFile.exists()) {
			System.out.println("image file을 찾았습니다.");
			boolean b = oldFile.delete();  // 상품 제거 시 image file도 같이 삭제...
			if( !b) {
				System.out.println("image file 삭제에 실패...");
				return new Message("fail");
			} else {
				int result = service.deleteProduct( product.getProdNo());
				if(result != 1) 
					return new Message("상품 삭제에 실패... :: DB error");
				else
		 			return new Message("ok");
			}
		} else {
			return new Message("image file을 찾지 못했습니다...");
		}
	}
	
	/*
	 * produces :: server의 Content-Type 설정
	 * consumes :: server의 Accept 설정
	 */
	@PostMapping(path = "/checkInventory", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	// @RequestBody를 사용하여 text/plain 값을 받으려면 반드시 'String'으로 받아야 함.
	public String checkInventory(@RequestBody String prodNo) throws Exception {
		
		System.out.println(prodNo);
		Integer result = service.getProduct(Integer.parseInt(prodNo)).getInventory();
		
		// 재고 없으면 요청 거절
		if(result <= 0)
			return "0";
		else {
			System.out.println("flag");
			return String.valueOf(result);
		}
			
	}
}
