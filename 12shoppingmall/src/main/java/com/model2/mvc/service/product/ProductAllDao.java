package com.model2.mvc.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
// @Component
@Service
public class ProductAllDao {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ImagesDao imagesDao;
}
