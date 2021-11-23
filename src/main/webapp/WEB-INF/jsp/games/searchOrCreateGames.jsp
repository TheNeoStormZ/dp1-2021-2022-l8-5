<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dwarf" tagdir="/WEB-INF/tags" %>

<dwarf:layout pageName="searchGames">
    <h2>Games</h2>

    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
			<th style="width: 150px;">Host</th>
			<th style="width: 150px;">Second player</th>
			<th style="width: 150px;">Third player</th>
			<th style="width: 150px;">Star Date</th>
			<th style="width: 150px;">Join !</th>
        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${gamesToJoin}" var="game">
	            <tr>
	            	<td>
	                    <c:out value="${game.firstPlayer.username}"/>
	            	</td>
            
               		<td>
	                	<c:choose>
	                		<c:when test="${empty game.secondPlayer}">
	                    		<c:out value="waiting for player ..."/>
	                		</c:when>
	                		<c:otherwise>
	                    		<c:out value="${game.secondPlayer.username}"/>
	                		</c:otherwise>
	                	</c:choose>
               		</td>
               		
               		<td>
	                	<c:choose>
	                		<c:when test="${empty game.thirdPlayer}">
	                    		<c:out value="waiting for player ..."/>
	                		</c:when>
	                		<c:otherwise>
	                    		<c:out value="${game.thirdPlayer.username}"/>
	                		</c:otherwise>
	                	</c:choose>
               		</td> 
               
           			<td>
            			<c:out value="${game.startDate}"/>
           			</td>
     
					<td> 
						<spring:url value="/games/{gameId}/waitingPlayers" var="joinGameUrl">
							<spring:param name="gameId" value="${game.id}"/>
						</spring:url>
						<a href="${fn:escapeXml(joinGameUrl)}"><span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>
						<span>Join game</span></a>
					</td>
						   
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/games/waitingPlayers" htmlEscape="true"/>'>New game</a>
	</sec:authorize>

	<sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/players2/find" htmlEscape="true"/>'>Find Player</a>
	</sec:authorize>
	
</dwarf:layout>