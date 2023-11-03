import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class CustomerServlet extends HttpServlet { 
	static final boolean debug = false; 

	static Connection conn;
    static Statement statement;

    public CustomerServlet ()
    {
	// NOTE: this is where we set up a connection to the dbase.
	// There's a single connection for the life of this servlet,
	// which is opened when the FetchDataServlet class instance
	// is first created.
	try {
        Class.forName ("org.h2.Driver");
	    conn = DriverManager.getConnection (
		"jdbc:h2:~/Desktop/myservers/databases/flight", 
                "sa", 
	        ""
        );
	    statement = conn.createStatement();
	    System.out.println ("CustomerServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }

	public void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Set the content type of the response.

	} 
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// We need to implement POST because the forms are written as such.
		

	resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
	out.println("<html> <head> <title>JZ Airlines</title> <style> body { display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; } table { border: 1px solid #ccc; padding: 20px; border-radius: 5px; } td { padding: 10px; } </style> </head> <body bgcolor='#DDDDFF'> <font color='#020202'><h1> JZ Airlines:Customer </h1>  ");

	if (req.getParameter("login") != null) {
		String loginname = req.getParameter("loginname");
	    String password = req.getParameter("password");
	    try{
	    String sql = "SELECT c.name FROM customer c where login='"+loginname+"' and password='"+ password+"'";
	    ResultSet rs = statement.executeQuery (sql);
	   
	    String username="";
	    while (rs.next()) {
	    username= rs.getString(1);
		}
		HttpSession session = req.getSession();
		session.setAttribute("username", username);
    	out.println("Welcome "+ username);
    	out.println("<form action=\"QueryServlet\" method=\"post\">");
        out.println("1. List my current flights  <input type=\"submit\" name=\"mybuttons\" value=\"Go\"> <br>  ");
        out.println("2. Add flight from <input type=\"text\" name=\"from\"> to <input type=\"text\" name=\"to\"> <input type=\"submit\" name=\"mybuttons\" value=\"Add\">");
        out.println("</form>");
	    
	    }
		catch (Exception e) {
		    e.printStackTrace ();
		    
		}
        
    } 
    else if (req.getParameter("register") != null) {
    	
    	String username = req.getParameter("username");
		String loginname = req.getParameter("registername");
	    String password = req.getParameter("regpassword");
	    String password2 = req.getParameter("regpassword2");

	    if (password.equals(password2)){
	    try {
	    	
	    	String sql1 = "create table if not exists customer (name varchar(20), login varchar(20), password varchar(20))";
	    	statement.executeUpdate(sql1);
	    	String sql2 = "insert into customer values('"+username+"', '"+loginname+"', '"+password+"')";
	    	statement.executeUpdate(sql2);
	    	HttpSession session = req.getSession();
			session.setAttribute("username", username);
	    	out.println("Welcome "+ username);
	    	out.println("<form action=\"QueryServlet\" method=\"post\">");
	        out.println("1. List my current flights  <input type=\"submit\" name=\"mybuttons\" value=\"Go\"> <br>  ");
	        out.println("2. Add flight from <input type=\"text\" name=\"from\"> to <input type=\"text\" name=\"to\"> <input type=\"submit\" name=\"mybuttons\" value=\"Add\">");
	        out.println("</form>");
	        }
		catch (Exception e) {
		    e.printStackTrace ();
		    
		}
		}  
    } 	

	out.println("</body> </html>");
    
}

}