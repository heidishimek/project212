import java.io.*;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Data structure class for the Inverted Index
 * Class contains add method and helper methods.
 * 
 * @author heidishimek
 *
 */
public class InvertedIndex 
{
	/**
	 * Initialize Index using TreeMap
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	
	/**
	 * Stores word count for locations
	 */
	private final TreeMap<String, Integer> count;

	/**
	 * Constructor
	 */
	public InvertedIndex()
	{
		this.index = new TreeMap<>();
		this.count = new TreeMap<String, Integer>();
	}
	
	/**
	 * Add method to add word to the index 
	 * if the word is not already there
	 * 
	 * @param word			adds word from file to index
	 * @param inputFile		file being used
	 * @param counter			line word was on
	 * @throws IOException	IOException
	 */
	public void add(String word, String inputFile, Integer counter) throws IOException
	{ 		
 		index.putIfAbsent(word, new TreeMap<String, TreeSet<Integer>>());
 		index.get(word).putIfAbsent(inputFile, new TreeSet<Integer>());
 		
 		boolean changed = index.get(word).get(inputFile).add(counter);
 		
 		if (changed) 
 		{
 			count.put(inputFile, count.getOrDefault(inputFile, 0) + 1);
 		}
	}

	/**
	 * Helper method to convert index to pretty JSON
	 * 
	 * @param path 			takes in path to write to json
	 * @throws IOException 	throws IOException
	 */
	public void indexToJson(Path path) throws IOException 
	{
		SimpleJsonWriter.asNestedObject(index, path);
	}
		
	/**
	 * Determines whether the word is stored in the index
	 * 
	 * @param	 	word word being passed in
	 * @return		boolean
	 */
	public boolean contains(String word)
	{
		return index.containsKey(word);
	}
	
	/**
	 * Determines whether the location is stored in the index and the word is
	 * stored for that location.
	 * 
	 * @param location	location passed in
	 * @param word		word passed in
	 * @return			boolean
	 */
	public boolean contains(String word, String location)
	{
		if (contains(word))
		{
			return index.get(word).containsKey(location);
		}
		return false;
	}
	
	/**
	 * Determines whether the location is stored in the index and the word and position
	 * is stored for that location.
	 * 
	 * @param word		word being passed in 
	 * @param location	location passed in
	 * @param position	position of index
	 * @return			boolean
	 */	
	public boolean contains(String word, String location, int position)
	{
		if (contains(word, location))
		{
			return index.get(word).get(location).contains(position);
		}
		return false;
	}
	
	/**
	 * Returns an unmodifiable view of locations
	 * 
	 * @param word	word being passed in
	 * @return		unmodifiable set
	 */
	public Set<String> getLocations(String word)
	{
		if (contains(word))
		{
			return Collections.unmodifiableSet(index.get(word).keySet());
		}
		return Collections.emptySet();
	}
	
	/**
	 * Helper method to return words of index
	 * 
	 * @return 	unmodifiable set
	 */
	public Set<String> getWords()
	{
		return Collections.unmodifiableSet(index.keySet());
	}
	
	/**
	 * method that safely returns the positions.
	 * 
	 * @param word  word passed in 
	 * @param location location passed in
	 * 
	 * @return positions of word and location
	 */
	public Set<Integer> getPositions(String word, String location)
	{
		if (contains(word, location))
		{
			return Collections.unmodifiableSet(index.get(word).get(location));
		}
		return Collections.emptySet();
	}
	
	/**
	 * Size method for words
	 * 
	 * @return size of words
	 */
	public int wordsSize()
	{
		return getWords().size();
	}
	
	/**
	 * Size method for locations
	 * 
	 * @param word 	word passed in
	 * @return 		size of locations
	 */
	public int locationSize(String word)
	{
		return getLocations(word).size();
	}
	
	/**
	 * Size method for positions
	 * 
	 * @param word 		word passed in
	 * @param location	location passed in
	 * @return			size of positions
	 */
	public int positionsSize(String word, String location)
	{
		return getPositions(word, location).size();
	}
	
	@Override
	public String toString()
	{
		return index.toString();
	}

	/**
	 * Convenience method such that given a
	 * list of words and the location/path they came from, it adds each to the
	 * inverted index using the list index as the position.
	 * 
	 * @param words list of words
	 * @param input input file
	 * @throws IOException IOException
	 */
	public void addAll(List<String> words, String input) throws IOException
	{
		int position = 0; 
		for (String word : words)
		{
			add(word, input, position);
			position++;
		}
	}
	
	/**
	 * exact search method for queries
	 * 
	 * @param queries query passed in
	 * @return collection of search results
	 */
	public Collection<QueryResult> exactSearch(Collection<String> queries)
	{
		ArrayList<QueryResult> collectionOutput = new ArrayList<>();
		HashMap<String, QueryResult> finalOutput = new HashMap<>();

		for (String query : queries)
		{
			if (index.containsKey(query))
			{
				helper(finalOutput, query, collectionOutput);
			}
		}
		Collections.sort(collectionOutput);
		return collectionOutput;
	}
	
	/**
	 * exact search method for queries
	 * 
	 * @param queries query passsed in 
	 * @return collection of search results
	 */
	public Collection<QueryResult> partialSearch(Collection<String> queries) 
	{
		ArrayList<QueryResult> collectionOutput = new ArrayList<>(); 
		HashMap<String, QueryResult> finalOutput = new HashMap<>();

		for (String query : queries)
		{
			for (String word : index.tailMap(query).keySet()) 
			{
				if (word.startsWith(query))
				{
					helper(finalOutput, word, collectionOutput);
				}
				else
				{
					break;
				}
			}
		}
		Collections.sort(collectionOutput);
		return collectionOutput;
	}
	
	/**
	 * Helper method for searches
	 * 
	 * @param finalOutput 		final output 
	 * @param word				word passed in 
	 * @param collectionOutput	collection output
	 */
	private void helper(HashMap<String, QueryResult> finalOutput, String word, ArrayList<QueryResult> collectionOutput)
	{
		
		for (String location : index.get(word).keySet())
		{
			if (!finalOutput.containsKey(location))
			{
				QueryResult queryOutput = new QueryResult(location); 
				finalOutput.put(location, queryOutput);
				collectionOutput.add(queryOutput);
			}
			finalOutput.get(location).update(word);
		}
	}

	/**
	 * helper method to determine
	 * exact or partial search
	 * 
	 * @param query query passed in
	 * @param flag flag to determine search
	 * @return search collection
	 */
	public Collection<QueryResult> search(Collection<String> query, boolean flag)
	{
		if (flag)
		{
			return exactSearch(query);
		}
		return partialSearch(query);
	}
	
	/**
	 * Helper method to convert index to pretty JSON
	 * 
	 * @param path takes in path to write to json
	 * @throws IOException throws IOException
	 */
	public void countJson(Path path) throws IOException
	{
		SimpleJsonWriter.asObject(count, path);
	}
	
	/**
	 * Helper Method for ThreadSafeIndexFactory
	 * 
	 * @param local index 
	 */
	public void addAll(InvertedIndex local) 
	{
		for (String word : local.index.keySet())
		{
			if (!index.containsKey(word))
			{
				index.put(word, local.index.get(word));
			}
			else {
				for (String file : local.index.get(word).keySet())
				{
					if (!index.get(word).containsKey(file))
					{
						index.get(word).put(file, local.index.get(word).get(file));
					}
					else {
						index.get(word).get(file).addAll(local.index.get(word).get(file));
					}
				}
			}
		}
		
		for (String counter : local.count.keySet())
		{
			if (!index.containsKey(counter))
			{
				count.put(counter, local.count.get(counter));
			}
			else 
			{
				count.put(counter, this.count.get(counter) + local.count.get(counter));
			}
		}
	}
	
	/**
	 * Query class
	 * 
	 * @author heidishimek
	 *
	 */
	public class QueryResult implements Comparable<QueryResult>
	{
		/**
		 * Location
		 */
		private final String location;
		
		/**
		 * Score
		 */
		private double score;
		
		/**
		 * Matches
		 */
		private int matches;
		
		/**
		 * Decimal Formatter
		 */
		private final DecimalFormat FORMATTER = new DecimalFormat("0.00000000");

		/**
		 * Override method compares to score
		 * 
		 * @param queryResult comparing to
		 * @return results
		 */
		@Override
		public int compareTo(QueryResult queryResult) 
		{
			
			if (this.score == queryResult.score && queryResult.matches == this.matches)
			{
				return this.location.compareToIgnoreCase(queryResult.location);
			}
			else if (this.score == queryResult.score)
			{
				return Integer.compare(queryResult.matches, this.matches);
			}

			else
			{
				return Double.compare(queryResult.score, this.score);
			}
		}
		
		/**
		 * Query Constructor
		 * @param location location passed in 
		 */
		public QueryResult(String location) 
		{
			this.location = location;
			this.score = 0;
			this.matches = 0;
		}
		
		/**
		 * Helper method to update
		 * 
		 * @param word word passed in
		 */
		private void update(String word) 
		{
			this.matches += index.get(word).get(location).size();
			setScore();
		}
		
		/**
		 * Helper method to get the location
		 * 
		 * @return location string
		 */
		public String getLocation() 
		{
			return this.location;
		}

		/**
		 * Helper method to get the word score
		 * 
		 * @return score 
		 */
		public double getScore() 
		{
			return this.score;
		}

		/**
		 * Helper method to set score
		 */
		private void setScore()
		{
			this.score = (double) this.matches / count.get(location);
		}

		/**
		 * Helper method to get the score in pretty format
		 * 
		 * @return decimal formatted string
		 */
		public String getScoreString() 
		{
			return FORMATTER.format(this.score);
		}

		/**
		 * Helper method to get the matches count
		 * 
		 * @return matches count
		 */
		public Integer getMatchesCount() 
		{
			return matches;
		}
	}
}