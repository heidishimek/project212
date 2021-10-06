import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Factory class for Query
 * Adds ability to parse queries
 * 
 * @author heidishimek
 *
 */
public class QueryFactory implements QueryFactoryInterface 
{
	/**
	 * Store query
	 */
	private final TreeMap<String, Collection<InvertedIndex.QueryResult>> query;
	
	/**
	 * Inverted Index
	 */
	private final InvertedIndex index;
	
	
	/**
	 * Constructor for query
	 * @param index index passed in
	 */
	public QueryFactory(InvertedIndex index)
	{
		this.query = new TreeMap<>();
		this.index = index;
	}
	
	@Override
	public void parseQuery(String line, boolean flag) 
	{
		TreeSet<String> lines = TextFileStemmer.uniqueStems(line);
		String word = String.join(" ", lines);
		if (!query.containsKey(word) && !word.isBlank())
		{
			query.put(word, index.search(lines, flag));
		}
	}
	
	@Override
	public void queryJson(Path path) throws IOException
	{
		SimpleJsonWriter.queryJson(query, path);
	}
}