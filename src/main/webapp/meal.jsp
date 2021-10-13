<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<form method="post">
    <input type="hidden" name="mealId" value="${meal.id}"/>
    Date time: <input type="datetime-local" name="dateTime" value="${meal.dateTime}" required/>
    <hr>
    Description: <input type="text" name="description" value="${meal.description}" required/>
    <hr>
    Calories: <input type="number" name="calories" value="${meal.calories}" required>
    <hr>
    <input type="submit" value="Submit"/>
    <input type="button" onclick="window.location.href = 'meals'" value="Cancel">
</form>
</body>
</html>
