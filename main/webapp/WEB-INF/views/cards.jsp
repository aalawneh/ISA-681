<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

 <ul class="hand">
	<c:choose>
		<c:when test="${back == 1 || quorum == 0}">
	    	<c:forEach var="item" items="${game.playerCards}">
				<li>
					<label class="card back" style="pointer-events: none;">
						<span class="rank">-</span>
		    			<span class="suit">Joker</span>
					</label>
				</li>
			</c:forEach>
		</c:when>
		<c:when test="${back ne 1 && quorum ne 0}">
			<c:forEach var="item" items="${game.playerCards}">
				<li>
					<%-- did you trash cards --%>
					<c:choose>
						<%-- no I did not --%>						
						<c:when test="${playerTrashCards eq false}">
							<label class="card rank-<c:out value="${item}" />" style="pointer-events: none;">
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
							<br/>
							<c:set var="labelCard" value="${fn:replace(item,' ', '')}" />
							<input type="checkbox" id="cardsCheckBox" name="cardsCheckBox" value="<c:out value='${item}' />" />
						</c:when>
						<%-- yes I did --%>
						<c:otherwise>							
							<%-- did all trash cards --%>
							<c:choose>
								<%-- no! then wait --%>
								<c:when test="${allPlayersTrashCards eq false}">
											<label for="card"  class="card rank-<c:out value="${item}" />" style="pointer-events: none;">
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
											<br/>																				
											<input type="checkbox" value="<c:out value='${item}' />" <c:if test='${(item eq firstCard) || (item eq secondCard) || (item eq thirdCard)}'>checked="checked"</c:if> disabled="disabled"/>
								</c:when>
								<%-- yes all did --%>
								<c:otherwise>
									<%-- is it your turn --%>										
									<c:choose>
										<%-- yes it is --%>
										<c:when test="${game.whoseTurnId == loggedInPlayerId}">
											<!--
											The below method can be used for GET.  We currently have a POST approach selected.
											<a class="card rank-<c:out value="${item}" />" href="<c:url value="/play?gameId=${game.gameId}&cardId='${item}'" />">
											-->
											<a class="card rank-<c:out value="${item}" />" href="javascript: submitCard('${item}')">
											
											<!--<form:input type="hidden" value="${item}" th:field="*{cardId}" />-->
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
											</a>
											
										</c:when>
										<%-- no it is not my turn --%>
										<c:otherwise>
											<label class="card rank-<c:out value="${item}" />" style="pointer-events: none;">
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
										</c:otherwise>											
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
		    <c:if test="${game.whoseTurnId == loggedInPlayerId}">
			    <form method="POST" id="theForm" action="board" modelAttribute="gameBoard" name="cardForm" >
			        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			        <input type="hidden" name="gameId" value="${game.gameId}"/>
			        <input type="hidden" name="cardId" value=""/>
			    </form>
		    </c:if>
		</c:when>
	</c:choose>	
</ul>
<c:if test="${playerTrashCards eq false && back ne 1 && quorum ne 0}">
	<form method="POST" id="trashCardsForm" action="trashCards" modelAttribute="gameBoard" name="cardForm" >
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="trashCards" id="trashCards" value=""/>
        <input type="hidden" name="gameId" value="${game.gameId}"/>
	</form>
</c:if>