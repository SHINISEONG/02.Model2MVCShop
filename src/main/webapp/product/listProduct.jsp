<%@page import="com.model2.mvc.service.product.ProductService"%>
<%@ page contentType="text/html; charset=euc-kr" %>

<%@ page import="java.util.*"  %>
<%@ page import="com.model2.mvc.service.purchase.vo.*" %>
<%@ page import="com.model2.mvc.service.product.vo.*" %>
<%@ page import="com.model2.mvc.service.user.vo.*" %>
<%@ page import="com.model2.mvc.common.*" %>
<%@ page import="com.model2.mvc.service.purchase.impl.*" %>
<%@ page import="com.model2.mvc.service.purchase.*" %>


<%
	HashMap<String,Object> map=(HashMap<String,Object>)request.getAttribute("map");
	SearchVO searchVO=(SearchVO)request.getAttribute("searchVO");
	
	int total=0;
	ArrayList<ProductVO> list=null;
	if(map != null){
		total=((Integer)map.get("count")).intValue();
		list=(ArrayList<ProductVO>)map.get("list");
	}
	
	int currentPage=searchVO.getPage();
	
	int pageDiv=searchVO.getPageDiv();
	int pageDivCnt = 1; 
	
	if(request.getParameter("pageDivCnt")!=null){
		pageDivCnt = Integer.parseInt(request.getParameter("pageDivCnt"));
	}	
	
	int pageDivStart = pageDivCnt*pageDiv - pageDiv + 1;
	int pageDivEnd = pageDivStart+pageDiv-1;
	
	int pageDivMax = 0;
	
	int totalPage=0;
	
	if(total > 0) {
		
		totalPage= total / searchVO.getPageUnit() ;
		if(total%searchVO.getPageUnit() >0) {
			totalPage += 1;
		}
		
		pageDivMax = total / (pageDiv*searchVO.getPageUnit());
		if(total%(pageDiv*searchVO.getPageUnit()) > 0){
			pageDivMax +=1;
		}
		
	}
	
	if (pageDivEnd>totalPage){
		pageDivEnd = totalPage;
	}
	
	if(searchVO.getSearchCondition()==null){
		searchVO.setSearchCondition("");
	}
	
	if(searchVO.getSearchKeyword() == null){
		searchVO.setSearchKeyword("");
	}
	
	String menu="";
	menu = request.getParameter("menu");
	
	PurchaseService purchaseService = new PurchaseServiceImpl();
	
	UserVO userVO = (UserVO)session.getAttribute("user");
	
	
%>



<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
<!--
function fncGetProductList(){
	document.detailForm.submit();
}
-->
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listProduct.do?menu=<%=menu%>" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					<%if (menu.equals("manage")) {%>
						상품 관리
					<%} else if (menu.equals("search")){%>
						상품 목록조회
					<%} %>
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px">
					
					<option value="0" <%= searchVO.getSearchCondition().equals("")||searchVO.getSearchCondition().equals("0")?"selected":"" %>>상품번호</option>
					<option value="1" <%= searchVO.getSearchCondition().equals("1")?"selected":"" %>>상품명</option>
					<option value="2" <%= searchVO.getSearchCondition().equals("2")?"selected":"" %>>상품가격</option>
			</select>
			<input type="text" name="searchKeyword" value="<%= searchVO.getSearchCondition()== "" ? "" : searchVO.getSearchKeyword() %>" class="ct_input_g" style="width:200px; height:19px" />
		</td>
	
		
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList();">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >전체  <%= total%> 건수, 현재 <%=currentPage %> 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<%
		int no=list.size();
		for(int i=0; i<list.size(); i++) {
			ProductVO vo = (ProductVO)list.get(i);
			int prodNo = vo.getProdNo();
			int tranCode = Integer.parseInt(vo.getProTranCode().trim());
	%>
	<tr class="ct_list_pop">
		<td align="center"><%=no--%></td>
		<td></td>
				
				<td align="left">
				<%if(tranCode==0) {%>
				<a href="/getProduct.do?prodNo=<%=vo.getProdNo()%>&menu=<%=menu%>"><%=vo.getProdName()%></a></td>
				<%} else {%>
					<%if(userVO.getUserId().equals("admin")) {%>
						<a href="/getProduct.do?prodNo=<%=vo.getProdNo()%>&menu=<%=menu%>"><%=vo.getProdName()%></a></td>
					<%} else {%>
					
					<%=vo.getProdName()%></td>
					
					<%} %>
				<%} %>
		
		<td></td>
		<td align="left"><%=vo.getPrice()%></td>
		<td></td>
		<td align="left"><%=vo.getRegDate()%></td>
		<td></td>
		<td align="left">
		<%if(menu.equals("search")) {%>
			<%if(!(userVO.getUserId().equals("admin"))) {%>
				<%if(tranCode == 0) {%>
					 	판매중
					<%} else {%>
						재고 없음
					<%} %>
			<%} else {%>
				<%if(tranCode==0) {%>
					판매중
				<%} else if(tranCode==1) {%>
					구매완료
				<%} else if(tranCode==2) {%>
					배송중
				<%} else if(tranCode==3) {%>
					배송완료
				<%} %>
				
			<%} %>
		<%} else if(menu.equals("manage")) {%>
				
				<%if(tranCode==0) {%>
					판매중
				<%} else if(tranCode==1) {%>
					구매완료
					&nbsp;
					<a href="/updateTranCodeByProd.do?prodNo=<%=vo.getProdNo() %>&tranCode=2&searchCondition=<%=searchVO.getSearchCondition()%>&searchKeyword=<%=searchVO.getSearchKeyword()%>&page=<%=searchVO.getPage()%>">배송하기</a>
				<%} else if(tranCode==2) {%>
					배송중
				<%} else if(tranCode==3) {%>
					배송완료
				<%} %>
				
		<%} %>
		</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>	
<% } //end of for %>
		
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
		
		
		<%if(pageDivCnt > 1)  {%>
				<a href="/listProduct.do?page=<%=pageDivStart-1%>&menu=<%=menu%>&pageDivCnt=<%=pageDivCnt %>&pageDivCondition=previous&searchKeyword=<%=searchVO.getSearchKeyword()%>&searchCondition=<%=searchVO.getSearchCondition()%>">&nbsp;&lt;&lt;&nbsp;</a>
		<%} %>
		
		<% for(int i=pageDivStart;i<=pageDivEnd;i++){ %>
			<a href="/listProduct.do?page=<%=i%>&menu=<%=menu%>&pageDivCnt=<%=pageDivCnt %>&searchKeyword=<%=searchVO.getSearchKeyword()%>&searchCondition=<%=searchVO.getSearchCondition()%>"><%=i %></a>
		<%}	%>
		
		<%if(pageDivCnt<pageDivMax) {%>
			<a href="/listProduct.do?page=<%=pageDivEnd+1%>&menu=<%=menu%>&pageDivCnt=<%=pageDivCnt %>&pageDivCondition=next&searchKeyword=<%=searchVO.getSearchKeyword()%>&searchCondition=<%=searchVO.getSearchCondition()%>">&nbsp;&gt;&gt;&nbsp;</a>		
		<%} %>
		
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
