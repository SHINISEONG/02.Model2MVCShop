package com.model2.mvc.service.purchase;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {
	public void addPurchase(Purchase purchaseVO) throws Exception;
	public Purchase findPerchase(int tranNo) throws Exception;
	public Map<String, Object> getPurchaseList(Search searchVO, String userId) throws Exception;
	public HashMap<String, Object> getSaleList(Search searchVO) throws Exception;
	public void updatePurchase(Purchase purchaseVO) throws Exception;
	public void updateTranCode(Purchase purchaseVO) throws Exception;
}
