import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility class for parsing and stemming text and text files into collections
 * of stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2021
 *
 * @see TextParser
 */
public class TextFileStemmer 
{
	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;
	
	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a list of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> listStems(String line, Stemmer stemmer) 
	{
		ArrayList<String> cleaned = new ArrayList<>();
		stemLine(line, stemmer, cleaned);
		return cleaned;
	}
	
	/**
	 * Stems individual line to improve efficiency 
	 * 
	 * @param line 		line to stem
	 * @param stemmer	stemmer
	 * @param cleaned	cleaned words
	 */
	public static void stemLine(String line, Stemmer stemmer, Collection<String> cleaned)
	{
		String[] words = TextParser.parse(line);
		
		for (String word : words)
		{
			cleaned.add(stemmer.stem(word).toString());
		}
	}
	
	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a list of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #listStems(String, Stemmer)
	 */
	public static ArrayList<String> listStems(String line) 
	{
		return listStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #uniqueStems(String)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> listStems(Path inputFile) throws IOException 
	{
		try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8))
		{
			String line = null; 
			ArrayList<String> stems = new ArrayList<>();
			Stemmer stemmer = null;
				
			while ((line = reader.readLine()) != null)
			{
				stemLine(line, stemmer, stems);
			}
			return stems;
		}
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed
	 * from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #uniqueStems(String, Stemmer)
	 */
	public static TreeSet<String> uniqueStems(String line) 
	{
		return uniqueStems(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed
	 * from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(String line, Stemmer stemmer) 
	{
		TreeSet<String> sorted = new TreeSet<>();
		stemLine(line, stemmer, sorted);
		return sorted;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #uniqueStems(String)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(Path inputFile) throws IOException 
	{
		try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8))
		{
			String line = null;
			TreeSet<String> sorted = new TreeSet<>();
			Stemmer stemmer = null;
			
			while ((line = reader.readLine()) != null)
			{
				stemLine(line, stemmer, sorted);
			}
			return sorted;
		}
	}
}