package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;

public class PurchaseServiceImpl implements PurchaseService {

	private PurchaseDAO purchaseDAO;
	private ProductDAO productDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
		productDAO = new ProductDAO();
	}

	@Override
	public void addPurchase(Purchase purchaseVO) throws Exception {
		purchaseDAO.insertPurchase(purchaseVO);
	}

	@Override
	public Purchase findPerchase(int tranNo) throws Exception {
		return purchaseDAO.findPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search searchVO, String userId) throws Exception {
		return purchaseDAO.getPurchaseList(searchVO, userId);
	}

	@Override
	public HashMap<String, Object> getSaleList(Search searchVO) throws Exception {
		return purchaseDAO.getSaleList(searchVO);
	}

	@Override
	public void updatePurchase(Purchase purchaseVO) throws Exception {
		purchaseDAO.updatePurchase(purchaseVO);
	}

	@Override
	public void updateTranCode(Purchase purchaseVO) throws Exception {
		purchaseDAO.updateTranCode(purchaseVO);
	}

}
