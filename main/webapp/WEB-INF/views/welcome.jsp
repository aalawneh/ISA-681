<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  http://websystique.com/springmvc/spring-4-mvc-helloworld-tutorial-full-example/  -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome Hearts Page</title>
<link href="<c:url value='/static/css/bootstrap.css' />"
	rel="stylesheet"></link>
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
<link rel="stylesheet" type="text/css"
	href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>
<body>
	<div id="mainWrapper">
		<div class="login-container">
			<div class="login-card">
				<div class="login-form">
				
					<div style="text-align:center">
						<h2>
							Welcome to the Secure Hearts Game built for ISA 681...<br/>
						</h2>
						<br/>
						<br/>
						<h3>
							<a href="login.html">Login</a>&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;<a href="register.html">Register</a>
						</h3>
					</div>
					<br/>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
