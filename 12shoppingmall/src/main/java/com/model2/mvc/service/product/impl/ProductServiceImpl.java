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
	 
	public File saveImg(MultipartFile img, String newFileName, String imagePath) throws Exception {
		
		// real path�� �����´�.
		// Spring boot ���� �� static�� �����ϱ� ���ؼ��� webapp���� ����� �Ѵ�.  << ���� server������ ��ȿ�� �� ������ ����
		// save�� file ��� ��� (original file name���� ����ؾ� ��)
		File file = new File( imagePath + newFileName );
		
		System.out.println( imagePath + newFileName);
		img.transferTo(file);  // �ش� ��ο� img�� transfer(?)

		if( !file.exists()) {
			System.out.println("img file�� ������� �ʾҽ��ϴ�...");
			return null;
		} else {
			return file;
		}
	}
	
	public void addHistory(HttpServletRequest request, HttpServletResponse response, Product product) {
		
		int prodNo = product.getProdNo();
		
		Cookie[] cookies = request.getCookies();
		boolean flag = true;  // �ߺ� üũ
		boolean first = true; // �ߺ� üũ �������� ù��° ��� ���� ó��
		String historyCookie = "";  // historyNo ��ȸ ���� history�� �������� �ȵǴϱ� ������ ���� ����
		String historyNo = "";  // ��ǰID�� ���
		String histories = "";  // ��ǰID�� ��ǰ�� �Բ� ���  << ��ǰ���� ������ ��ǰ�� ���� �ߺ� �˻縦 �ϱ� ����
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("historyNo")) {
				first=false;
				String[] searchArr = cookie.getValue().split("-");
				// prodNo �ߺ� �˻�  -- ���� �̸��̾ prodNo�� �ٸ��� ���� �ٸ��ϱ� ��ȣ�� ��
				for (String searchItemNo : searchArr) {
					if(searchItemNo.equals(String.valueOf(prodNo))) {
						flag=false;
						break;
					}
				}
				// �ߺ� ���� ��� history�� �߰�
				if(flag) {
					// �Ϻ� Ư�� ���ڸ� ��Ű�� ����� �� �ֽ��ϴ�. ���� ��� ������(-), ������ھ�(_), ��ħǥ(.) ���� �ֽ��ϴ�.
					// ��ǰ��.��ǰ��ȣ-��ǰ��.��ǰ��ȣ ~~~
					historyNo = cookie.getValue()+"-" + String.valueOf(prodNo);
					Cookie historyNoCookie = new Cookie("historyNo", historyNo);
					historyNoCookie.setPath("/");
					response.addCookie( historyNoCookie );
					System.out.println("����� historyNo : "+historyNo);
				} else {
					System.out.println("�̹� �˻��� ��ǰ�Դϴ�.");
				}
			} else if (cookie.getName().equals("histories")) {
				historyCookie = cookie.getValue();
				System.out.println("historyCookie = " + historyCookie);
			}
		}
		
		// cookie�� ������ ���� �� �����Ƿ�, ������ ���͸��ؼ� �۾��ϱ�
		String[] temp = product.getProdName().trim().split(" ");
		String prodName = "";
		for (String t : temp)
			prodName += (t + "_");
		if(flag & !first) {  // �ݺ��� �ۿ��� �ߺ��� �ƴϸ� �߰�����
			String[] searchArr = historyCookie.split("-");
			// �Ϻ� Ư�� ���ڸ� ��Ű�� ����� �� �ֽ��ϴ�. ���� ��� ������(-), ������ھ�(_), ��ħǥ(.) ���� �ֽ��ϴ�.
			histories = historyCookie+"-" + prodName+ "."+ String.valueOf(prodNo);
			Cookie historiesCookie = new Cookie("histories", histories);
			historiesCookie.setPath("/");
			response.addCookie( historiesCookie );
			System.out.println("����� histories : "+ histories );
		}
		
		if(first) {  // ���� ��ȸ�� ��Ű�� ����
			// addCookie�� ���� ������ �⺻ cookie ���� = -1  :: client�� browser ���� �� �ڵ� ����
			histories = prodName + "."+ String.valueOf(prodNo);
			historyNo = String.valueOf(prodNo);
			Cookie historiesCookie =  new Cookie("histories", histories);
			historiesCookie.setPath("/");
			response.addCookie( historiesCookie );					
			Cookie historyNoCookie = new Cookie("historyNo", historyNo);
			historyNoCookie.setPath("/");
			response.addCookie( historyNoCookie );	
			System.out.println("history ��Ű�� ��� ���� �����߽��ϴ�.");
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
		
		/// user�� img�� �����ߴٸ�?
		if( !img.isEmpty() ) {
			
			String temp = this.getProduct(product.getProdNo()).getFileName().split("\\.")[0];
			String extention = "." + img.getOriginalFilename().split("\\.")[1];  // Ȯ���� :: '.' �� ����ǥ���� Ư�� �����̹Ƿ� �Ϲ� ���ڷ� ��ȯ����� ��
			String oldFileName = temp + extention;
			
			new File(imagePath + product.getFileName()).delete();
			this.saveImg(img, oldFileName, imagePath);
			product.setFileName(oldFileName);
		}
	}


}
