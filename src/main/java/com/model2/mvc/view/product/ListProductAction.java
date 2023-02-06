package com.model2.mvc.view.product;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class ListProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchVO searchVO = new SearchVO();

		int page = 1;
		if (request.getParameter("page") != null)
		page = Integer.parseInt(request.getParameter("page"));

		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
		System.out.println("검색어 리스트 프로덕트 액션으로 잘 넘어와?"+searchVO.getSearchKeyword());
		System.out.println("검색어 리스트 프로덕트 액션으로 잘 넘어와? 바로파람"+request.getParameter("searchKeyword"));

		String pageUnit = getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		System.out.println(searchVO);
		ProductService service = new ProductServiceImpl();
		HashMap<String, Object> map = service.getProductList(searchVO);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		

		// TODO navigating 방식 및 URI 확인
		return "forward:/product/listProduct.jsp";
	}

}
