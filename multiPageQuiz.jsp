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
ArrayList<String> question_ids = (ArrayList<String>)request.getAttribute("question_ids");
int question_number = (Integer)request.getAttribute("question_number");
ArrayList<String> answers = (ArrayList<String>)request.getAttribute("answers");
String gradeNote = (String)request.getAttribute("gradeNote");
String submitAction = (String)request.getAttribute("submitAction");
String disabled;
if(quiz.getPracticeFlag()) disabled = "";
else disabled = "DISABLED";
%>
<%-- The disabled field is not currently used, but could prove useful if we put
in ability to cycle through previous and next questions --%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=quiz.getQuiz_name() %>: Question <%=question_number %></title>
</head>
<body>
<h4><%= quiz.getQuiz_name() %></h4>
<b><%=question_number %>.</b> <br />
<%
Question question = new Question(question_ids.get(question_number));
out.println(question.getHTML());
out.println(gradeNote);
%>
<%-- The above again assumes the functionality from the Question class
Additionally, as a note on gradeNote, that is a String of HTML that is set
by the MultipageQuizServlet that will appropriately display the grading section
of the page. 
Additionally, maybe have functionality to disable ability to input answer to question 
when not on "Submit" page? Also, could redisplay text that user input - just use answer
parameter?
--%>
<br />
<form action="MultipageQuizServlet" method="post">
<p><input type="submit" value="<%= submitAction%>"/></p>
</form>
</body>
</html>