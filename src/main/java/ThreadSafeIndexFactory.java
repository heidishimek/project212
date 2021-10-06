import java.io.IOException;
import java.nio.file.Path;

/**
 * Multithreaded Index Factory
 * 
 * @author heidishimek
 *
 */
public class ThreadSafeIndexFactory extends IndexFactory
{
	/** Inverted Index **/
	public final ThreadSafeInvertedIndex invertedIndex;

	/** Work Queue **/
	private final WorkQueue workQueue; 
	
	/**
	 * Initializes a thread-safe query
	 * @param invertedIndex index
	 * @param workQueue work queue
	 *
	 */
	public ThreadSafeIndexFactory(ThreadSafeInvertedIndex invertedIndex, WorkQueue workQueue) 
	{
		super(invertedIndex);
		this.invertedIndex = invertedIndex;
		this.workQueue = workQueue;
	}
	
	@Override
	public void parseText(Path inputFile) throws IOException
	{
		workQueue.execute(new Task(inputFile));
	}
	
	@Override
	public void parseSelector(Path input) throws IOException
	{
		super.parseSelector(input);
		workQueue.finish();
	}
	
	/*
	 * The non-static task class that will update the shared paths and pending
	 * members in our task manager instance.
	 */
	/**
	 * @author heidishimek
	 *
	 */
	private class Task implements Runnable 
	{
		/** The path to add or list. */
		private final Path path;

		/**
		 * Initializes this task.
		 *
		 * @param path the path to add or list
		 */
		public Task(Path path) 
		{
			this.path = path;
		}

		@Override
		public void run() 
		{
			InvertedIndex local = new InvertedIndex();
			try 
			{
				IndexFactory.parseText(path, local);
				invertedIndex.addAll(local);
			} 
			catch (IOException e) 
			{
				System.out.println("Unable to parse " + path + "and add all to " + local);
			}
		}
	}
}