package com.model2.mvc.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Getter
@Component
public class ProductAllDao {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ImagesDao imagesDao;
}
