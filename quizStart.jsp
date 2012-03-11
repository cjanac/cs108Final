<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="finalWebProject.src.web.Quiz" %>
    <%@ page import="finalWebProject.src.web.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
Quiz quiz = new Quiz(request.getParameter("quiz_id"));
request.setAttribute(quiz, "quiz");
User creator = new User(quiz.getCreator_id());
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title><%=quiz.getQuiz_name()  %></title>
</head>
<body>
<h1>Welcome to <%=quiz.getQuiz_name()  %>!</h1>
<p>Created by: <%= creator.getName() %></p>
<%-- The above assumes some way to get the name of a user through a public method with a User object 
Additionally, will need to link to the creator's user page - will need to talk to Charlie about
how best to do that--%>
<p>Description: </p>
<p><%= quiz.getDescription() %></p>
<form action="UserServlet" method="post">
<p><input type="submit" value="Cancel"/></p>
<%-- The above assumes that the user_id and password parameters are still stored in the request at this point in time --%>
</form>
<form action = "QuizServlet" method = "post">
<p><input type="submit" value="Take"/></p>
</form>
</body>
</html>