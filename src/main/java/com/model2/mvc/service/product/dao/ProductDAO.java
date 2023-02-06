package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {
	/// field
	/// constructor
	public ProductDAO() {
	}

	/// method
	public void insertProduct(ProductVO productVO) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO product VALUES(seq_product_prod_no.NEXTVAL, ?, ?, ?, ?, ?, sysdate)";
		
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.executeUpdate();

		con.close();
	}// end of insertProduct()

	public ProductVO findProduct(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();
		PreparedStatement stmt = con.prepareStatement("	SELECT p.*, NVL(t.tran_status_code,0) tran_code, NVL(t.tran_no,0) tran_no FROM product p, transaction t "
				+ "WHERE p.prod_no = t.prod_no(+) AND p.prod_no = ?");
		
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();	
		
		ProductVO productVO = null;
		
		while (rs.next()) {
			productVO = new ProductVO();
			productVO.setProdNo(prodNo);
			
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
			productVO.setProTranCode(rs.getString("tran_code"));
			productVO.setProTranNo(rs.getInt("TRAN_NO"));
		}
		con.close();
		return productVO;
	}// end of findProduct

	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception {

		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT count(prod_no) total FROM product";
		if (searchVO.getSearchCondition() != null) {
			
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " WHERE PROD_NO='" + searchVO.getSearchKeyword() // searchCon이 0이면 아이디검색
						+ "'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " WHERE PROD_NAME like '%" + searchVO.getSearchKeyword() // serchCon이 1이면 이름 검색
						+ "%'";
			} else if (searchVO.getSearchCondition().equals("2")) {
				sql += " WHERE PRICE='" + searchVO.getSearchKeyword() // serchCon이 2이면 가격 검색
				+ "'";
			}
		}
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int total = rs.getInt("total");
		
		sql = "SELECT iv.num, p.*, NVL(t.tran_status_code,0) tran_code, NVL(t.tran_no,0) tran_no FROM product p, transaction t, (SELECT ROWNUM as num, prod_no FROM product ";
		
		if (searchVO.getSearchCondition() != null) {
			
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " WHERE PROD_NO='" + searchVO.getSearchKeyword() // searchCon이 0이면 아이디검색
						+ "'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " WHERE PROD_NAME like '%" + searchVO.getSearchKeyword() // serchCon이 1이면 이름 검색
						+ "%'";
			} else if (searchVO.getSearchCondition().equals("2")) {
				sql += " WHERE PRICE='" + searchVO.getSearchKeyword() // serchCon이 2이면 가격 검색
				+ "'";
			}
		}
		
		sql += " ORDER BY prod_no) iv "
		 		+ " WHERE p.prod_no = t.prod_no(+) "
		 		+ " AND p.prod_no = iv.prod_no"
				+ " AND (num >= ? AND num <= ?)";
		
		System.out.println(sql);
		stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, (searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1));
		//1 * 3 - 3 + 1 = 1
		//2 * 3 - 3 + 1 = 4
		stmt.setInt(2, (searchVO.getPage() * searchVO.getPageUnit()));
		
		rs = stmt.executeQuery();
		
		System.out.println("상품검색 로우의 수:" + total);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(total)); 

		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		if (total > 0) { // 검색 결과가 있으면!
			
			while(rs.next()) {
				ProductVO vo = new ProductVO();

				vo.setProdNo(rs.getInt("PROD_NO"));
				vo.setProdName(rs.getString("PROD_NAME"));
				vo.setFileName(rs.getString("IMAGE_FILE"));
				vo.setProdDetail(rs.getString("PROD_DETAIL"));
				vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				vo.setProTranCode(rs.getString("tran_code"));
				vo.setProTranNo(rs.getInt("tran_no"));
				list.add(vo);
				
			}
		}
		System.out.println("list.size() : " + list.size());
		map.put("list", list); // "list"에 검색결과 할당
		System.out.println("map().size() : " + map.size());

		con.close();

		return map;
	}// end of getProductList

	public void updateProduct(ProductVO productVO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product SET prod_name=?, prod_detail=?, manufacture_day=?, price=?, image_file=?"
					+ "WHERE prod_no=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.setInt(6, productVO.getProdNo());
		stmt.executeUpdate();

		con.close();
	}// end of updateProduct

}
