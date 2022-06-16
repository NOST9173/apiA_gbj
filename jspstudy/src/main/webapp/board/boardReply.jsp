<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@page import= "jspstudy.domain.BoardVo" %>
<%
if(session.getAttribute("midx")== null){
	out.println("<script>alert('로그인 해주시기 바랍니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
}

%>	
	
	<% BoardVo bv = (BoardVo)request.getAttribute("bv"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reply</title>
<style>
h1 {
	text-align: center;
}

table {
	border-collapse: collapse;
	margin-left: auto;
	margin-right: auto;
	width: 600px;
}

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
	  //	alert("테스트");
	  	
	  	var fm = document.frm;
	  	
	  	if (fm.subject.value ==""){
	  		alert("제목을 입력하세요");
	  		fm.subject.focus();
	  		return;
	  	} else if (fm.content.value == ""){
	  		alert("내용을 입력하세요");  		
	  		fm.content.focus();
	  		return;
	  	}else if (fm.writer.value == ""){
	  		alert("작성자를 입력하세요");  		
	  		fm.writer.focus();
	  		return;
	  	} 	  	
	  		// 가상경로를 사용해서 페이지 이동시킨다
	  		fm.action = "<%=request.getContextPath()%>/board/boardReplyAction.do";
	
		fm.method = "post";
		fm.submit();
		return;
	}
</script>

</head>
<body>
<h1>Write Reply</h1>
<form name="frm">
	<input type="hidden" name="bidx" value="<%=bv.getBidx() %>" >
	<input type="hidden" name="originbidx" value="<%=bv.getOriginbidx() %>" >
	<input type="hidden" name="depth" value="<%=bv.getDepth() %>" >
	<input type="hidden" name="level_" value="<%=bv.getLevel_() %>" >
		<div>
			<table>
				<tr>
					<td style="text-align: center;"></td>
					<td >
						<input type="text" name="subject" size="83" maxlength="100" placeholder="게시글 제목" >
					</td>
				</tr>
				<tr>
					<td style="text-align: center;"></td>
					<td>
						<textarea cols="85" rows="40" name="content" placeholder="게시글 내용"></textarea>
					</td>
				</tr>
				<tr>
					<td style="text-align: center;"></td>
					<td style="text-align: right;">
						<input type="text" name="writer" size="20" maxlength="50" placeholder="작성자">
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="button" class="button" name="btn" value="확인" onclick="check();"> 
						<input type="reset" class="reset" value="다시작성">
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;"></td>
				</tr>
			</table>
		</div>
</form>
</body>
</html>