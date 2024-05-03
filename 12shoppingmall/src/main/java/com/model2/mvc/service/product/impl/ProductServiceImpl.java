package com.model2.mvc.service.product.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Images;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductAllDao;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
//=============================================================/
//우선순위 1 : 메소드에 설정된 @Transactional
//우선순위 2 : 클래스에 설정된 @Transactional
//우선순위 3 : 인터페이스에 설정된 @Transactional
//======================== 추가된 부분  ==========================/
@Transactional  // transaction metadata 추가
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	// @Qualifier("productDaoImpl")
	// @Qualifier("productDao")  // @Mapper interface의 구현체를 받자.
	// private ProductDao productDao;
	private ProductAllDao dao;
	
	/*
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	 */
	
	public ProductServiceImpl() {
	}

	@Override
	public int addProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		
		if(dao.getProductDao().insertProduct(product) == 1) {
			
			Images temp = product.getImages();
			temp.setProdNo( dao.getProductDao().findLatestProdId() );
			return dao.getImagesDao().insertImages( product.getImages() );
		}
		else
			return -1;
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return dao.getProductDao().findProduct(prodNo);
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", dao.getProductDao().getProductList(search));
		result.put("totalCount", dao.getProductDao().getTotalCount(search) );
		
		return result;
	}

	@Override
	public int updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		return dao.getProductDao().updateProduct(product);
	}

	@Override
	public int deleteProduct(Integer prodNo) throws Exception {
		// TODO Auto-generated method stub
		return dao.getProductDao().deleteProduct(prodNo);
	}
	
	// ProductRestController에서 사용
	@Override
	public Map<String, Object> getProductList(Search search, String option) throws Exception {

		if(option.equals("autocomplete")) {

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", dao.getProductDao().getProductListAutoComplete(search.getSearchKeyword()) );
			
			return result;
		} else {
			
			return null;
		}
	}
	
	public int addProductImages(Images productImages) throws Exception {
		
		return dao.getImagesDao().insertImages(productImages);
	}
	
	//======================================================
	// utility
	//======================================================
	
	public String generateRandomName(MultipartFile img, String imagePath) throws Exception {
		
		System.out.println("getOriginalFilename() :: " + img.getOriginalFilename());
		
		String extention = "." + img.getOriginalFilename().split("\\.")[1];  // 확장자 :: '.' 은 정규표현식 특수 문자이므로 일반 문자로 전환해줘야 함 
		String fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;  // unique한 random name으로 저장 :: 동일 img name에 대한 중복 회피
		File file = new File(imagePath + "/" + fileName);  // save할 file 경로 명시 (original file name까지 명시해야 함)
		/// unique한 file name을 찾을 때까지 돌리기
		while( file.exists() ) {
			fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;
			file = new File(imagePath + "/" + fileName);  // 기존 instance는 GC 대상의 대기를 기대
		}
		
		System.out.println(fileName);
		return fileName;
	}
	 
	public File saveImg(MultipartFile img, String newFileName, String imagePath) throws Exception {
		
		// real path를 가져온다.
		// Spring boot 변경 후 static에 접근하기 위해서는 webapp에서 벗어나야 한다.  << 개발 server에서만 유효할 수 있으니 주의
		// save할 file 경로 명시 (original file name까지 명시해야 함)
		File file = new File( imagePath + newFileName );
		
		System.out.println( imagePath + newFileName);
		img.transferTo(file);  // 해당 경로에 img를 transfer(?)

		if( !file.exists()) {
			System.out.println("img file이 저장되지 않았습니다...");
			return null;
		} else {
			return file;
		}
	}
	
	public void addHistory(HttpServletRequest request, HttpServletResponse response, Product product) {
		
		int prodNo = product.getProdNo();
		
		Cookie[] cookies = request.getCookies();
		boolean flag = true;  // 중복 체크
		boolean first = true; // 중복 체크 로직에서 첫번째 경우 예외 처리
		String historyCookie = "";  // historyNo 조회 도중 history가 지나가면 안되니까 데이터 따로 빼둠
		String historyNo = "";  // 상품ID만 기록
		String histories = "";  // 상품ID와 상품명 함께 기록  << 상품명이 동일한 상품에 대한 중복 검사를 하기 위함
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("historyNo")) {
				first=false;
				String[] searchArr = cookie.getValue().split("-");
				// prodNo 중복 검사  -- 같은 이름이어도 prodNo가 다르면 서로 다르니까 번호로 비교
				for (String searchItemNo : searchArr) {
					if(searchItemNo.equals(String.valueOf(prodNo))) {
						flag=false;
						break;
					}
				}
				// 중복 없을 경우 history에 추가
				if(flag) {
					// 일부 특수 문자만 쿠키에 사용할 수 있습니다. 예를 들면 하이픈(-), 언더스코어(_), 마침표(.) 등이 있습니다.
					// 상품명.상품번호-상품명.상품번호 ~~~
					historyNo = cookie.getValue()+"-" + String.valueOf(prodNo);
					Cookie historyNoCookie = new Cookie("historyNo", historyNo);
					historyNoCookie.setPath("/");
					response.addCookie( historyNoCookie );
					System.out.println("저장된 historyNo : "+historyNo);
				} else {
					System.out.println("이미 검색한 상품입니다.");
				}
			} else if (cookie.getName().equals("histories")) {
				historyCookie = cookie.getValue();
				System.out.println("historyCookie = " + historyCookie);
			}
		}
		
		// cookie에 공백을 넣을 수 없으므로, 공백을 필터링해서 작업하기
		String[] temp = product.getProdName().trim().split(" ");
		String prodName = "";
		for (String t : temp)
			prodName += (t + "_");
		if(flag & !first) {  // 반복문 밖에서 중복이 아니면 추가해줌
			String[] searchArr = historyCookie.split("-");
			// 일부 특수 문자만 쿠키에 사용할 수 있습니다. 예를 들면 하이픈(-), 언더스코어(_), 마침표(.) 등이 있습니다.
			histories = historyCookie+"-" + prodName+ "."+ String.valueOf(prodNo);
			Cookie historiesCookie = new Cookie("histories", histories);
			historiesCookie.setPath("/");
			response.addCookie( historiesCookie );
			System.out.println("저장된 histories : "+ histories );
		}
		
		if(first) {  // 최초 조회면 쿠키를 만듦
			// addCookie를 통해 생성한 기본 cookie 수명 = -1  :: client가 browser 종료 시 자동 삭제
			histories = prodName + "."+ String.valueOf(prodNo);
			historyNo = String.valueOf(prodNo);
			Cookie historiesCookie =  new Cookie("histories", histories);
			historiesCookie.setPath("/");
			response.addCookie( historiesCookie );					
			Cookie historyNoCookie = new Cookie("historyNo", historyNo);
			historyNoCookie.setPath("/");
			response.addCookie( historyNoCookie );	
			System.out.println("history 쿠키가 없어서 새로 생성했습니다.");
		}
	}
	
	public void updateImg(Product product, MultipartFile thumbnail, MultipartFile[] images, HttpServletRequest request) throws Exception {

		String imagePath = request.getServletContext().getRealPath("/") + "../resources/static/images/uploadFiles/";
		updateIfThumbnailRegistered(thumbnail, product, imagePath);
		
		Images parameter = product.getImages();
		String oldFileName = "";
		if( !images[0].isEmpty() ) {
			
			oldFileName = this.getProduct(product.getProdNo()).getImages().getImg1();
			String oldRandomName = oldFileName.split("\\.")[0];
			String extention = "." + images[0].getOriginalFilename().split("\\.")[1];
			String newFileName = oldRandomName + extention;
			System.out.println("old : " + oldFileName);
			System.out.println("new : " + newFileName);
			
			parameter.setImg1(newFileName);
			parameter.setProdNo(product.getProdNo());
			int dbResult = dao.getImagesDao().updateImages(parameter);
			
			if(dbResult == 1) {
				boolean result = new File(imagePath + oldFileName).delete();
				System.out.println(result); 
				saveImg(images[0], newFileName, imagePath);	
			}
		}
		
		if( !images[1].isEmpty() ) {
			
			oldFileName = this.getProduct(product.getProdNo()).getImages().getImg2();
			String oldRandomName = oldFileName.split("\\.")[0];
			String extention = "." + images[1].getOriginalFilename().split("\\.")[1];
			String newFileName = oldRandomName + extention;
			System.out.println("old : " + oldFileName);
			System.out.println("new : " + newFileName);
			
			parameter.setImg2(newFileName);
			parameter.setProdNo(product.getProdNo());
			int dbResult = dao.getImagesDao().updateImages(parameter);
			
			if(dbResult == 1) {
				boolean result = new File(imagePath + oldFileName).delete();
				System.out.println(result); 
				saveImg(images[1], newFileName, imagePath);	
			}
		}
		
		if( !images[2].isEmpty() ) {
			
			oldFileName = this.getProduct(product.getProdNo()).getImages().getImg3();
			String oldRandomName = oldFileName.split("\\.")[0];
			String extention = "." + images[2].getOriginalFilename().split("\\.")[1];
			String newFileName = oldRandomName + extention;
			System.out.println("old : " + oldFileName);
			System.out.println("new : " + newFileName);
			
			parameter.setImg3(newFileName);
			parameter.setProdNo(product.getProdNo());
			int dbResult = dao.getImagesDao().updateImages(parameter);
			
			if(dbResult == 1) {
				boolean result = new File(imagePath + oldFileName).delete();
				System.out.println(result); 
				saveImg(images[2], newFileName, imagePath);	
			}
		}
		
		
	}

	private void updateIfThumbnailRegistered(MultipartFile img, Product product, String imagePath) throws Exception {
		
		/// user가 img를 변경했다면?
		if( !img.isEmpty() ) {
			
			String temp = this.getProduct(product.getProdNo()).getFileName().split("\\.")[0];
			String extention = "." + img.getOriginalFilename().split("\\.")[1];  // 확장자 :: '.' 은 정규표현식 특수 문자이므로 일반 문자로 전환해줘야 함
			String oldFileName = temp + extention;
			
			new File(imagePath + product.getFileName()).delete();
			this.saveImg(img, oldFileName, imagePath);
			product.setFileName(oldFileName);
		}
	}


}
