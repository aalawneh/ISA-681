<!--
 * Example HTML for CSS Playing Cards
 *
 * @author   Anika Henke <anika@selfthinker.org>
 * @license  CC BY-SA [http://creativecommons.org/licenses/by-sa/3.0]
 * @version  2011-06-14
 * @link     http://selfthinker.github.com/CSS-Playing-Cards/
-->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Hearts Playing Cards</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/static/css/cards.css' />" media="screen" />
	<!-- the following js and css is not part of the CSS cards, but only for this example page -->
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
	<script type="text/javascript">
		<!--
			$(document).ready(function() {
				$('.options').addClass('active');
				$('.toggle li').click(function() {
					$('.playingCards').toggleClass($(this).text());
				});
				$('.lang li').click(function() {
					$('html').attr('lang', $(this).text());
					$('html').attr('xml:lang', $(this).text());
				});
			});
		//-->
	</script>
	<style type="text/css"></style>
</head>
<body>
	<table border="1" width="100%">
		<tr>
			<td width="30%" valign="top" align="left">
				<table>
					<tr>
						<td>
							<table border="0" width="100%">
							    <tr>
				                    <td colspan="2"><a href="<c:url value="/home" />">Home</a>    <a href="<c:url value="/logout" />">Logout</a></td>
				                </tr>
				            </table>
							&nbsp;&nbsp;&nbsp;
							<table border="0">
							    <tr>
                                    <th colspan="4" width="100%" nowrap="nowrap">Game ${game.gameId} Status: &nbsp;&nbsp;&nbsp;
                                        <c:choose> 
                                            <c:when test="${game.gameStatus eq 'W'}"><div style="color: red;">Waiting for More Players...</div></c:when>
                                            <c:when test="${game.gameStatus eq 'G'}"><div style="color: green;">In Progress...</div></c:when>
                                            <c:when test="${game.gameStatus eq 'O'}">Complete.</c:when>
                                        </c:choose>
                                    </th>
                                </tr>
						    </table>
							&nbsp;&nbsp;&nbsp;
                            <table>
								<tr>
                                    <td valign="top" align="left" nowrap="nowrap">Score&nbsp;&nbsp;</td>
									<td align="right">
										<table border="1">
											<tr>
												<td><c:out value="${loggedInPlayerName}"/></td>
												<c:forEach var="item" items="${game.opponents}">
													<td><c:out value="${item.firstName} ${item.lastName}"/></td>
												</c:forEach>
												<c:forEach begin="1" end="${oppSize}">
													<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
												</c:forEach>
											</tr>
											<tr>										
												<td><c:out value="${game.playerScore}"/></td>
												<c:forEach var="item" items="${game.opponents}">
													<td><c:out value="${item.score}"/></td>
												</c:forEach>
												<c:forEach begin="1" end="${oppSize}">
													<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
												</c:forEach>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table border="1">
											<tr>
												<td width="100%">Moves History</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td width="70%">
				<!-- div class="playingCards fourColours rotateHand"-->
				<div class="playingCards fourColours">
					<table border="0" width="100%">
						<tr>
							<td width="33%" height="3.6em;" nowrap="nowrap">&nbsp;</td>
							<td width="33%" height="3.6em;" nowrap="nowrap">
								<c:choose>
								    <c:when test="${game.playerPosition eq 0}">
								       <c:out value="${loggedInPlayerName}"/>
									   		
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 0}">
										       <c:out value="${game.opponents[0].firstName} ${game.opponents[0].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 0}">
										       <c:out value="${game.opponents[1].firstName} ${game.opponents[1].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 0}">
										       <c:out value="${game.opponents[2].firstName} ${game.opponents[2].lastName}"/>
										    </c:when>
										</c:choose>					        
			
			<c:set var="back" scope="session" value="1"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:otherwise>
								</c:choose>
							</td>
							<td width="33%" height="3.6em;" nowrap="nowrap">&nbsp;</td>
							<td width="1%" height="3.6em;" nowrap="nowrap">&nbsp;</td>
						</tr>
						<tr>
							<td>
								<c:choose>
								    <c:when test="${game.playerPosition eq 3}">
								       <c:out value="${loggedInPlayerName}"/>
			
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 3}">
										       <c:out value="${game.opponents[0].firstName} ${game.opponents[0].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 3}">
										       <c:out value="${game.opponents[1].firstName} ${game.opponents[1].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 3}">
										       <c:out value="${game.opponents[2].firstName} ${game.opponents[2].lastName}"/>
										    </c:when>
										</c:choose>					        
			
			<c:set var="back" scope="session" value="1"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:otherwise>
								</c:choose>
							</td>
							<td>
								<label for="back" class="card back" style="pointer-events: none;"> 
								</label>
								<label for="back" class="card back" style="bottom: 2em; pointer-events: none;">
								</label>
								<label for="back" class="card back" style="top: 2em; pointer-events: none;">
								</label> 
								<label for="back" class="card back" style="pointer-events: none;"> 
								</label>
							</td>
							<td>
								<c:choose>
								    <c:when test="${game.playerPosition eq 1}">
								       <c:out value="${loggedInPlayerName}"/>
								       
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 1}">
										       <c:out value="${game.opponents[0].firstName} ${game.opponents[0].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 1}">
										       <c:out value="${game.opponents[1].firstName} ${game.opponents[1].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 1}">
										       <c:out value="${game.opponents[2].firstName} ${game.opponents[2].lastName}"/>
										    </c:when>
										</c:choose>					        
			
			<c:set var="back" scope="session" value="1"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:otherwise>
								</c:choose>
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>
								<c:choose>
								    <c:when test="${game.playerPosition eq 2}">
								       <c:out value="${loggedInPlayerName}"/>
			
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 2}">
										       <c:out value="${game.opponents[0].firstName} ${game.opponents[0].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 2}">
										       <c:out value="${game.opponents[1].firstName} ${game.opponents[1].lastName}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 2}">
										       <c:out value="${game.opponents[2].firstName} ${game.opponents[2].lastName}"/>
										    </c:when>
										</c:choose>					        
			
			<c:set var="back" scope="session" value="1"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:otherwise>
								</c:choose>
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>
