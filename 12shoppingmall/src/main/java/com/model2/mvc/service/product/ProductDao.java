package com.model2.mvc.service.product;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

// @Mapper
public interface ProductDao {
	public int insertProduct(Product product) throws Exception;
	
	public Product findProduct(int prodNo) throws Exception;
	
	public int findLatestProdId() throws Exception;
	
	public List<Product> getProductList(Search search) throws Exception;
	
	public List<Product> getProductListAutoComplete(String prodName) throws Exception;
	
	public int updateProduct(Product product) throws Exception;
	
	public int decreaseInventory(Product product) throws Exception;
	
	public int getTotalCount(Search search) throws Exception ;
	
	public int deleteProduct(Integer prodNo) throws Exception;	
}
