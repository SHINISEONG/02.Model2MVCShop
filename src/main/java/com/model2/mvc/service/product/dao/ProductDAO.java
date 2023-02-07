package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDAO {
	/// field
	/// constructor
	public ProductDAO() {
	}

	/// method
	public void insertProduct(Product productVO) throws Exception {
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

	public Product findProduct(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();
		PreparedStatement stmt = con.prepareStatement("	SELECT p.*, NVL(t.tran_status_code,0) tran_code, NVL(t.tran_no,0) tran_no FROM product p, transaction t "
				+ "WHERE p.prod_no = t.prod_no(+) AND p.prod_no = ?");
		
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();	
		
		Product productVO = null;
		
		while (rs.next()) {
			productVO = new Product();
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

	public Map<String, Object> getProductList(Search search) throws Exception {

		
		
		String sql = "SELECT prod_no, prod_name, price, reg_date FROM product ";
			System.out.println("getPL 내부 search"+search);
			if(search.getSearchCondition() != null && search.getSearchKeyword()!=null) {
				if (search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")) {
					sql += " WHERE PROD_NO='" + search.getSearchKeyword() // searchCon이 0이면 아이디검색
							+ "'";
				} else if (search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
					sql += " WHERE PROD_NAME like '%" + search.getSearchKeyword() // serchCon이 1이면 이름 검색
							+ "%'";
				} else if (search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {
					sql += " WHERE PRICE='" + search.getSearchKeyword() // serchCon이 2이면 가격 검색
					+ "'";
				}
			}
		System.out.println("total Count 전 Query : " + sql);
		int totalCount = getTotalCount(sql);
		
		System.out.println("상품검색 로우의 수:" + totalCount);
		
		sql += "ORDER BY prod_no";
		
		sql = makeCurrentPageSql(sql, search);
		
		System.out.println("makeCurrentPage result : " + sql);
		
		Connection con = DBUtil.getConnection();
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
				
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", new Integer(totalCount)); 

		System.out.println("searchVO.getPage():" + search.getCurrentPage());
		System.out.println("searchVO.getPageSize():" + search.getPageSize());

		List<Product> list = new ArrayList<Product>();
		if (totalCount > 0) { // 검색 결과가 있으면!
			
			while(rs.next()) {
				Product product = new Product();
				
				product.setProdNo(rs.getInt("PROD_NO"));
				product.setProdName(rs.getString("PROD_NAME"));
				product.setPrice(rs.getInt("PRICE"));
				product.setRegDate(rs.getDate("REG_DATE"));
				product.setProTranCode(rs.getString("tran_code"));
				product.setProTranNo(rs.getInt("tran_no"));
				list.add(product);
			}
		}
		System.out.println("list.size() : " + list.size());
		map.put("list", list); // "list"에 검색결과 할당
		System.out.println("map().size() : " + map.size());

		con.close();
		stmt.close();
		rs.close();
		
		return map;
	}// end of getProductList

	public void updateProduct(Product productVO) throws Exception {

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
		
		stmt.close();
		con.close();
	}// end of updateProduct
	
	private int getTotalCount(String sql) throws Exception {
		Connection con = DBUtil.getConnection();
		sql = "SELECT COUNT(*) total FROM "
				+ "(" + sql + ")";
		
		System.out.println("totalCount query " + sql);
		
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		int totalCount = 0; 
		rs.next();
		totalCount = rs.getInt("total");
		
		con.close();
		stmt.close();
		rs.close();
		
		return totalCount;
	}
	
	private String makeCurrentPageSql(String sql, Search search) {
		sql = "SELECT iv2.*, NVL(t.tran_no,0) \"tran_no\" , NVL(t.tran_status_code,0) \"tran_code\" FROM "
				+ " (SELECT ROWNUM num, iv.* FROM ("+sql+") iv WHERE ROWNUM <= "+(search.getCurrentPage()*search.getPageSize())+") iv2, transaction t "
				+ " WHERE iv2.prod_no = t.prod_no(+) "
				+ " AND num >= " + ((search.getCurrentPage()-1)*search.getPageSize()+1) +" ORDER BY num" ;
		
		System.out.println("UserDAO :: make SQL :: "+ sql);
		
	return sql;
	}
	
}
