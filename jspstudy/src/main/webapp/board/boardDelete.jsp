<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import = "jspstudy.domain.BoardVo" %>    
    
    <%
if(session.getAttribute("midx")== null){
	out.println("<script>alert('로그인 해주시기 바랍니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
}

%>	
 <%
 	BoardVo bv =(BoardVo) request.getAttribute("bv");
 
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 삭제</title>
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


<form name="frm">
<input type="hidden" name="bidx" value="<%=bv.getBidx() %>" >

<table>
<tr>
<td colspan=2 style="text-align:center;">삭제하시겠습니까?</td>
</tr>
<tr>
	<td colspan="2" style="text-align: center;">
		<input type="button" class="button" name="btn" value="확인" onclick="check();"> 
		<a href="/jspstudy/index.jsp"><input type="button" class="button" name="btn" value="취소"></a>  
	</td>
</tr>

</table>

</form>

</body>
</html>