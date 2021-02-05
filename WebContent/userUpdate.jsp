<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="user.UserDAO" %>
<%@ page import="user.User" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name ="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
	String userID=null;
	if(session.getAttribute("userID")!=null){
		userID=(String)session.getAttribute("userID");
	}
	User user=new UserDAO().getUser(userID);
	
	%>

	<nav class="navbar navbar-inverse">
		<div class ="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class ="icon-bar"></span>
				<span class ="icon-bar"></span>
				<span class ="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">공유 다이어리📕</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">main</a></li>
				<li><a href="bbs.jsp">diary</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>
	<%
		
	%>
	<div class="container">
		<div class="col-lg-4"></div>
		<div class="col-lg-4">
			<div class="jumbotron" style="padding-top: 20px;">
				<form method="post" action="userUpdateAction.jsp">
					<h3 style="text-align:center;">내 정보</h3>
					<div class = "form-group">
						<input type="text" class="form-control" value=<%=user.getUserID()%> name="userID" maxlength="20">
					</div>
					<div class="form-group">
						<input type="password" class="form-control" value=<%=user.getUserPassword()%> name="userPassword" maxlength="20">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" value=<%=user.getUserName()%> name="userName" maxlength="20">
					</div>
					<div class="form-group" style="text-align:center;">
						<div class="btn-group" data-toggle="buttons">
						<%
							if(user.getUserGender().equals("남자")){
						%>
							<label class="btn btn-success active">
						<%
							} else{
						%>
							<label class="btn btn-success">
						<%
							}
						%>		
								<input type="radio" name="userGender" autocomplete="off" value="남자" checked>남자
							</label>
						<%
							if(user.getUserGender().equals("여자")){
						%>
								<label class="btn btn-success active">
						<%
							} else{
						%>
								<label class="btn btn-success">
						<%
							}
						%>	
								<input type="radio" name="userGender" autocomplete="off" value="여자" checked>여자
							</label>
						</div>
					</div>
					<div class="form-group">
						<input type="email" class="form-control" value=<%=user.getUserEmail()%> name="userEmail" maxlength="20">
					</div>
					<input type="submit" class="btn btn-success form-control" value="수정하기"></form>
					<br>
            <form method="post" action="userDeleteAction.jsp">
            <input type="submit" class="btn btn-danger pull-right" value="탈퇴하기">
            </form>

					</div></div>
</div></div>
	<script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>