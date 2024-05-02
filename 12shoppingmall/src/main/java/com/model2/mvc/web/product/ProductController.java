package com.model2.mvc.web.product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.service.domain.Images;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
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
	
	private static final int unitKB = 1024;
	/*
	 *  file 경로에 대하여
	 *  	절대경로 :: origin에서 절대경로로 접근하는 것은 아무런 문제가 되지 않음.
	 *  	상대경로 :: 외부에서 절대경로로 접근 막혀있음. 대신 context root로부터의 상대경로 접근은 허용됨.
	 */
	// private static final String relativeImagePath = "\\images\\uploadFiles\\";
	// private static final String imagePath = "\\images\\uploadFiles\\";  // context root로부터 path를 잡아줘야 보안 이슈가 발생하지 않는다... (C:\ 기반 절대경로로 설정하면 local 접근에 대한 보안 이슈로 차단 )
	public ProductController() {
		// TODO Auto-generated constructor stub
		System.out.println("각종 Controller Bean load...");
	}
	
	/*
	 *  @ModelAttribute는 HttpServletRequest의 getParameter()를 활용하여 auto binding을 해주는 annotation이다.
	 *  	query parameter?
	 *  	GET :: url 내의 query string
	 *  	POST :: body 내의 query string  
	 */
	/*
	@PostMapping("/addProduct")
	public String addProduct(@ModelAttribute Product  product, Model model) throws Exception {
		String[] temp = product.getManuDate().split("-");
		product.setManuDate(temp[0] + temp[1] + temp[2]);  // manufacture_day 입력 양식(custom) :: yyyymmdd
		service.addProduct(product);
		model.addAttribute("product", product);
		return "forward:/product/addProduct.jsp";
	}
	*/

	/// file upload
	/*
	 *  enctype = 'multipart/form-data'
	 *  consumes(Content-Type)에서 동일하게 설정해줘야 함 
	 *  @RequestParam, @ModelAttribute :: form data는 어떤 것이든 parameter로 취급... 
	 *  MultipartHttpServletRequest :: multipartResolver 사용 시 MultipartHttpServletRequest로 변환되어 사용됨. up casting 해서 사용 가능
	 */
	@PostMapping(value = "/addProduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public String addProduct(@ModelAttribute Product product, @RequestParam(name="thumbnail", required=false) MultipartFile thumbnail,
			@RequestParam(name="productImages",required=false) MultipartFile[] images, Model model, HttpServletRequest request) throws Exception {

		// real path를 가져온다.
		// Spring boot 변경 후 static에 접근하기 위해서는 webapp에서 벗어나야 한다.  << 개발 server에서만 유효할 수 있으니 주의
		String imagePath = request.getServletContext().getRealPath("/");
		imagePath = imagePath + "../resources/static/images/uploadFiles";
		System.out.println("path :: " + imagePath);

		/// 사용자가 image를 넣지 않는 경우,  예외 file로 대체하여 저장
		if(thumbnail.isEmpty())  {
			product.setFileName(null);
		} 
		/// image가 존재하는 경우
		else {			
			/// thumbnail 크기 제한
			if(thumbnail.getSize() > unitKB * unitKB * 10)
				System.out.println("10MB 이하의 이미지만 가능합니다...");
			else {
				product.setFileName( service.generateRandomName(thumbnail, imagePath) );
			}
		}
		
		// 사용자가 넣은 상품 설명 image들을 순차적으로 저장
		List<String> tempName = new ArrayList<String>();
		for(MultipartFile img : images) {
			tempName.add( service.generateRandomName(img, imagePath) ); 
		}
		// builder 써도 됐지만 일단 이렇게 함
		Images productImages = new Images();
		productImages.setProdNo(product.getProdNo());
		productImages.setImg1(tempName.get(0));
		productImages.setImg2(tempName.get(1));
		productImages.setImg3(tempName.get(2));
		product.setImages(productImages);
		
		StringTokenizer temp = new StringTokenizer( product.getManuDate(), "-" );  // delim 넣어줘야 split해줌
		product.setManuDate( temp.nextToken() + temp.nextToken() + temp.nextToken() );	
		
		int result = service.addProduct(product);  // id는 sequence에 의해 auto increment
		if(result == -1) {
			System.out.println("DB 문제 발생. add 안됐음.");
		} else {
			service.saveImg(thumbnail, imagePath, product.getFileName());
			service.saveImg(images[0], imagePath, product.getImages().getImg1());
			service.saveImg(images[1], imagePath, product.getImages().getImg2());
			service.saveImg(images[2], imagePath, product.getImages().getImg3());
		}
		
		model.addAttribute("product", product);  // setter...
		
		return "forward:/product/addProduct.jsp";
	}
	
	


	@GetMapping("/addProduct")
	public String addProduct(Model model) {
		return "forward:/product/addProductView.jsp";
	}
	
	// HandlerAdapter에서 proxy객체 형태로 HttpServletRequest와 HttpServletResponse 객체를 넘겨줌
	@GetMapping("/getProduct/{menu}")
	public String getProduct(@RequestParam Integer prodNo, @PathVariable String menu, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws Exception {
		
		Product product = service.getProduct(prodNo);

		model.addAttribute("product", product);

		System.out.println("client에게 cookie(history, 상품명.상품번호)를 제공합니다.");
		
		/// history 등록 로직
		service.addHistory(request, response, product);
		
		System.out.println(product.getFileName());
		/// user는 상품 검색으로 navigation 처리
		if(menu.equals("search")) {
			return "forward:/product/getProduct.jsp?menu=search";
		} 
		/// admin은 상품 수정으로 navigation
		else {  
			return "forward:/product/updateProduct";
		}	
	}

	// list에서는 조회 및 검색이 모두 사용됨.
	/*
	 *  @RequestParam :: requestObject에만 로드
	 *  @ModelAttribute :: requestObject 및 QueryString에 모두 로드  >> 단, domain 객체는 Querystring에 로드 안되더라.
	 *  		binding :: domain 객체에 auto binding을 할 것인지 여부. (default = true)
	 *  @PathVariable :: 어디에도 load하지 않는다...
	 */
	@RequestMapping("/listProduct/{menu}")
	public String listProduct(@ModelAttribute(binding=true) Search search, @PathVariable String menu, Model model, HttpServletRequest request) throws Exception {
		
		// 최초 접근 시 Query Parameter인 currentPage값이 null일 때 1페이지에서 시작하도록 설정
		if(search.getCurrentPage() == null ||  search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		// priceDESC null string 들어올 때 bug 해결 (null check 안하면 isEmpty()에서 null pointer error)
		if( search.getPriceDESC() != null && search.getPriceDESC().isEmpty())
			search.setPriceDESC(null);
		
		Map<String, Object> map = service.getProductList(search);
		
		Page myPage = new Page(search.getCurrentPage(), (Integer) map.get("totalCount"),pageUnit, pageSize);
		
		//  1페이지 이후에서 검색 시 1페이지에서 재시작하도록 설정
		if( (search.getCurrentPage() > myPage.getPageUnit() ) && !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() )
			myPage.setBeginUnitPage(1);
		
		model.addAttribute("search", search);  // 검색 조건 유지를 위해 requestScope에 같이 넘겨줌...
		model.addAttribute("list", map.get("list") );
		model.addAttribute("resultPage", myPage);
		model.addAttribute("menu", menu);
		model.addAttribute("title", "product");
		
		// 페이지 모드에 따라서 리턴 Query String을 정의
		if(menu.contains("search"))
			return "forward:/product/listProduct.jsp?menu=search";  
		else
			return "forward:/product/listProduct.jsp?menu=manage";
	}
	
	@GetMapping("/updateProduct")
	public String updateProduct(@RequestParam Integer prodNo, Model model) throws Exception {
		Product product =  service.getProduct(prodNo);

		// substring() :: python의 list와 동일한 begin ~ end 방식
		String year = product.getManuDate().substring(0, 4);
		String month = product.getManuDate().substring(4, 6);
		String day = product.getManuDate().substring(6, 8);
		product.setManuDate(year + "-" + month + "-" + day);
		System.out.println("end :: "+product.getManuDate());

		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	/*
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product) throws Exception {
		service.updateProduct(product);
		return "forward:/product/updateProduct.jsp";
	}
	*/
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam(name="thumbnail", required=false) MultipartFile thumbnail,
			@RequestParam(name="productImages",required=false) MultipartFile[] images, Model model, HttpServletRequest request) throws Exception {

		String oldFileName = service.getProduct(product.getProdNo()).getFileName();
		service.updateImg(thumbnail, oldFileName, request);
		product.setFileName(oldFileName);  // img 변경사항이 없으면 기존 것을 그대로 채용, img 있어도 기존 이름으로 변경해야 함.
		
		

		StringTokenizer temp = new StringTokenizer( product.getManuDate(), "-" );  // delim 넣어줘야 split해줌
		product.setManuDate( temp.nextToken() + temp.nextToken() + temp.nextToken() );
		service.updateProduct(product);

		
		return "forward:/product/updateProduct.jsp";
	}
}