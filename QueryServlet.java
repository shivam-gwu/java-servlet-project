// Handle the query server-side

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


public class QueryServlet extends HttpServlet {

    static Connection conn;
    static Statement statement;

    public QueryServlet ()
    {
	// NOTE: this is where we set up a connection to the dbase.
	// There's a single connection for the life of this servlet,
	// which is opened when the QueryServlet class instance
	// is first created.
	try {
        Class.forName ("org.h2.Driver");
	    conn = DriverManager.getConnection (
		"jdbc:h2:~/Desktop/myservers/databases/flight", 
                "sa", 
	        ""
        );
	    statement = conn.createStatement();
	    System.out.println ("QueryServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }
	 
    public void doPost (HttpServletRequest req, HttpServletResponse resp) 
    {
	// We'll print to terminal to know whether the browser used post or get.
	System.out.println ("QueryServlet: doPost()");
	handleRequest (req, resp);
    }
    

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
    {
	System.out.println ("QueryServlet: doGet()");
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
	    out.println ("<head><title> Results </title> <style> table { border: 1px solid #ccc; padding: 20px; border-radius: 5px; } td { padding: 10px; } </style></head>");
	    out.println ("<body bgcolor=\"#DDDDFF\">");
	    out.println ("<h2>Query Results</h2>");
	    
	    // Find out which button was clicked.
	    String whichButton = req.getParameter ("mybuttons");
	    System.out.println ("PassengerServlet: whichButton=" + whichButton);

	    String passengerTableHTML = "";

	    HttpSession session = req.getSession();
		String username = (String) session.getAttribute("username");

	    // The actual results after pulling from the database
	    if (whichButton.equalsIgnoreCase("Add")) {
		String fromApt = req.getParameter ("from");
		String toApt = req.getParameter ("to");
		// WRITE YOUR CODE IN THE addPassengerData() method.
		passengerTableHTML = addPassengerData (username,fromApt,toApt);
	    }
	    else if (whichButton.equalsIgnoreCase("Go")) {
		// WRITE YOUR CODE IN THE makePassengerTable() method.
		passengerTableHTML = showPassengerData (username);
	    }
	    else {
		out.println ("<h2> Unknown query</h2>");
	    }

		out.println (passengerTableHTML);

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


    String addPassengerData ( String userName, String fromApt, String toApt)
    {
	try {
		String sql = "select f.flight_id from flight f where start_apt='"+fromApt+"' and end_apt='"+toApt+"'";
		ResultSet rs = statement.executeQuery (sql);

		int flightId=0;

		while(rs.next()){
			flightId=Integer.parseInt(rs.getString(1));
		}

		sql = "select max(p.pid) from passenger p";
		rs = statement.executeQuery (sql);

		int pId=0;

		while(rs.next()){
			pId=Integer.parseInt(rs.getString(1)) +1;
		}


		sql="insert into passenger values("+pId+", '"+userName+"', "+flightId+", 200)";

		statement.executeUpdate(sql);

		String htmlString= showPassengerData(userName);


		return htmlString;

	    
	}
	catch (Exception e) {
	    e.printStackTrace ();
	    return null;
	}
    }


    String showPassengerData (String userName)
    {
	try {

		String sql = "select flight_num,start_apt,end_apt from flight where flight_id in "+
		"(SELECT flight_id FROM passenger where name='"+userName+"')";
	    ResultSet rs = statement.executeQuery (sql);
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numColumns = rsmd.getColumnCount();

	    // We're going to make the HTML rather simple (unformatted):
	    String htmlString = "<table>\n<tr>\n<th>Flight Number</th>\n<th> Start Apt Code</th>\n<th>End Apt Code</th>\n</tr>";
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