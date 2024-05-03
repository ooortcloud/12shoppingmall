package com.model2.mvc.service.product;

import org.apache.ibatis.annotations.Mapper;

import com.model2.mvc.service.domain.Images;

@Mapper
public interface ImagesDao {

	public int insertImages(Images images) throws Exception;
	
	public Images getImages(Integer prodNo) throws Exception;
	
	public int updateImages(Images images) throws Exception;
}
