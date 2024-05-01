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
//�켱���� 1 : �޼ҵ忡 ������ @Transactional
//�켱���� 2 : Ŭ������ ������ @Transactional
//�켱���� 3 : �������̽��� ������ @Transactional
//======================== �߰��� �κ�  ==========================/
@Transactional  // transaction metadata �߰�
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	// @Qualifier("productDaoImpl")
	// @Qualifier("productDao")  // @Mapper interface�� ����ü�� ����.
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
	
	// ProductRestController���� ���
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
		
		String extention = "." + img.getOriginalFilename().split("\\.")[1];  // Ȯ���� :: '.' �� ����ǥ���� Ư�� �����̹Ƿ� �Ϲ� ���ڷ� ��ȯ����� �� 
		String fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;  // unique�� random name���� ���� :: ���� img name�� ���� �ߺ� ȸ��
		File file = new File(imagePath + "/" + fileName);  // save�� file ��� ��� (original file name���� ����ؾ� ��)
		/// unique�� file name�� ã�� ������ ������
		while( file.exists() ) {
			fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;
			file = new File(imagePath + "/" + fileName);  // ���� instance�� GC ����� ��⸦ ���
		}
		
		System.out.println(fileName);
		return fileName;
	}
	
	public String saveImg(MultipartFile img, String imagePath, String fileName) throws Exception {
		
		File file = new File(imagePath + "/" + fileName);  // save�� file ��� ��� (original file name���� ����ؾ� ��)
		img.transferTo(file);  // �ش� ��ο� img�� transfer(?)

		if( !file.exists()) {
			System.out.println("img file�� ������� �ʾҽ��ϴ�...");
			return null;
		} else {
			return fileName;
		}
	}
}
