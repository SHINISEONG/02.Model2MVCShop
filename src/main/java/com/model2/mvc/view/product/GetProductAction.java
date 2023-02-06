package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		String menu = request.getParameter("menu");
		System.out.println(menu);
		ProductService service = new ProductServiceImpl();
		ProductVO vo = service.getProduct(prodNo);

		request.setAttribute("productVO", vo);
		request.setAttribute("menu", menu);
		// TODO navigating 방식 및 URI 체크
		return "forward:/product/getProductView.jsp";
	}

}
