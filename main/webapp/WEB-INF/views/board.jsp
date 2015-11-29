<!--
 * Example HTML for CSS Playing Cards
 *
 * @author   Anika Henke <anika@selfthinker.org>
 * @license  CC BY-SA [http://creativecommons.org/licenses/by-sa/3.0]
 * @version  2011-06-14
 * @link     http://selfthinker.github.com/CSS-Playing-Cards/
-->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


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
	<style>
	    #table_wrapper{background:tomato;border:1px solid olive;float:left;}
	    #tbody{height:100%;min-height: 100px;overflow-y:auto;width:100%;background:yellow;white-space:nowrap;}
        table{border-collapse:collapse; width:100%;}
        td{padding:1px 5px; /* pixels */
        border-right:1px solid red; /* to avoid the hacks for the padding */
        border-bottom:1px solid red;} 
        .td1{width:25%;border-right:1px solid orange;}
        .td2{width:25%;border-right:1px solid orange;}
        .td3{width:25%;border-right:1px solid orange;} /* optional */
	    .td4{border-right-width:0;}
	    #header{width:100%;overflow-y:auto;overflow-x:hidden;background:DodgerBlue;border-bottom:1px solid black;white-space:nowrap;}
  	    #header div{float:left;}
        #header #head1{width:25%;border-right:1px solid orange;}  
        #header #head2{width:25%;border-right:1px solid orange;}  
        #header #head3{width:25%;border-right:1px solid orange;}
        #header #head4{border-right-width:0;}
	
	table.bord, th.bord, td.bord {
		border: 1px solid black;
		border-collapse: collapse;
	}
	th.bord, td.bord {
		padding: 5px;
		text-align: left;
		width: 25%;
	}
	table#t01 {
		width: 100%;
		background-color: #f1f1c1;
	}
	</style>
</head>
<body>
	<table border="1" width="100%">
		<tr valign="top">
			<td width="30%">
				<table>
					<tr valign="top">
						<td>
							<table border="0">
								<tr>
										Welcome ${loggedInPlayerSso}!
										&nbsp;&nbsp;&nbsp;
										<a href="<c:url value="/home" />">Home</a>
										&nbsp;&nbsp;&nbsp;
										<a href="<c:url value="/board" />">Refresh board</a>
										&nbsp;&nbsp;&nbsp;
										<a href="<c:url value="/logout" />">Logout</a>
								</tr>
							</table>
						</td>									
					</tr>
					<tr>
						<td>
							Game ${game.gameId} Status: &nbsp;&nbsp;&nbsp;
                            				<c:choose> 
                            					<c:when test="${game.gameStatus eq 'W'}"><div style="color: red;">Waiting for More Players...</div></c:when>
                                				<c:when test="${game.gameStatus eq 'S'}"><div style="color: green;">In Progress...</div></c:when>
                                				<c:when test="${game.gameStatus eq 'D'}"><div style="color: red;">Shuffling and dealing the cards...</div></c:when>
                                				<c:when test="${game.gameStatus eq 'C'}"><div style="color: red;">Calculating scores for hand...</div></c:when>
                            					<c:when test="${game.gameStatus eq 'O'}">Complete.</c:when>
                            				</c:choose>
						</td>
					</tr>
					<tr>
						<td>
							Game ${game.gameId} Score: &nbsp;&nbsp;&nbsp;
							<table style="width: 100%" class="bord">
								<tr>
									<th class="bord"><c:out value="${loggedInPlayerSso}"/></th>
									<c:forEach var="item" items="${game.opponents}">
										<th class="bord"><c:out value="${item.playerSso}"/></th>
									</c:forEach>
									<c:forEach begin="1" end="${oppSize}">
										<th class="bord">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> 
									</c:forEach>
								</tr>
								<tr>										
									<td><c:out value="${game.playerScore}"/></td>
									<c:forEach var="item" items="${game.opponents}">
										<td class="bord"><c:out value="${item.score}"/></td>
									</c:forEach>
									<c:forEach begin="1" end="${oppSize}">
										<td class="bord">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
									</c:forEach>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
					    <td>
							Game ${game.gameId} History: &nbsp;&nbsp;&nbsp;
					        <div id="table_wrapper" style="width: 100%">
                                <div id="header">
                                    <div id="head1">Player</div>
                                    <div id="head2">Hand</div>
                                    <div id="head3">Round</div>
                                    <div id="head4">Card</div>
                                </div>
                                <div id="tbody">
                                    <table>
			                            <c:forEach var="item" items="${gameMoves}">
				                            <tr>
					                            <td class="td1" nowrap="nowrap"><c:out value="${item.playerName}"/></td>
					                            <td class="td2"><c:out value="${item.handId}"/></td>
					                            <td class="td3"><c:out value="${item.roundId}"/></td>
					                            <td class="td4">
						                            <label class="card rank-<c:out value="${item.cardId}" />" style="pointer-events: none;">
							                            <c:choose>
							                                <c:when test="${fn:startsWith(item.cardId, '10')}">
									                            <span class="rank"><c:out value="${fn:substring(item.cardId, 0, 2)}" /></span>
									                            <span class="suit"><c:set var="end" value="${fn:length(item.cardId)}" />&<c:out value="${fn:substring(item.cardId, 3, end)}" />;</span>
							                                </c:when>
							                                <c:otherwise>
									                            <span class="rank"><c:set var="caps" value="${fn:substring(item.cardId, 0, 1)}" /><c:out value="${fn:toUpperCase(caps)}" /></span>
									                            <span class="suit"><c:set var="end" value="${fn:length(item.cardId)}" />&<c:out value="${fn:substring(item.cardId, 2, end)}" />;</span>
							                                </c:otherwise>
							                            </c:choose>		
						                            </label>
					                            </td>
				                            </tr>
			                            </c:forEach>
                                    </table>
                                </div>
                            </div>
					    </td>
					</tr>
				</table>			
			</td>
			<td width="70%">
				<!-- div class="playingCards fourColours rotateHand"-->
				<div class="playingCards fourColours">
					<table border="0" width="100%">
						<tr valign="top">
							<td width="33%" height="3.6em;" nowrap="nowrap" align="left">
							    Player's turn: <c:out value="${game.whoseTurnName}"/> <br>
							    ${game.gameMsg} 
							</td>
							<td width="33%" height="3.6em;" nowrap="nowrap">
								<c:choose>
								    <c:when test="${game.playerPosition eq 0}">
								       <c:out value="${loggedInPlayerSso}"/>
									   		
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 0}">
										       <c:out value="${game.opponents[0].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 0}">
										       <c:out value="${game.opponents[1].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 0}">
										       <c:out value="${game.opponents[2].playerSso}"/>
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
								       <c:out value="${loggedInPlayerSso}"/>
			
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 3}">
										       <c:out value="${game.opponents[0].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 3}">
										       <c:out value="${game.opponents[1].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 3}">
										       <c:out value="${game.opponents[2].playerSso}"/>
										    </c:when>
										</c:choose>					        
			
			<c:set var="back" scope="session" value="1"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:forEach var="item" items="${game.cardsInRound}"  varStatus="myIndex">
									<label class="card rank-<c:out value="${item}" />" style="<c:if test='${myIndex.index eq 1}'>bottom</c:if><c:if test='${myIndex.index eq 2}'>top</c:if>: 2em; pointer-events: none;">
										<c:choose>
										    <c:when test="${fn:startsWith(item, '10')}">
												<span class="rank"><c:out value="${fn:substring(item, 0, 2)}" /></span>
												<span class="suit"><c:set var="end" value="${fn:length(item)}" />&<c:out value="${fn:substring(item, 3, end)}" />;</span>
										    </c:when>
										    <c:otherwise>
												<span class="rank"><c:set var="caps" value="${fn:substring(item, 0, 1)}" /><c:out value="${fn:toUpperCase(caps)}" /></span>
												<span class="suit"><c:set var="end" value="${fn:length(item)}" />&<c:out value="${fn:substring(item, 2, end)}" />;</span>
										    </c:otherwise>
										</c:choose>		
									</label>								
								</c:forEach>
							</td>
							<td>
								<c:choose>
								    <c:when test="${game.playerPosition eq 1}">
								       <c:out value="${loggedInPlayerSso}"/>
								       
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 1}">
										       <c:out value="${game.opponents[0].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 1}">
										       <c:out value="${game.opponents[1].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 1}">
										       <c:out value="${game.opponents[2].playerSso}"/>
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
								       <c:out value="${loggedInPlayerSso}"/>
			
			<c:set var="back" scope="session" value="0"/>
			<c:import url="/WEB-INF/views/cards.jsp"></c:import>
			
								    </c:when>
								    <c:otherwise>
										<c:choose>
										    <c:when test="${game.opponents[0].position eq 2}">
										       <c:out value="${game.opponents[0].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[1].position eq 2}">
										       <c:out value="${game.opponents[1].playerSso}"/>
										    </c:when>
										    <c:when test="${game.opponents[2].position eq 2}">
										       <c:out value="${game.opponents[2].playerSso}"/>
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
	
	
	<script type="text/javascript">
    function submitCard(cardId)
    {
        //From: http://stackoverflow.com/questions/13380346/is-it-possible-to-have-a-spring-model-object-as-the-value-of-spring-form
        //$('#theForm').attr("action","play?cardId=\'"+cardId+"\'");
        
        //From: http://stackoverflow.com/questions/6912197/change-value-of-input-then-submit-form-in-javascript
        document.forms["cardForm"].cardId.value = "\'" + cardId + "\'";
        document.forms["cardForm"].submit();
    }
    </script>
</body>
</html>
