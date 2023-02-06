package com.model2.mvc.view.user;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class ListUserAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		SearchVO searchVO=new SearchVO();
		
		int page=1;
		if(request.getParameter("page") != null)
			page=Integer.parseInt(request.getParameter("page"));
		
		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
		
		String pageUnit=getServletContext().getInitParameter("pageSize");  //servletcontext�� �� �׼ǿ� �ο��� ����.
		String pageDiv=getServletContext().getInitParameter("pageDiv");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		searchVO.setPageDiv(Integer.parseInt(pageDiv));
		
		
		int pageDivCnt = 1; 
		if (request.getParameter("pageDivCnt")!=null) {
			pageDivCnt = Integer.parseInt(request.getParameter("pageDivCnt"));
		}
		String pageDivCondition = request.getParameter("pageDivCondition");
		
		if(pageDivCondition != null) {
			if(pageDivCondition.equals("previous")) {
				pageDivCnt--;
			} else if(pageDivCondition.equals("next")) {
				pageDivCnt++;
			}
		}
		
		System.out.println("pageDiv : " + pageDiv);
		UserService service=new UserServiceImpl();
		HashMap<String,Object> map=service.getUserList(searchVO);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		
		
		return "forward:/user/listUser.jsp?&pageDivCnt="+pageDivCnt;
		
	}
}