package com.model2.mvc.service.product.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import org.junit.Assertions;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertionsions;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


/*
 *	FileName :  ProductServiceTest.java
 * �� JUnit4 (Test Framework) �� Spring Framework ���� Test( Unit Test)
 * �� Spring �� JUnit 4�� ���� ���� Ŭ������ ���� ������ ��� ���� �׽�Ʈ �ڵ带 �ۼ� �� �� �ִ�.
 * �� @RunWith : Meta-data �� ���� wiring(����,DI) �� ��ü ����ü ����
 * �� @ContextConfiguration : Meta-data location ����
 * �� @Test : �׽�Ʈ ���� �ҽ� ����
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:config/context-*.xml" })
@SpringBootTest
public class ProductServiceTest {

	//==>@RunWith,@ContextConfiguration �̿� Wiring, Test �� instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	// @Test
	public void testAddProduct() throws Exception {
		
		Product product = new Product();
		product.setProdName("test");
		product.setProdDetail("test");
		product.setManuDate("43214321");
		product.setPrice(12345);
		product.setFileName("test");
		// product.setRegDate(null);
		
		productService.addProduct(product);
		
		Product result = productService.getProduct(12345);

		//==> API Ȯ��
		Assertions.assertEquals(product.getProdNo(), result.getProdNo());
		Assertions.assertEquals(product.getProdName(), result.getProdName());
		Assertions.assertEquals(product.getProdDetail(), result.getProdDetail());
		Assertions.assertEquals(product.getManuDate(), result.getManuDate());
		Assertions.assertEquals(product.getPrice(), result.getPrice());
		Assertions.assertEquals(product.getFileName(), result.getFileName());
	}
	
	@Test
	public void testGetProduct() throws Exception {
		Product product = productService.getProduct(10040);
		Assertions.assertEquals(Integer.valueOf(10040), product.getProdNo());
	}
	
	// @Test
	public void testGetProductList() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(5);
		search.setPriceMin(0);
		search.setPriceMax(2147483647);

		Map<String, Object> resultMap = productService.getProductList(search);
		@SuppressWarnings("unchecked")
		List<Product> list =  (List<Product>)resultMap.get("list");
		Assertions.assertEquals(5, list.size());
	}
	
	@Test
	public void testGetProductListByProdName() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(5);
		search.setPriceMin(0);
		search.setPriceMax(2147483647);
		String keyword = "update";
		search.setSearchKeyword(keyword);

		Map<String, Object> result = productService.getProductList(search);
		@SuppressWarnings("unchecked")
		List<Product> list =  (List<Product>)result.get("list");
		Assertions.assertEquals(1, list.size());
	}
	
	// @Test
	public void testUpdateProduct() throws Exception {
		Product product = new Product();
		product.setProdNo(12345);
		product.setProdName("update");
		product.setProdDetail("test");
		product.setManuDate("12341212");
		product.setPrice(12345);
		product.setFileName("test");
		
		Assertions.assertEquals(1, productService.updateProduct(product));
		
		Product updatedProduct = productService.getProduct(12345);
		Assertions.assertEquals(product.getProdName(), updatedProduct.getProdName());
	}
}