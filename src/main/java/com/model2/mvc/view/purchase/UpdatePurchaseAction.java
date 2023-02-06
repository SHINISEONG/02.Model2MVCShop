package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdatePurchaseAction extends Action {

	public UpdatePurchaseAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		PurchaseVO purchaseVO = new PurchaseVO();
		PurchaseService purchaseService = new PurchaseServiceImpl();
		
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("divyDate"));
		
		purchaseService.updatePurchase(purchaseVO);
		purchaseVO = purchaseService.findPerchase(tranNo);
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/getPurchase.jsp";
	}

}
