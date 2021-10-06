import java.net.URL;

//import java.util.logging.LogManager;
//import org.apache.commons.text.StringEscapeUtils;
//import org.apache.commons.text.StringSubstitutor;
//import org.apache.logging.log4j.Logger;

/**
 * @author heidishimek
 *
 */
public class WebCrawler 
{
	/** Inverted Index **/
	private final ThreadSafeInvertedIndex invertedIndex; 

	/** Work Queue **/
	private final WorkQueue workQueue;
	
	/**
	 * Logger
	 */
//	private static Logger log = LogManager.getLogger();
	
	/**
	 * Constructor
	 * 
	 * @param invertedIndex index passed in
	 * @param workQueue queue
	 */
	public WebCrawler(ThreadSafeInvertedIndex invertedIndex, WorkQueue workQueue)
	{
		this.workQueue = workQueue;
		this.invertedIndex = invertedIndex;
	}
	
	/**
	 * @param url url 
	 * @param index index
	 */
	public static void crawl(URL url, ThreadSafeInvertedIndex index)
	{
//		addUrl(url, index);
//		workQueue.execute(new Task());
		
	}
	
	/**
	 * @param url  link
	 * @param html html
	 * 
	 */
	private static void addUrl(URL url, String html) 
	{
		String link = url.toString();
		String[] parsed = TextParser.parse(html);
		for (String word : parsed)
		{
			
		}
		
	}

	/**
	 * The non-static task class that will update the shared paths and pending
	 * members in our task manager instance.
	 */
	private class Task implements Runnable 
	{
		private final URL url;
		
		/**
		 * Initializes Task
		 */
		public Task(URL url)
		{
			this.url = url;
		}

		@Override
		public void run() 
		{
			
		}
	}
}
