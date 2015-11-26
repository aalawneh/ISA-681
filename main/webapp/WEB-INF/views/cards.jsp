<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

 <ul class="hand">
	<c:choose>
	    <c:when test="${back == 1 || quorum == 0}">
	    	<c:forEach var="item" begin="1" end="13">
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
					<c:choose>
						<c:when test="${game.whoseTurnId == loggedInPlayerId}">
							<a class="card rank-<c:out value="${item}" />" href="<c:url value="/play?gameId=${game.gameId}&cardId='${item}'" />">
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
				</li>
			</c:forEach>
		</c:when>
	</c:choose>	
</ul>