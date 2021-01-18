<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID")!=null){
			userID=(String)session.getAttribute("userID");
		}
		if(userID == null){
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href='login.jsp'");
			script.println("</script>");
		}
		User user = new UserDAO().getUser(userID);
			if(request.getParameter("userID")==null||request.getParameter("userPassword")==null||request.getParameter("userName")==null
			||request.getParameter("userGender")==null||request.getParameter("userEmail")==null){
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}else{
			UserDAO userDAO=new UserDAO();//하나의 인스턴스
			int result=userDAO.update(userID,request.getParameter("userPassword"),request.getParameter("userName"),request.getParameter("userGender"),request.getParameter("userEmail"));
			if(result == -1){//데이터 베이스 오류가 날 때
				PrintWriter script=response.getWriter();
				script.println("<script>");
				script.println("alert('회원정보 수정에 실패했습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else{
				PrintWriter script=response.getWriter();
				script.println("<script>");
				script.println("alert('회원정보 수정에 성공했습니다.')");
				script.println("location.href='main.jsp'");
				script.println("</script>");
			}
		
		}
	%>
</body>
</html>