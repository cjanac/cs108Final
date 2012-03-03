<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Login</title>
</head>
<body>
<h1>Either your password does not match user id or the user id you entered does not exist.</h1>
<p>Please enter your correct password or create a new account.</p>
<form action="UserServlet" method="post">
<p>UserId: <input type="text" name="user_id" /></p>
<p>Password: <input type="text" name="password" /></p>
<p><input type="submit" /></p>
</form>
<p><a href="createNewAccount.html">Create New Account</a></p>
</body>
</html>