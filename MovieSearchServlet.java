// Handle the query server-side

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


public class MovieSearchServlet extends HttpServlet {

    static Connection conn;
    static Statement statement;

    // All the movies. For each movie: title and description.
    static ArrayList<Movie> movies = new ArrayList<>();

    // A map from a word to all the data we need for that word.
    // This is really "The Index".
    static HashMap<String, IndexEntry> index = new HashMap<>();
   

    public MovieSearchServlet ()
    {
	// NOTE: this is where we set up a connection to the dbase.
	// There's a single connection for the life of this servlet,
	// which is opened when the MovieSearchServlet class instance
	// is first created.
    readMovieData ();
	buildIndex ();
	try {
        Class.forName ("org.h2.Driver");
	    conn = DriverManager.getConnection (
		"jdbc:h2:~/Desktop/myservers/databases/flight", 
                "sa", 
	        ""
        );
	    statement = conn.createStatement();
	    System.out.println ("MovieSearchServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }
	 
    public void doPost (HttpServletRequest req, HttpServletResponse resp) 
    {
	// We'll print to terminal to know whether the browser used post or get.
	System.out.println ("MovieSearchServlet: doPost()");
	handleRequest (req, resp);
    }
    

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
    {
	System.out.println ("MovieSearchServlet: doGet()");
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
	    out.println ("<head><title> Results </title></head>");
	    out.println ("<body bgcolor=\"#DDDDFF\">");
	    out.println("<h1> Movie Search </h1> <form action=\"moviequery\" method=\"post\">");
        out.println("Enter search word: <input type=\"text\" name=\"word\">");
        out.println("<input type=\"submit\" value=\"Go\">");
        out.println("</form>");
	    out.println ("<h2>Query Results</h2>");
	    
	    // Find out which button was clicked.
	    String word = req.getParameter ("word");
	    System.out.println ("MovieSearchServlet: word=" + word);

	    String tableHTML = "";

	    tableHTML= getData(word);

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


    String getData ( String word)
    {
	try {

	    // We're going to make the HTML rather simple (unformatted):
	    String htmlString = "<pre>\n";


		IndexEntry entry = index.get (word);

		if (entry == null) {
		    htmlString+="NOT FOUND";
		}

		if (entry.titleMovies.size() > 0) {
		    htmlString+="OCCURRENCE IN TITLES:\n <ul>\n";
		    // Why do we need this arraylist?
		    ArrayList<String> results = new ArrayList<>();

		    String row = "";
		    for (Movie m: entry.titleMovies) {
			if (! results.contains(m.title) ) {
				row += "<li> " + m.title+ "</li>\n";
			    System.out.println (" -> " + m.title);
			    results.add (m.title);
			}
		    }
		    htmlString+=row+ "</ul>\n";

		}

		if (entry.descriptionMovies.size() > 0) {
		    
		    htmlString+="OCCURRENCE IN DESCRIPTION:\n <ul>\n";
		    ArrayList<String> results = new ArrayList<>();
		    String row = "";

		    for (Movie m: entry.descriptionMovies) {
			if (! results.contains(m.title) ) {
				row += "<li> " + m.title+ "</li>\n";
			    System.out.println (" -> " + m.title);
			    results.add (m.title);
			}
		    }
		    htmlString+=row+ "</ul>\n";
		}
	
	    htmlString += "</pre>\n";
	    return htmlString;
	}
	catch (Exception e) {
	    e.printStackTrace ();
	    return null;
	}
    }

    public static void readMovieData ()
    {
	movies = MovieUtil.readMovieData ();
    }


    public static void buildIndex ()
    {
	for (Movie m: movies) {

	    // Handle all the words in the title
	    for (String word: MovieUtil.extractWords(m.title)) {
		word = word.toLowerCase();
		IndexEntry entry = index.get (word);
		if (entry != null) {
		    entry.titleMovies.add (m);   
		}
		else {
		    entry = new IndexEntry ();   // First occurrence of word.
		    entry.word = word;
		    entry.titleMovies.add (m);
		    index.put (word, entry);
		}
	    }

	    // Handle the words in the description in the same way.
	    for (String word: MovieUtil.extractWords(m.description)) {
		word = word.toLowerCase();
		IndexEntry entry = index.get (word);
		if (entry != null) {
		    entry.descriptionMovies.add (m);
		}
		else {
		    entry = new IndexEntry ();
		    entry.word = word;
		    entry.descriptionMovies.add (m);
		    index.put (word, entry);
		}
	    }

	}

	System.out.println ("Index constructed for " + movies.size() + " movies");
    }


}