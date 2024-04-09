package com.model2.mvc.service.product.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;

// annotation ��� metadata �ۼ����� ���� ������, ������ �����ο� ���� DI metadata ���� ���ϱ��� ���� �����ؾ߸� ��...
@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession; 

	public ProductDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	*/

	/*
	 *  ���� JDBC ��� DAO ���� ���� ���� ::
	 *  1. Connection Pool�κ��� Connection instance ������
	 *  2. sql ���� ����
	 *  3. connection instance�κ��� �ش� sql ������ ���� PreparedStatement instance ����. '?'�� ���� ���ڵ� setter�� ����
	 *  4.  preparedStatement instance�� execute ���� �޼ҵ带 ����Ͽ� DB�� ���� �߻�
	 *  5. DB�� ���ϰ��� �޾Ƽ� ó�� (ResultSet�� ��� VO��ü�� ���� �����ؼ� ������)
	 */
	public int insertProduct(Product product) throws Exception {
		// Mybatis framework ��� �� DAO ���� ���� :: 
		// SqlSession instance�� crud �޼ҵ� ȣ��. �ʿ信 ���� �޾ƿ� �� �״�� ����. -��-
		// SqlSession���� metadata���� ������ preparedStatement ����� sql ������� ���� �ε��ϰ� �ֱ� ������ �̷� �÷��̰� ��������.
		return sqlSession.insert("ProductMapper.addProduct", product);
	}

	public Product findProduct(int prodNo) throws Exception {
		return sqlSession.selectOne("ProductMapper.findProduct", prodNo);
	}

	public List<Product> getProductList(Search search) throws Exception {
		return sqlSession.selectList("ProductMapper.getProductList", search);
	}

	public int updateProduct(Product product) throws Exception {
		return sqlSession.update("ProductMapper.updateProduct", product);
	}

	@Override
	public int getTotalCount(Search search) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}

	@Override
	public int deleteProduct(Integer prodNo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.delete("ProductMapper.deleteProduct", prodNo);
	}

	@Override
	public List<Product> getProductListAutoComplete(String prodName) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("ProductMapper.getProductListAutoComplete", prodName);
	}
}
