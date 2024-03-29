package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeAction extends Action {



	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		Purchase purchaseVO = new Purchase();
				
		String tranCode = request.getParameter("tranCode");
		String tranNo = request.getParameter("tranNo");
		
		String page = request.getParameter("page");
		
		purchaseVO.setTranNo(Integer.parseInt(tranNo));
		purchaseVO.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchaseVO);
		
		return "forward:/listPurchase.do?&page="+page;
	}

}
