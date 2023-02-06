package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action {

	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession(true);
		PurchaseVO purchaseVO=new PurchaseVO();
		ProductVO productVO = (ProductVO)session.getAttribute("productVO");
		UserVO userVO = (UserVO)session.getAttribute("user");
		System.out.println("session에 세팅된 prodVO in AddPurAct : " + productVO);
		System.out.println("session에 세팅된 userVO in AddPurAct : " + userVO);
		
		purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setBuyer(userVO);
		
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("receiverDate"));
		
		purchaseVO.setTranCode("1");
		
		System.out.println("addPurchaseAction안에서 모든 값 잘 세팅 됐니?"+purchaseVO);
		
		PurchaseService purchaseService=new PurchaseServiceImpl();
		purchaseService.addPurchase(purchaseVO);
		request.setAttribute("purchaseVO", purchaseVO);
		return "forward:/purchase/addPurchase.jsp";
	}

}
