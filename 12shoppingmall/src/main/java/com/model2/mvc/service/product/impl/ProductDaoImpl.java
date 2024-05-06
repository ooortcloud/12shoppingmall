package com.model2.mvc.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;


@Repository
public class ProductDaoImpl implements ProductDao {

	@Override
	public int insertProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Product findProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findLatestProdId() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Product> getProductList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductListAutoComplete(String prodName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int decreaseInventory(Product product) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCount(Search search) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteProduct(Integer prodNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
