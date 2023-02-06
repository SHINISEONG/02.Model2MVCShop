package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeByProdAction extends Action {

	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		String tranCode = request.getParameter("tranCode");
		String searchCondition = request.getParameter("searchCondition");
		String searchKeyword = request.getParameter("searchKeyword");
		String page = request.getParameter("page");
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		ProductService productService = new ProductServiceImpl();
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductVO productVO = productService.getProduct(prodNo);
		
		int tranNo = productVO.getProTranNo();
				
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchaseVO);
		
		return "forward:/listProduct.do?menu=manage&searchCondition="+searchCondition
				+"&searchKeyword="+searchKeyword
				+"&page="+page;
	}

}
