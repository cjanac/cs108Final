<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="finalWebProject.src.web.Quiz" %>
    <%@ page import="finalWebProject.src.web.User" %>
    <%@ page import="finalWebProject.src.web.Question" %>
    <%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
Quiz quiz = (Quiz)request.getAttribute("quiz");
ArrayList<Question> questions = (ArrayList<Question>)request.getAttribute("questions");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=quiz.getQuiz_name() %></title>
</head>
<body>
<h1><%= quiz.getQuiz_name() %></h1>
<%
int size = questions.size();
for(int i = 0; i < size; i++){
	out.print((i+1) + ". ");
	out.println(questions.get(i).getHTML());
}
%>
<%-- The above is assuming that the Question class exports some
method that returns in string form the HTML necessary to display the question
Will need to talk to Elliott about how to deal with form parameters being set in
this situation and how Question will handle this - maybe getHTML() takes in an
integer question number in order to set parameter for post as "question" + num?--%>
<form action="QuizSinglePageResultsServlet" method="post">
<p><input type="submit" value="Submit Quiz"/></p>
</form>
</body>
</html>