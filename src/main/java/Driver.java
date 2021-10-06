import java.time.Duration;
import java.time.Instant;
import java.io.IOException;
import java.nio.file.Path;


/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2021
 */
public class Driver {

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		// store initial start time
		Instant start = Instant.now();
		
		ArgumentMap argMap = new ArgumentMap(args);
		
		InvertedIndex invertedIndex = null;
		
		IndexFactory indexFactory = null;
		
		QueryFactoryInterface queryFactory = null;
		
		int threads = 0;
		WorkQueue workQueue = null;
		
		if (argMap.hasFlag("-threads"))
		{	
			threads = argMap.getInteger("-threads", 5);
			
			if (threads < 1)
			{
				threads = 5;
			}
			
			workQueue = new WorkQueue(threads);
			invertedIndex = new ThreadSafeInvertedIndex();
 			ThreadSafeInvertedIndex threadSafe = new ThreadSafeInvertedIndex(); 
 			invertedIndex = threadSafe;
 			indexFactory = new ThreadSafeIndexFactory(threadSafe, workQueue);
 			queryFactory = new ThreadSafeQueryFactory(threadSafe, workQueue);
		}
		
		else 
		{
			invertedIndex = new InvertedIndex();
			indexFactory = new IndexFactory(invertedIndex);
			queryFactory = new QueryFactory(invertedIndex);
		}
		
		if (argMap.hasFlag("-html"))
		{
			
		}
		
		if (argMap.hasFlag("-text"))
		{
			if (argMap.getPath("-text") != null)
			{
		
				Path input = Path.of(argMap.getString("-text"));
				
				try
				{	
					indexFactory.parseSelector(input);
				}
			
				catch (IOException e) 
				{
					System.out.println("Unable to build the inverted index from the -text value of:" + input.toString());
				}
			}
			else
			{
				System.out.println("Unable to build the inverted index: -text value provided without path value");
			}
		}
		
		if (argMap.hasFlag("-index"))
		{
			Path output = argMap.getPath("-index", Path.of("index.json"));
			
			try
			{
				invertedIndex.indexToJson(output);
			}
			
			catch (IOException e)
			{
				System.out.println("Unable to build the inverted index with path " + output);	
			}
		}
		
		if (argMap.hasFlag("-counts"))
		{
			Path counts = argMap.getPath("-counts", Path.of("counts.json"));
			
			try
			{
				invertedIndex.countJson(counts);
			}
			
			catch (IOException e)
			{
				System.out.println("Unable to build the inverted index with  " + counts);	
			}
		}
		
		if (argMap.hasFlag("-query"))
		{
			Path query = argMap.getPath("-query");
			
			if (query != null) 
			{
				try
				{
					queryFactory.parseQuery(query, argMap.hasFlag("-exact"));
				}
				
				catch (IOException e)
				{
					System.out.println("Unable to build the inverted index with  " + query);	
				
				}
			}
			else
			{
				System.out.println("No query");
			}
		}
		
		if (argMap.hasFlag("-results"))
		{
			Path results = argMap.getPath("-results", Path.of("results.json"));

			try
			{
				queryFactory.queryJson(results);
			}
			
			catch (IOException e)
			{
				System.out.println("Unable to build the inverted index with  " + results);	
			}
		}
		
		if (workQueue != null)
		{
			workQueue.shutdown();
		}

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}
