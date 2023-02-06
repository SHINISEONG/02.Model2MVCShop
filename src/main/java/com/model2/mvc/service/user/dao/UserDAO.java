package com.model2.mvc.service.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.user.vo.UserVO;

public class UserDAO {
	/// field
	public static HashMap<String, Object> countMaxMap;

	/// constructor
	public UserDAO() {
	}

	/// method
	public void insertUser(UserVO userVO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "insert into USERS values (?,?,?,'user',?,?,?,?,sysdate)";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userVO.getUserId());
		stmt.setString(2, userVO.getUserName());
		stmt.setString(3, userVO.getPassword());
		stmt.setString(4, userVO.getSsn());
		stmt.setString(5, userVO.getPhone());
		stmt.setString(6, userVO.getAddr());
		stmt.setString(7, userVO.getEmail());
		stmt.executeUpdate();

		con.close();
	}

	public UserVO findUser(String userId) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "select * from USERS where USER_ID=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userId);

		ResultSet rs = stmt.executeQuery();

		UserVO userVO = null;
		while (rs.next()) {
			userVO = new UserVO();
			userVO.setUserId(rs.getString("USER_ID"));
			userVO.setUserName(rs.getString("USER_NAME"));
			userVO.setPassword(rs.getString("PASSWORD"));
			userVO.setRole(rs.getString("ROLE"));
			userVO.setSsn(rs.getString("SSN"));
			userVO.setPhone(rs.getString("CELL_PHONE"));
			userVO.setAddr(rs.getString("ADDR"));
			userVO.setEmail(rs.getString("EMAIL"));
			userVO.setRegDate(rs.getDate("REG_DATE"));
		}

		con.close();

		return userVO;
	}

	public HashMap<String, Object> getUserList(SearchVO searchVO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "SELECT COUNT(user_id) total FROM users";
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " where USER_ID like '%" + searchVO.getSearchKeyword() + "%'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " where USER_NAME LIKE '%" + searchVO.getSearchKeyword() + "%'";
			}
		}

		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int total = rs.getInt("total");
		System.out.println("total : " + total);
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("count", new Integer(total));
		
		sql = "SELECT u.*, iv.num num FROM users u, ( SELECT ROWNUM as num , user_id FROM users ";
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " where USER_ID like '%" + searchVO.getSearchKeyword() + "%'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " where USER_NAME LIKE '%" + searchVO.getSearchKeyword() + "%'";
			}
		}
		sql += " ORDER BY user_id) iv "
		+      " WHERE u.user_id = iv.user_id "
		+	   " AND (num >= ? and num <= ? ) ";
		
		System.out.println(sql);
		System.out.println((searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1));
		System.out.println((searchVO.getPage() * searchVO.getPageUnit()));
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, (searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1));
						//1 * 3 - 3 + 1 = 1
						//2 * 3 - 3 + 1 = 4
		stmt.setInt(2, (searchVO.getPage() * searchVO.getPageUnit()));
						//
		rs = stmt.executeQuery();
		
		ArrayList<UserVO> list = new ArrayList<UserVO>();
		if (total > 0) {
			while (rs.next()){
				UserVO vo = new UserVO();
				vo.setUserId(rs.getString("USER_ID"));
				vo.setUserName(rs.getString("USER_NAME"));
				vo.setPassword(rs.getString("PASSWORD"));
				vo.setRole(rs.getString("ROLE"));
				vo.setSsn(rs.getString("SSN"));
				vo.setPhone(rs.getString("CELL_PHONE"));
				vo.setAddr(rs.getString("ADDR"));
				vo.setEmail(rs.getString("EMAIL"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				System.out.println(vo);
				list.add(vo);
				
			}
		}
		System.out.println("list.size() : " + list.size());
		map.put("list", list);
		System.out.println("map().size() : " + map.size());

		con.close();

		return map;
	}

	public void updateUser(UserVO userVO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "update USERS set USER_NAME=?,CELL_PHONE=?,ADDR=?,EMAIL=? where USER_ID=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, userVO.getUserName());
		stmt.setString(2, userVO.getPhone());
		stmt.setString(3, userVO.getAddr());
		stmt.setString(4, userVO.getEmail());
		stmt.setString(5, userVO.getUserId());
		stmt.executeUpdate();

		con.close();
	}
}