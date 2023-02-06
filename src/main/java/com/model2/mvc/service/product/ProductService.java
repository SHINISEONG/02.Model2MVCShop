package com.model2.mvc.service.product;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.product.vo.ProductVO;

public interface ProductService {
	public void addProduct(ProductVO productVO) throws Exception;
	public ProductVO getProduct(int prodNo) throws Exception;
	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception;
	public void updateProduct(ProductVO productVO) throws Exception;
	//TODO 모델링 기술서랑 다르게 void?? UserService update method역시 모델링 문서에는 UserVO를
	//리턴하도록 돼 있지만 void로 수행하고 있음 체크할 것
}
