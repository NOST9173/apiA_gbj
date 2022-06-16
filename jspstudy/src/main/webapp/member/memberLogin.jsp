<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <TITLE> New Document </TITLE> 
  <script>
  function check(){
  	//alert("테스트");
  	
  	var fm = document.frm;
  	
  	if (fm.memberId.value ==""){
  		alert("아이디를 입력하세요");
  		fm.memberId.focus();
  		return;
  	}else if (fm.memberPwd.value ==""){
  		alert("비밀번호를 입력하세요");
  		fm.memberPwd.focus();
  		return;
  	}
  	
  		// 가상경로를 사용해서 페이지 이동시킨다
  	//	fm.action = "./memberJoinOk.jsp";
  		fm.action = "<%=request.getContextPath()%>/member/memberLoginAction.do ";
  		fm.method = "post";
  		fm.submit();
  	
  
   return;
  }
  
  </script>
 <style>
  	.button {
  		cursor: pointer;
  		background-color: powderblue;
		border: none;
		color: white;
		
  	}
</style>
 </HEAD>

 <BODY>
<center><h1>로그인</h1></center>
<hr></hr>
<form name="frm"> 
	 <table border="1" style="text-align:left;width:320px;height:200px;border-collapse: collapse; margin-left: auto; margin-right: auto; ">
		<tr>
			<td style="text-align:center;">ID</td>
			<td><input  type="text" name="memberId" size="30" style="border:none;border-bottom:1px solid #CCC;" placeholder="ID "></td>
		</tr>
		<tr>
			<td style="text-align:center;">PWD</td>
			<td><input type="password" name="memberPwd" size="30" style="border:none;border-bottom:1px solid #CCC;" placeholder="PWD "></td>
		</tr>
		
		<tr>
		<td ></td>
		<td style="text-align:center;">
			<input type="button" name ="btn" value="확인" onclick="check();" class="button"> 
			<input type="reset" value="다시작성" class="button"> 
			<a href="/jspstudy/index.jsp"><input type="button" class="button" name="btn" value="홈"></a>  
		</td>
		</tr>
 	</table>
 </form>
 </BODY>
</HTML>
