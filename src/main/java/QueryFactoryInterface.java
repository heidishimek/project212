import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Interface for QueryFactory for Multithreading
 * 
 * Used instead of extending in both 
 * QueryFactory and ThreadSafeQueryFactory
 * 
 * @author heidishimek
 */
public interface QueryFactoryInterface 
{
	/**
	 * parses query and stems, and adds words to the inverted index
	 * 
	 * @param queries 	query to take in
	 * @param flag 		flag to determine search
	 * @throws IOException exception
	 */	
	default public void parseQuery(Path queries, boolean flag) throws IOException 
	{
		try (BufferedReader reader = Files.newBufferedReader(queries, StandardCharsets.UTF_8))
		{
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				parseQuery(line, flag);
			}
		}
	}
	
	/**
	 * Helper method to parse queries
	 * 
	 * @param line line passed in 
	 * @param flag flag for search
	 */
	public void parseQuery(String line, boolean flag);
	
	/**
	 * Helper JSON method for main
	 * 
	 * @param path path taken in 
	 * @throws IOException exception
	 */
	public void queryJson(Path path) throws IOException;
}
