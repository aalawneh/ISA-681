<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hearts Game Home Page</title>
<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>

<style>
table.bord, th.bord, td.bord {
	border: 1px solid black;
	border-collapse: collapse;
}

th.bord, td.bord {
	padding: 5px;
	text-align: left;
}

table#t01 {
	width: 100%;
	background-color: #f1f1c1;
}
</style>
</head>
<body>
	<div class="success">
		<c:if test="${playerError eq 'Y' || playerError eq 'y'}">
		    <div class="alert alert-danger">
				<p>Error message : ${failure}</p>
			</div>
		</c:if>
		<table style="width: 100%; border: 0">
			<tr>
		        <c:url var="logoutUrl" value="/logout"/>
                <form action="${logoutUrl}" method="post">
                    <td align="left" colspan="2"><input type="submit" value="Log out" class="btn btn-primary btn-sm"/></td>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
		        <c:url var="gameUrl" value="/joingame"/>
                <form action="${gameUrl}" method="post">
                    <td align="right" colspan="2"><input type="submit" value="Join a game" class="btn btn-primary btn-sm"/></td>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
				<!-- <td align="right">
					<a href="<c:url value="/board" />">Join a game</a>
				</td> -->
			</tr>
		</table>
		<br /> <br />
		<c:if test="${newPlayer eq 'Y' || newPlayer eq 'y'}">
			<table style="width: 100% border: 0">
				<tr>
					<td align="left" colspan="3">Confirmation message : ${success}</td>
   				</tr>
   			</table>
		</c:if>

		<c:if test="${newPlayer ne 'Y' && newPlayer ne 'y'}">
			<table style="width: 100%" class="bord">
				<tr>
					<th class="bord">Player Name</th>
					<th class="bord">Total Wins</th>
					<th class="bord">Total Losses</th>
				</tr>
				<tr>
					<td class="bord"><c:out value="${loggedInPlayerName}"/></td>
					<td class="bord"><c:out value="${playerWins}"/></td>
					<td class="bord"><c:out value="${playerLosses}"/></td>
				</tr>
			</table>

			<br />
			Games details:
			<table id="t01" class="bord">
				<tr>
					<th class="bord">Game Id</th>
					<th class="bord">Players/Scores</th>
					<th class="bord">Won?</th>
				</tr>
				<c:forEach var="item" items="${playersScores}">
					<tr>
						<td class="bord"><a href="<c:url value='/board?oldGameId=${item.gameId}' />"><c:out value="${item.gameId}"/></a></td>
						<td class="bord"><c:out value="${item.playersScores}"/></td>
						<td class="bord"><c:out value="${item.playerWon}"/></td>
					</tr>
				</c:forEach>
			</table>		
	
			Click Game Id to view moves history
			<br />
		</c:if>
	</div>

</body>
</html>