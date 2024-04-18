package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
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
	private ProductDao productDao;
	
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
		return productDao.insertProduct(product);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return productDao.findProduct(prodNo);
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", productDao.getProductList(search));
		result.put("totalCount", productDao.getTotalCount(search) );
		
		return result;
	}

	@Override
	public int updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		return productDao.updateProduct(product);
	}

	@Override
	public int deleteProduct(Integer prodNo) throws Exception {
		// TODO Auto-generated method stub
		return productDao.deleteProduct(prodNo);
	}
	
	// ProductRestController에서 사용
	@Override
	public Map<String, Object> getProductList(Search search, String option) throws Exception {

		if(option.equals("autocomplete")) {

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", productDao.getProductListAutoComplete(search.getSearchKeyword()) );
			
			return result;
		} else {
			
			return null;
		}
	}
}
