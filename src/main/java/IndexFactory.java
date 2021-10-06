import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Factory class to check if file/directory contains valid text files.
 * Parses files.
 * Stems, and adds words to the Inverted Index data structure.
 * 
 * @author heidishimek
 *
 */
public class IndexFactory {
	
	/**
	 *  Store Index
	 */
	private final InvertedIndex invertedIndex;
	
	/**
	 * Initialize Index
	 * 
	 * @param invertedIndex IndexFactory constructor 
	 */
	public IndexFactory(InvertedIndex invertedIndex) 
	{
		this.invertedIndex = invertedIndex;
	}
	
	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = TextFileStemmer.DEFAULT;
 
	/**
	 * parses files and stems, and adds words to the inverted index
	 * 
	 * @param inputFile		uses Buffered Reader to read the 
	 * 						words and stems them (calls TextFileStemmer)
	 * 						calls add method to add word, file, and the count
	 * @param index 		adds to invertedIndex
	 * 						
	 * @throws IOException	IOException
	 */
	public static void parseText(Path inputFile, InvertedIndex index) throws IOException
	{
		String file = inputFile.toString();
		String line; 
		int count = 1;
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		
		try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8))
		{			
			while ((line = reader.readLine()) != null)
			{
				for (String word : TextParser.parse(line))
				{
					index.add(stemmer.stem(word).toString(), file, count);
					count++;
				}
			}
		}
	}
	
	/**
	 * calls static parse text with just an inputFile
	 * 
	 * @param inputFile 	file name as a Path
	 * @throws IOException 	throws IOException
	 */
	public void parseText(Path inputFile) throws IOException
	{
		parseText(inputFile, this.invertedIndex);
	}

	/**
	 * parses a directory to find text files to parse
	 * 
	 * @param dir			parses directory if it is not a text file
	 * @throws IOException	IOException
	 */
	public void parseDirectory(Path dir) throws IOException
	{
		try (DirectoryStream<Path> i = Files.newDirectoryStream(dir))
		{
			for (Path file : i)
			{
				if (textFile(file))
				{
					parseText(file);
				}
				
				else if (Files.isDirectory(file) == true)
				{
					parseDirectory(file);
				}
			}
		}
	}

	/**
	 * checks if file is a text file
	 * 
	 * @param files		checks if file is a text file (used in parseDirectory)
	 * @return			BOOLEAN
	 */	
	public static boolean textFile(Path files)
	{
		String file = files.getFileName().toString().toLowerCase();
		return (Files.isRegularFile(files) && (file.endsWith("text") || file.endsWith(".txt")));
	}
	
	/**
	 * Helper method for driver to determine 
	 * whether to use parseText or parseDirectory
	 * 
	 * @param input Path input file
	 * @throws IOException throws IOException
	 */
	public void parseSelector(Path input) throws IOException
	{
		if (Files.isRegularFile(input))
		{
			parseText(input);
		}
		parseDirectory(input);
	}
}