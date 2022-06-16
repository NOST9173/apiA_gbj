<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "jspstudy.domain.BoardVo" %>    
 <%
 	BoardVo bv =(BoardVo) request.getAttribute("bv");
 
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 내용</title>
<style>
.button, .reset {
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
<script>
function check(){
	  	//alert("테스트");
	  	var fm = document.frm;
	  	if (confirm("삭제를 실행합니다. 삭제 하시겠습니까?")) {
	  	    // 확인 버튼 클릭 시 동작
	  		alert("삭제 되었습니다.");
	  		fm.action = "<%=request.getContextPath()%>/board/boardDeleteAction.do";
	  		
			fm.method = "post";
			fm.submit();
	  	}else{
	  	    // 취소 버튼 클릭 시 동작
	  	    alert("취소 되었습니다.");
	  	    
	  	}
	  		// 가상경로를 사용해서 페이지 이동시킨다
	  		
		return;
	}
</script>
</head>
<body>
	<h1 style="text-align:center;">게시글 내용</h1>
	<form name="frm">
		<div>
			<table border="1" style="text-align:center; border-collapse: collapse; margin-left: auto; margin-right: auto; ">
				<tr>
					<td style="text-align: center;">제목</td>
					<td><%=bv.getSubject() %></td>
				</tr>
				<tr>
					<td style="text-align: center;">내용</td>
					<td style="vertical-align: top;  text-align: left;" width="700px" height="600px"><%=bv.getContent() %></td>
				</tr>
				<tr>
					<td style="text-align: center;">파일다운로드</td>
					<td >
					<%if (bv.getFilename() != null){ %><img src="<%=request.getContextPath() %>/images/<%=bv.getFilename() %>"><%} %>
					<a href="<%=request.getContextPath()%>/board/fileDownload.do?filename=<%=bv.getFilename()%>"><%=bv.getFilename() %></a>
					</td>
				</tr>
				<tr>
					<td style="text-align: center;">작성자</td>
					<td style="text-align: right; "><%=bv.getWriter() %></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: right;">
					<input type="button" class="button" name="modify" value="수정" onclick="location.href='<%=request.getContextPath()%>/board/boardModify.do?bidx=<%=bv.getBidx()%>'"> 
					<input type="button" class="button" name="delete" value="삭제" onclick="location.href='<%=request.getContextPath()%>/board/boardDelete.do?bidx=<%=bv.getBidx()%>'"> 
					<input type="button" class="button" name="reply" value="답변" onclick="location.href='<%=request.getContextPath()%>/board/boardReply.do?bidx=<%=bv.getBidx()%>&originbidx=<%=bv.getOriginbidx()%>&depth=<%=bv.getDepth()%>&level_=<%=bv.getLevel_()%>'"> 
					<input type="button" class="button" name="list" value="목록" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'"> 
					</td>
				</tr>
			</table>
		</div>
	</form>

</body>
</html>














