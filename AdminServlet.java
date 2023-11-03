import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class AdminServlet extends HttpServlet { 
static final boolean debug = false; 
public void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
} 
public void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// We need to implement POST because the forms are written as such.
	 	

	String username = req.getParameter("username");
    String password = req.getParameter("password");

    // Hardcoded username and password for example purposes
    String validUsername = "admin";
    String validPassword = "123";

    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();

    out.println("<html> <head> <title>JZ Airlines</title> <style> body { display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; } table { border: 1px solid #ccc; padding: 20px; border-radius: 5px; } td { padding: 10px; } </style> </head> <body bgcolor='#DDDDFF'> <font color='#020202'> <h1> JZ Airlines: Admin </h1> ");

    
    if (username.equals(validUsername) && password.equals(validPassword)) {
		out.println("<form action=\"FetchDataServlet\" method=\"post\">");
        out.println("Enter table name: <input type=\"text\" name=\"tableName\">");
        out.println("<input type=\"submit\" value=\"Fetch Data\">");
        out.println("</form>");
    } else {
        // out.println("<p style='color: red;'>Login Failed</p>");
        out.println("<h1> JZ Airlines </h1> <p style='color: red;'>Login Failed</p> <table> <form action=\"http://localhost:40120/admin\" method='post'> <tr> <td colspan='2' style=\"text-align: center;\"> <h2>Admin Login:</h2> </td> </tr> <tr> <td>Login:</td> <td><input type='text' name='username'></td> </tr> <tr> <td>Password:</td> <td><input type='password' name='password'></td> </tr> <tr> <td colspan='2' style=\"text-align: center;\"> <input type='submit' value='login'> </td> </tr> </form> </table> ");
    }

    out.println("</body> </html>");

    
}

}