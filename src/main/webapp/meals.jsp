<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<button onclick="window.location.href = 'meals?action=create'">Add Meal</button>
<br>
<hr>
<table border="1">
    <tr>
        <th>Date Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <jsp:useBean id="list" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${list}">
        <c:choose>
            <c:when test="${meal.excess}"><c:set var="rowColor" value="red"/></c:when>
            <c:otherwise><c:set var="rowColor" value="green"/></c:otherwise>
        </c:choose>
        <tr style="color:${rowColor}">
            <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&mealId=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<hr>
</body>
</html>