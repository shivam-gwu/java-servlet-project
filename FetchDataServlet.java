// Handle the query server-side

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


public class FetchDataServlet extends HttpServlet {

    static Connection conn;
    static Statement statement;

    public FetchDataServlet ()
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
	    System.out.println ("FetchDataServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }
	 
    public void doPost (HttpServletRequest req, HttpServletResponse resp) 
    {
	// We'll print to terminal to know whether the browser used post or get.
	System.out.println ("FetchDataServlet: doPost()");
	handleRequest (req, resp);
    }
    

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
    {
	System.out.println ("FetchDataServlet: doGet()");
	handleRequest (req, resp);
    }
    


    public void handleRequest (HttpServletRequest req, HttpServletResponse resp)
    {
	try {
	    resp.setContentType ("text/html");
	    resp.setCharacterEncoding ("UTF-8");

	    PrintWriter out = resp.getWriter();
	    // The top part of the HTML page:
	    out.println ("<html>");
	    out.println ("<head><title> Results </title><style> table { border: 1px solid #ccc; padding: 20px; border-radius: 5px; } td { padding: 10px; } </style></head>");
	    out.println ("<body bgcolor=\"#DDDDFF\">");
	    out.println ("<h2>Query Results</h2>");
	    
	    // Find out which button was clicked.
	    String tableName = req.getParameter ("tableName");
	    System.out.println ("FetchDataServlet: tableName=" + tableName);

	    String tableHTML = "";

	    tableHTML= getData(tableName);

	    out.println (tableHTML);

	    // Un-comment for debugging:
	    // System.out.println ("HTML: [" + passengerTableHTML + "]");

	    // The bottom part, along with the all-important flush().
	    out.println ("</body>");
	    out.println ("</html>");
	    out.flush ();	    
	} 
	catch (Exception e) {
	    e.printStackTrace();
	}
    }


    String getData ( String tableName)
    {
	try {
	    String sql = "SELECT * FROM "+ tableName;
	    ResultSet rs = statement.executeQuery (sql);
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numColumns = rsmd.getColumnCount();
	    String htmlString = "<table>\n<tr>\n";

	    String row2 = "";

	    for (int i=1; i<=numColumns; i++) {
	    	String s = rsmd.getColumnName (i);
		    row2 += "<th>"+ s+ "</th>\n";
	    }
	    htmlString += row2 + "</tr>\n";

	    // We're going to make the HTML rather simple (unformatted):
	    
	    while (rs.next()) {
		String row = "<tr>\n";
		for (int i=1; i<=numColumns; i++) {
		    String s = rs.getString (i);
		    row += "<td>"+ s+ "</td>\n";
		}
		htmlString += row + "</tr>";
	    }
	    htmlString += "</table>\n";
	    return htmlString; 
	}
	catch (Exception e) {
	    e.printStackTrace ();
	    return null;
	}
    }

}