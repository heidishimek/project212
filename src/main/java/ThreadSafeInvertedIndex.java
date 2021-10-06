import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Thread Safe Inverted Index
 * 
 * @author heidishimek
 *
 */
public class ThreadSafeInvertedIndex extends InvertedIndex
{
	/** The lock used to protect concurrent access to the underlying set. */
	private final SimpleReadWriteLock lock;
	
	/**
	 * Initializes a thread-safe Inverted Index
	 */
	public ThreadSafeInvertedIndex()
	{
		super();
		lock = new SimpleReadWriteLock();
	}
	
	@Override
	public void add(String word, String inputFile, Integer counter) throws IOException
	{
		lock.writeLock().lock();
		try
		{
			super.add(word, inputFile, counter);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void indexToJson(Path path) throws IOException
	{
		lock.readLock().lock(); 
		try
		{
			super.indexToJson(path);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public boolean contains(String word)
	{
		lock.readLock().lock(); 
		try
		{
			return super.contains(word);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public boolean contains(String word, String location)
	{
		lock.readLock().lock(); 
		try
		{
			return super.contains(word, location);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public boolean contains(String word, String location, int position)
	{
		lock.readLock().lock();
		try
		{
			return super.contains(word, location, position);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public Set<String> getLocations(String word)
	{
		lock.readLock().lock();
		try
		{
			return super.getLocations(word);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public Set<String> getWords()
	{
		lock.readLock().lock();
		try
		{
			return super.getWords();
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public Set<Integer> getPositions(String word, String location)
	{
		lock.readLock().lock();
		try
		{
			return super.getPositions(word, location);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public int wordsSize()
	{
		lock.readLock().lock();
		try
		{
			return super.wordsSize();
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public int locationSize(String word)
	{
		lock.readLock().lock();
		try
		{
			return super.locationSize(word);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public int positionsSize(String word, String location)
	{
		lock.readLock().lock();
		try
		{
			return super.positionsSize(word, location);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public String toString()
	{
		return super.toString();
	}
	
	@Override
	public void addAll(List<String> words, String input) throws IOException
	{
		lock.writeLock().lock();
		try
		{
			super.addAll(words, input);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public Collection<InvertedIndex.QueryResult> exactSearch(Collection<String> queries) 
	{
		lock.readLock().lock();
		try
		{
			return super.exactSearch(queries);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public Collection<InvertedIndex.QueryResult> partialSearch(Collection<String> queries) 
	{
		lock.readLock().lock();
		try
		{
			return super.partialSearch(queries);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void countJson(Path path) throws IOException
	{
		lock.readLock().lock();
		try
		{
			super.countJson(path);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void addAll(InvertedIndex local)
	{
		lock.writeLock().lock();
		try
		{
			super.addAll(local);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
}