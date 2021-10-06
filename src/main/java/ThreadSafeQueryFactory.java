import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Multithreaded Query Factory using
 * the QueryFactoryInterface
 * 
 * @author heidishimek
 */
public class ThreadSafeQueryFactory implements QueryFactoryInterface
{		
	/** Store Query **/
	private final TreeMap<String, Collection<InvertedIndex.QueryResult>> query;
	
	/** Inverted Index **/
	private final ThreadSafeInvertedIndex invertedIndex; 

	/** Work Queue **/
	private final WorkQueue workQueue;
	
	/**
	 * Initializes a thread-safe query
	 * @param invertedIndex index 
	 * @param workQueue work queue
	 *
	 */
	public ThreadSafeQueryFactory(ThreadSafeInvertedIndex invertedIndex, WorkQueue workQueue)
	{
		this.invertedIndex = invertedIndex;
		this.query = new TreeMap<>();
		this.workQueue = workQueue;
	}
	
	@Override
	public void parseQuery(Path queries, boolean flag) throws IOException
	{
		QueryFactoryInterface.super.parseQuery(queries, flag);
		workQueue.finish();
	}
	
	@Override 
	public void parseQuery(String line, boolean flag)
	{
		workQueue.execute(new Task(line, flag));
	}
	
	@Override
	public void queryJson(Path path) throws IOException
	{
		synchronized (query)
		{
			SimpleJsonWriter.queryJson(query, path);
		}
	}
	
	/**
	 * The non-static task class that will update the shared paths and pending
	 * members in our task manager instance.
	 */
	private class Task implements Runnable 
	{
		/** The path to add or list. */
		private final String line;
		
		/** The flag to add or list. */
		private final boolean flag;

		/**
		 * Initializes this task.
		 * @param line line to parse
		 * @param flag boolean flag
		 */
		public Task(String line, boolean flag) 
		{
			this.line = line;
			this.flag = flag;
		}

		@Override
		public void run() 
		{
			TreeSet<String> lines = TextFileStemmer.uniqueStems(line);
			String word = String.join(" ", lines);


			synchronized (query)
			{
				if (query.containsKey(word) || word.isBlank())
				{
					return;
				}
			}
			
			var local = invertedIndex.search(lines, flag);
			
			synchronized (query)
			{
				query.put(word, local);
			}
		}
	}
}