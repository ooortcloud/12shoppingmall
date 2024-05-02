package com.model2.mvc.service.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;


public interface ProductService {
	
	public int addProduct(Product product) throws Exception;
	
	public Product getProduct(int prodNo) throws Exception;
	
	public Map<String, Object> getProductList(Search search) throws Exception;
	
	public Map<String, Object> getProductList(Search search, String option) throws Exception;
	
	public int updateProduct(Product product) throws Exception;	
	
	public int deleteProduct(Integer prodNo) throws Exception;
	
	//////////////////////////
	// utility
	//////////////////////////
	
	public String generateRandomName(MultipartFile img, String imagePath) throws Exception;
	
	public String saveImg(MultipartFile img, String imagePath, String fileName) throws Exception;
	
	public void addHistory(HttpServletRequest request, HttpServletResponse response, Product product);
	
	public void updateImg(MultipartFile img, String oldFileName, HttpServletRequest request) throws Exception;
}