<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="jspstudy.domain.*" %>
<%
ArrayList<BoardVo> alist =(ArrayList<BoardVo>) request.getAttribute("alist");
PageMaker pm =(PageMaker)request.getAttribute("pm");

%>

    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 리스트</title>
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
<H1 style="text-align:center;">게시글 리스트</H1>

<form name="frm" action="<%=request.getContextPath() %>/board/boardList.do" method="post">
<table width="800px" style="text-align:right; border-collapse: collapse; margin-left: auto; margin-right: auto; ">
	<tr>
		<td width="630px">
			<select name="searchType">
			<option value="subject">제목</option>
			<option value="wirter">작성자</option>
			</select>
		</td>
		<td>
			<input type="text" name="keyword" size="15">
		</td>
		<td>
			<input type="submit" name="submit" value="검색">
		</td>
	</tr>

</table>
</form>



<table border="1" width="800px" style="text-align:center; border-collapse: collapse; margin-left: auto; margin-right: auto; ">
	<tr style="color:white; background-color:skyblue">
		<th >bidx번호</th>
		<th >제목</th>
		<th >작성자</th>
		<th >작성일</th>
	</tr>
	
	<%for(BoardVo bv : alist){ %>
	<tr>
		<td><%=bv.getBidx() %></td>
		<td style="text-align:left;">
			<%
			for (int i =1;i<=bv.getLevel_();i++){
				out.print("&nbsp;&nbsp;");   // &nbsp -> 공백, 스페이스바, 한칸
				if(i==bv.getLevel_()){
					out.print(" ㄴ");
				}
			}
			%>
			<a href="<%=request.getContextPath()%>/board/boardContent.do?bidx=<%=bv.getBidx() %>"><%=bv.getSubject() %></a>
		</td>
		<td><%=bv.getWriter() %></td>
		<td><%=bv.getWriteday() %></td>
	</tr>
	<%} %>
	<tr>
		<td colspan="4" style="text-align: right;">
			<a href="/jspstudy/index.jsp"><input type="button" class="button" name="btn" value="Main Pages"></a>  
		</td>
	</tr>

</table>
<table style="text-align:center; border-collapse: collapse; margin-left: auto; margin-right: auto;  width:800px;">
	<tr>
		<td style="text-align:center;">
		<% if (pm.isPrev()==true) {
			out.println("<a href='"+request.getContextPath()+"/board/boardList.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");
		}
			%>
		</td>
		<td style="text-align:center;">
	
		<% 
			for(int i =pm.getStartPage();i<=pm.getEndPage();i++){
				out.println("<a href='"+request.getContextPath()+"/board/boardList.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
				
			}
		%>
	
		</td>
		<td style="text-align:center;">
		<% if (pm.isNext() && pm.getEndPage() > 0){
			out.println("<a href='"+request.getContextPath()+"/board/boardList.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
		} %>
		</td>
	</tr>


</table>
</body>
</html>














