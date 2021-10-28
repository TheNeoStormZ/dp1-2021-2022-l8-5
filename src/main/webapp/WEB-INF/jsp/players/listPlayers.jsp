<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Players</h2>

    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
         	<th style="width: 150px;">ID</th>
            <th style="width: 150px;">Username</th>
            <th style="width: 150px;">Total Points</th>
            <th style="width: 150px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${players}" var="player">
            <tr>
             <td>
                    <c:out value="${player.id}"/>
                </td>
                <td>
                    <c:out value="${player.username}"/>
                </td>
                
                 <td>
                    <c:out value="${player.totalPoints}"/>
                </td>
                 
				<td> 
                    <spring:url value="/players/delete/{playerId}" var="playerUrl">
                        <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(playerUrl)}"><span class=" glyphicon glyphicon-trash" aria-hidden="true"></span>
                    <span>Delete</span></a>
                    
                    <spring:url value="/players/update/{playerId}" var="editUrl">
       					 <spring:param name="playerId" value="${player.id}"/>
    				</spring:url>
    				 <a href="${fn:escapeXml(editUrl)}"><span class=" glyphicon glyphicon-edit" aria-hidden="true"></span>
							<span>Update</span></a>
                    
				</tr>
               
            </tr>
            
        </c:forEach>
        </tbody>
    </table>

	
</petclinic:layout>