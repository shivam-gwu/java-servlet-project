import java.util.*;
import java.io.*;


public class MovieUtil {

    public static String[] extractWords (String line)
    {
	return StringUtil.extractWords (line);
    }

    public static String fixTitle (String line)
    {
	line = line.substring (0, line.length()-4);
	String[] words = StringUtil.extractWordsSeparatedBy(line, '_');
	String title = words[0];
	for (int i=1; i<words.length; i++) {
	    title += " " + words[i];
	}
	return title;
    }

    public static ArrayList<Movie> readMovieData ()
    {
	try {
	    ArrayList<Movie> movies = new ArrayList<>();
	    File movieDir = new File ("movies");
	    for (File f: movieDir.listFiles()) {
		if (f.isFile()) {
		    Movie movie = new Movie ();
		    movie.title = fixTitle(f.getName());
		    LineNumberReader lnr = new LineNumberReader (new FileReader(f));
		    String line = lnr.readLine ();
		    movie.description = "";
		    while (line != null) {
			movie.description += line;
			line = lnr.readLine ();
		    }
		    lnr.close ();
		    movies.add (movie);
		}
	    }

	    System.out.println (movies.size() + " movies processed");
	    return movies;
	}
	catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

}