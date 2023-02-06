package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {
	/// field
	ProductService productService = new ProductServiceImpl();
	UserService userService = new UserServiceImpl();

	/// constructor
	public PurchaseDAO() {
	}

	/// method
	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO transaction VALUES(seq_transaction_tran_no.NEXTVAL, "
				+ "?, ?, ?, ?, ?, ?, ?, ? , sysdate,?)";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDivyAddr());
		stmt.setString(7, purchaseVO.getDivyRequest());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setString(9, purchaseVO.getDivyDate());
		stmt.executeUpdate();

		con.close();
	}// end of insertPurchase()

	public PurchaseVO findPurchase(int tranNo) throws Exception {
		Connection con = DBUtil.getConnection();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM transaction WHERE tran_no = ?");

		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();

		PurchaseVO purchaseVO = null;
		ProductVO productVO = null;
		UserVO userVO = null;

		while (rs.next()) {
			productVO = productService.getProduct(rs.getInt("PROD_NO"));
			System.out.println("purDAO로 proVO잘 넘어와?" + productVO);

			userVO = userService.getUser(rs.getString("BUYER_ID"));
			System.out.println("purDAO로 userVO잘 넘어와?" + userVO);

			purchaseVO = new PurchaseVO();
			purchaseVO.setTranNo(rs.getInt("TRAN_NO"));
			purchaseVO.setPurchaseProd(productVO);
			purchaseVO.setBuyer(userVO);
			purchaseVO.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchaseVO.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchaseVO.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchaseVO.setDivyAddr(rs.getString("DLVY_ADDR"));
			purchaseVO.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchaseVO.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchaseVO.setOrderDate(rs.getDate("ORDER_DATE"));
			purchaseVO.setDivyDate(rs.getString("DLVY_DATE"));

		}
		con.close();
		return purchaseVO;
	}// end of findPurchase()

	public HashMap<String, Object> getPurchaseList(SearchVO searchVO, String userId) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "select * from TRANSACTION where BUYER_ID = ? order by TRAN_NO";

		PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE); //

		stmt.setString(1, userId);

		ResultSet rs = stmt.executeQuery();

		rs.last();
		int total = rs.getRow();
		System.out.println("구매목록검색 로우의 수:" + total);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(total)); // 맵 키 count에 총 검색 결과 수 할당.

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1);
		// 보여줄 page번호(ex. 2) page당 보여줄 결과 수(ex.3) - page당 보여줄 결과 수+1(ex.4) =2)
		// 3*3-3+1 = 7
		
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
		if (total > 0) { // 검색 결과가 있으면!

			for (int i = 0; i < searchVO.getPageUnit(); i++) {

				ProductVO productVO = null;
				UserVO userVO = null;

				PurchaseVO purchaseVO = new PurchaseVO();

				productVO = productService.getProduct(rs.getInt("PROD_NO"));
				System.out.println("purDAO findList로 proVO잘 넘어와?" + productVO);

				userVO = userService.getUser(rs.getString("BUYER_ID"));
				System.out.println("purDAO로 findList로 userVO잘 넘어와?" + userVO);

				purchaseVO = new PurchaseVO();
				purchaseVO.setTranNo(rs.getInt("TRAN_NO"));
				purchaseVO.setPurchaseProd(productVO);
				purchaseVO.setBuyer(userVO);
				purchaseVO.setPaymentOption(rs.getString("PAYMENT_OPTION"));
				purchaseVO.setReceiverName(rs.getString("RECEIVER_NAME"));
				purchaseVO.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
				purchaseVO.setDivyAddr(rs.getString("DLVY_ADDR"));
				purchaseVO.setDivyRequest(rs.getString("DLVY_REQUEST"));
				purchaseVO.setTranCode(rs.getString("TRAN_STATUS_CODE"));
				purchaseVO.setOrderDate(rs.getDate("ORDER_DATE"));
				purchaseVO.setDivyDate(rs.getString("DLVY_DATE"));

				list.add(purchaseVO);
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : " + list.size());
		map.put("list", list); // "list"에 검색결과 할당
		System.out.println("map().size() : " + map.size());

		con.close();

		return map;
	}// end of getPurchaseList()
	
	
	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {
			return null;
	}//end of getSaleList()
	

	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, dlvy_addr=?, dlvy_request=?, dlvy_date=? "
				+ "	  WHERE tran_no=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getPaymentOption());
		stmt.setString(2, purchaseVO.getReceiverName());
		stmt.setString(3, purchaseVO.getReceiverPhone());
		stmt.setString(4, purchaseVO.getDivyAddr());
		stmt.setString(5, purchaseVO.getDivyRequest());
		stmt.setString(6, purchaseVO.getDivyDate());

		stmt.setInt(7, purchaseVO.getTranNo());

		stmt.executeUpdate();

		con.close();
	}// end of updatePurchase

	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "	UPDATE transaction SET tran_status_code=? "
				+ "	  WHERE tran_no=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getTranCode());
		stmt.setInt(2, purchaseVO.getTranNo());

		stmt.executeUpdate();

		con.close();
	}//end of updateTranCode()
	
}// end of class
