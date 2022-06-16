<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="jspstudy.domain.*" %>   
<%@ page import = "java.util.*" %> 
<%
	//select 쿼리를 사용하기 위해서 function에서 메소드를 만든다
	//memberSelectAll 메소드 호출한다
	//MemberDao md = new MemberDao();
//	ArrayList<MemberVo> alist = md.memberSelectAll() ;
//	out.println(alist.get(0).getMembername()+"<br>");

    ArrayList<MemberVo> alist=(ArrayList<MemberVo>)request.getAttribute("alist");
	
%>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<style>
	.button {
	
	background-color: powderblue;
	border: none;
	color: white;
	padding: 5px 15px;
	text-align: right;
	text-decoration: none;
	display: inline-block;
	font-size: 13px;
	margin: 4px 4px;
	cursor: pointer;
	}

</style>
</head>
<body>
<%
	if(session.getAttribute("midx") != null){
		out.println("회원 아이디 : "+session.getAttribute("memberId")+"<br>");
		out.println("회원 이름 : "+session.getAttribute("memberName")+"<br>");
		
		out.println("<a href='"+request.getContextPath()+"/member/memberLogout.do'>로그아웃</a>");
		
		
	}
%>


<H1 style="text-align:center;">회원목록 리스트</H1>

<table border="1" width="800px" style="text-align:center; border-collapse: collapse; margin-left: auto; margin-right: auto; ">
	<tr style="color:white; background-color:skyblue">
		<th>번호</th>
		<th>회원이름</th>
		<th>회원연락처</th>
		<th>작성일</th>
	</tr>

<% for (MemberVo mv : alist) {%>
	<tr>
		<td><%=mv.getMidx() %></td>
		<td><%=mv.getMembername() %></td>
		<td><%=mv.getMemberphone() %></td>
		<td><%=mv.getWriteday() %></td>
	</tr>
<% } %>
	<tr>
		<td colspan="4" style="text-align: right;">
			<a href="/jspstudy/index.jsp"><input type="button" class="button" name="btn" value="Main Pages"></a>  
		</td>
	</tr>
	

</table>
</body>
</html>