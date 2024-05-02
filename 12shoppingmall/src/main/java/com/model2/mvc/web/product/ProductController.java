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
	
	/// root WebApplicationContext�� ����� properties �� �ε�...
	// @Value("#{commonProperties['pageSize']}")
	@Value("${common.pageSize}")
	int pageSize;
	// @Value("#{commonProperties['pageUnit']}")
	@Value("${common.pageUnit}")
	int pageUnit;
	
	private static final int unitKB = 1024;
	/*
	 *  file ��ο� ���Ͽ�
	 *  	������ :: origin���� �����η� �����ϴ� ���� �ƹ��� ������ ���� ����.
	 *  	����� :: �ܺο��� �����η� ���� ��������. ��� context root�κ����� ����� ������ ����.
	 */
	// private static final String relativeImagePath = "\\images\\uploadFiles\\";
	// private static final String imagePath = "\\images\\uploadFiles\\";  // context root�κ��� path�� ������ ���� �̽��� �߻����� �ʴ´�... (C:\ ��� �����η� �����ϸ� local ���ٿ� ���� ���� �̽��� ���� )
	public ProductController() {
		// TODO Auto-generated constructor stub
		System.out.println("���� Controller Bean load...");
	}
	
	/*
	 *  @ModelAttribute�� HttpServletRequest�� getParameter()�� Ȱ���Ͽ� auto binding�� ���ִ� annotation�̴�.
	 *  	query parameter?
	 *  	GET :: url ���� query string
	 *  	POST :: body ���� query string  
	 */
	/*
	@PostMapping("/addProduct")
	public String addProduct(@ModelAttribute Product  product, Model model) throws Exception {
		String[] temp = product.getManuDate().split("-");
		product.setManuDate(temp[0] + temp[1] + temp[2]);  // manufacture_day �Է� ���(custom) :: yyyymmdd
		service.addProduct(product);
		model.addAttribute("product", product);
		return "forward:/product/addProduct.jsp";
	}
	*/

	/// file upload
	/*
	 *  enctype = 'multipart/form-data'
	 *  consumes(Content-Type)���� �����ϰ� ��������� �� 
	 *  @RequestParam, @ModelAttribute :: form data�� � ���̵� parameter�� ���... 
	 *  MultipartHttpServletRequest :: multipartResolver ��� �� MultipartHttpServletRequest�� ��ȯ�Ǿ� ����. up casting �ؼ� ��� ����
	 */
	@PostMapping(value = "/addProduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public String addProduct(@ModelAttribute Product product, @RequestParam(name="thumbnail", required=false) MultipartFile thumbnail,
			@RequestParam(name="productImages",required=false) MultipartFile[] images, Model model, HttpServletRequest request) throws Exception {

		// real path�� �����´�.
		// Spring boot ���� �� static�� �����ϱ� ���ؼ��� webapp���� ����� �Ѵ�.  << ���� server������ ��ȿ�� �� ������ ����
		String imagePath = request.getServletContext().getRealPath("/");
		imagePath = imagePath + "../resources/static/images/uploadFiles";
		System.out.println("path :: " + imagePath);

		/// ����ڰ� image�� ���� �ʴ� ���,  ���� file�� ��ü�Ͽ� ����
		if(thumbnail.isEmpty())  {
			product.setFileName(null);
		} 
		/// image�� �����ϴ� ���
		else {			
			/// thumbnail ũ�� ����
			if(thumbnail.getSize() > unitKB * unitKB * 10)
				System.out.println("10MB ������ �̹����� �����մϴ�...");
			else {
				product.setFileName( service.generateRandomName(thumbnail, imagePath) );
			}
		}
		
		// ����ڰ� ���� ��ǰ ���� image���� ���������� ����
		List<String> tempName = new ArrayList<String>();
		for(MultipartFile img : images) {
			tempName.add( service.generateRandomName(img, imagePath) ); 
		}
		// builder �ᵵ ������ �ϴ� �̷��� ��
		Images productImages = new Images();
		productImages.setProdNo(product.getProdNo());
		productImages.setImg1(tempName.get(0));
		productImages.setImg2(tempName.get(1));
		productImages.setImg3(tempName.get(2));
		product.setImages(productImages);
		
		StringTokenizer temp = new StringTokenizer( product.getManuDate(), "-" );  // delim �־���� split����
		product.setManuDate( temp.nextToken() + temp.nextToken() + temp.nextToken() );	
		
		int result = service.addProduct(product);  // id�� sequence�� ���� auto increment
		if(result == -1) {
			System.out.println("DB ���� �߻�. add �ȵ���.");
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
	
	// HandlerAdapter���� proxy��ü ���·� HttpServletRequest�� HttpServletResponse ��ü�� �Ѱ���
	@GetMapping("/getProduct/{menu}")
	public String getProduct(@RequestParam Integer prodNo, @PathVariable String menu, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws Exception {
		
		Product product = service.getProduct(prodNo);

		model.addAttribute("product", product);

		System.out.println("client���� cookie(history, ��ǰ��.��ǰ��ȣ)�� �����մϴ�.");
		
		/// history ��� ����
		service.addHistory(request, response, product);
		
		System.out.println(product.getFileName());
		/// user�� ��ǰ �˻����� navigation ó��
		if(menu.equals("search")) {
			return "forward:/product/getProduct.jsp?menu=search";
		} 
		/// admin�� ��ǰ �������� navigation
		else {  
			return "forward:/product/updateProduct";
		}	
	}

	// list������ ��ȸ �� �˻��� ��� ����.
	/*
	 *  @RequestParam :: requestObject���� �ε�
	 *  @ModelAttribute :: requestObject �� QueryString�� ��� �ε�  >> ��, domain ��ü�� Querystring�� �ε� �ȵǴ���.
	 *  		binding :: domain ��ü�� auto binding�� �� ������ ����. (default = true)
	 *  @PathVariable :: ��𿡵� load���� �ʴ´�...
	 */
	@RequestMapping("/listProduct/{menu}")
	public String listProduct(@ModelAttribute(binding=true) Search search, @PathVariable String menu, Model model, HttpServletRequest request) throws Exception {
		
		// ���� ���� �� Query Parameter�� currentPage���� null�� �� 1���������� �����ϵ��� ����
		if(search.getCurrentPage() == null ||  search.getCurrentPage() == 0)
			search.setCurrentPage(1);
		// 1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
		else if( !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() && search.getCurrentPage() != 1 )
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		// priceDESC null string ���� �� bug �ذ� (null check ���ϸ� isEmpty()���� null pointer error)
		if( search.getPriceDESC() != null && search.getPriceDESC().isEmpty())
			search.setPriceDESC(null);
		
		Map<String, Object> map = service.getProductList(search);
		
		Page myPage = new Page(search.getCurrentPage(), (Integer) map.get("totalCount"),pageUnit, pageSize);
		
		//  1������ ���Ŀ��� �˻� �� 1���������� ������ϵ��� ����
		if( (search.getCurrentPage() > myPage.getPageUnit() ) && !CommonUtil.null2str(search.getSearchKeyword()).isEmpty() )
			myPage.setBeginUnitPage(1);
		
		model.addAttribute("search", search);  // �˻� ���� ������ ���� requestScope�� ���� �Ѱ���...
		model.addAttribute("list", map.get("list") );
		model.addAttribute("resultPage", myPage);
		model.addAttribute("menu", menu);
		model.addAttribute("title", "product");
		
		// ������ ��忡 ���� ���� Query String�� ����
		if(menu.contains("search"))
			return "forward:/product/listProduct.jsp?menu=search";  
		else
			return "forward:/product/listProduct.jsp?menu=manage";
	}
	
	@GetMapping("/updateProduct")
	public String updateProduct(@RequestParam Integer prodNo, Model model) throws Exception {
		Product product =  service.getProduct(prodNo);

		// substring() :: python�� list�� ������ begin ~ end ���
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
		product.setFileName(oldFileName);  // img ��������� ������ ���� ���� �״�� ä��, img �־ ���� �̸����� �����ؾ� ��.
		
		

		StringTokenizer temp = new StringTokenizer( product.getManuDate(), "-" );  // delim �־���� split����
		product.setManuDate( temp.nextToken() + temp.nextToken() + temp.nextToken() );
		service.updateProduct(product);

		
		return "forward:/product/updateProduct.jsp";
	}
}