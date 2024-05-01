package com.model2.mvc.service.product.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

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
		if(dao.getImagesDao().insertImages(product.getImages()) == 1)
			return dao.getProductDao().insertProduct(product);
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
	
	public String saveImg(MultipartFile img, String imagePath, String fileName) throws Exception {
		
		File file = new File(imagePath + "/" + fileName);  // save할 file 경로 명시 (original file name까지 명시해야 함)
		img.transferTo(file);  // 해당 경로에 img를 transfer(?)

		if( !file.exists()) {
			System.out.println("img file이 저장되지 않았습니다...");
			return null;
		} else {
			return fileName;
		}
	}
}
