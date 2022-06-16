<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="jspstudy.dbconn.Dbconn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MAIN PAGES</title>
<style>

</style>
</head> 
<body>
<%
	//객체 생성 
	Dbconn dbconn = new Dbconn();
	System.out.println("dbconn"+dbconn);
%>


<h1 style="text-align:center;">수정한 페이지 </h1>

<table border="1" width="400px" style="text-align:center; border-collapse: collapse; margin-left: auto; margin-right: auto; " >
<tr>
	<td><a href="<%=request.getContextPath() %>/member/memberJoin.do" class="btton" >회원 가입</a></td>
	<td><a href="<%=request.getContextPath() %>/member/memberList.do" class="btton">회원 리스트</a></td>
</tr>
<tr>
	<td><a href="<%=request.getContextPath() %>/member/memberLogin.do" class="btton">로그인</a></td>
	<td><a href="<%=request.getContextPath() %>/member/memberList.do" class="btton"></a></td>
</tr>
<tr>
	<td><a href="<%=request.getContextPath() %>/board/boardWrite.do" class="btton">게시판 글 작성</a></td>
	<td><a href="<%=request.getContextPath() %>/board/boardList.do" class="btton">게시판 리스트</a></td>
</tr>


</table>
</body>
</html>