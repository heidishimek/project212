import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs several simple data structures in "pretty" JSON format where newlines
 * are used to separate elements and nested elements are indented using tabs.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2021
 */
public class SimpleJsonWriter 
{
	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer the writer to use
	 * @param level the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asArray(Collection<Integer> elements, Writer writer, int level) throws IOException 
	{ 	
		Iterator<Integer> i = elements.iterator();
		writer.write("[\n");
		
		if (i.hasNext())
		{
			indent(i.next().toString(), writer, level + 1);
		}
		
		while (i.hasNext())
		{
			writer.write(",\n");
			indent(i.next().toString(), writer, level + 1);
		}
		writer.write("\n");
		indent("]", writer, level);
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static void asArray(Collection<Integer> elements, Path path) throws IOException 
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) 
		{
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static String asArray(Collection<Integer> elements) 
	{
		try 
		{
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) 
		{
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer the writer to use
	 * @param level the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asObject(Map<String, Integer> elements, Writer writer, int level) throws IOException 
	{
		Iterator<String> i = elements.keySet().iterator();
		writer.write("{");
		
		if (i.hasNext())
		{
			writer.write("\n");
			String key = i.next().toString();
			Integer value = elements.get(key);
			indent(("\"" + key + "\"" + ": "+ value), writer, level + 1);
		}
		
		while (i.hasNext())
		{
			writer.write(",\n");
			String key = i.next().toString();
			Integer value = elements.get(key);
			indent(("\"" + key + "\"" + ": "+ value), writer, level + 1);
		}
		writer.write("\n");
		indent("}", writer, level);
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static String asObject(Map<String, Integer> elements) 
	{
		try 
		{
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) 
		{
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asObject(Map<String, Integer> elements, Path path) throws IOException 
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) 
		{
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON object with a nested array. The
	 * generic notation used allows this method to be used for any type of map
	 * with any type of nested collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer the writer to use
	 * @param level the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asNestedArray(Map<String, ? extends Collection<Integer>> elements, Writer writer, int level) throws IOException 
	{
		Iterator<String> i = elements.keySet().iterator();
		writer.write("{\n");
		
		if (i.hasNext())
		{
			String key = i.next().toString();
			quote(key, writer, level + 1);
			writer.write(": ");
			asArray(elements.get(key), writer, level + 1);
		}
		
		while (i.hasNext())
		{
			writer.write(",\n");
			String key = i.next().toString();
			quote(key, writer, level + 1);
			writer.write(": ");
			asArray(elements.get(key), writer, level + 1);
		}
		writer.write("\n");
		indent("}", writer, level);
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asNestedArray(Map, Writer, int)
	 */
	public static void asNestedArray(Map<String, ? extends Collection<Integer>> elements, Path path) throws IOException 
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) 
		{
			asNestedArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedArray(Map, Writer, int)
	 */
	public static String asNestedArray(Map<String, ? extends Collection<Integer>> elements) 
	{
		try 
		{
			StringWriter writer = new StringWriter();
			asNestedArray(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) 
		{
			return null;
		}
	}
	
	/**
	 * Built for InvertedIndex to return JSON
	 * 
	 * @param map			map to use 
	 * @param writer		writes JSON
	 * @param level			initial indent level
	 * @throws IOException	IOException
	 */
	public static void asNestedObject(Map<String, TreeMap<String, TreeSet<Integer>>> map, Writer writer, int level) throws IOException
	{
		Iterator<String> i = map.keySet().iterator();
		writer.write("{");
		
		if (i.hasNext())
		{
			writer.write("\n");
			String key = i.next().toString();
			quote(key, writer, level + 1);
			writer.write(": ");
			asNestedArray(map.get(key), writer, level + 1);
		}
		
		while (i.hasNext())
		{
			writer.write(",\n");
			String key = i.next().toString();
			quote(key, writer, level + 1);
			writer.write(": ");
			asNestedArray(map.get(key), writer, level + 1);
		}
		writer.write("\n");
		indent("}", writer, level);
	}

	/**
	 * Built for InvertedIndex to return JSON.
	 * 
	 * @param map			map to use 
	 * @param path			path of file
	 * @throws IOException	IOException
	 */
	public static void asNestedObject(Map<String, TreeMap<String, TreeSet<Integer>>> map, Path path) throws IOException 
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) 
		{
			asNestedObject(map, writer, 0);
		}
	}

	/**
	 * Built for InvertedIndex to return JSON.
	 * 
	 * @param map			map to use 
	 * @return				return string of JSON
	 * @throws IOException	IOException
	 */
	public static String asNestedObject(Map<String, TreeMap<String, TreeSet<Integer>>> map) throws IOException
	{
		try
		{
			StringWriter writer = new StringWriter();
			asNestedObject(map, writer, 0);
			return writer.toString();
		}
		catch (IOException e) 
		{
			return null;
		}	
	}
	
	/**
	 * helper method to write query in set format
	 * where: 
	 * count:
	 * score:
	 * 
	 * @param query query to write
	 * @param writer writer
	 * @throws IOException IO Exception
	 */
	public static void query(InvertedIndex.QueryResult query, Writer writer) throws IOException
	{
		quote("where", writer, 3);
		writer.write(": ");
		quote(query.getLocation(), writer, 0);
		writer.write(",\n");
		quote("count", writer, 3);
		writer.write(": ");
		writer.write(query.getMatchesCount().toString());
		writer.write(",\n");
		quote("score", writer, 3);
		writer.write(": ");
		writer.write(query.getScoreString());
		writer.write("\n"); 	
	}
	
	/**
	 * Writes the query as a pretty JSON object 
	 * 
	 * @param query query to write
	 * @param writer writer
	 * @throws IOException IO Exception
	 */ 
	public static void queryToJson(Collection<InvertedIndex.QueryResult> query, Writer writer) throws IOException
	{
		if (!query.isEmpty())
		{
			Iterator<InvertedIndex.QueryResult> i = query.iterator();
			
			if (i.hasNext())
			{
				indent("{\n", writer, 2);
				query(i.next(), writer);
			}
			
			while (i.hasNext())
			{
				indent("},\n", writer, 2);
				indent("{\n", writer, 2);
				query(i.next(), writer);
			}
			indent("}\n", writer, 2);
		}
	}
	
	/**
	 * Writes the query as a pretty JSON object 
	 * 
	 * @param query query to write
	 * @param path path of file
	 * @throws IOException IO Exception
	 */
	public static void queryJson(TreeMap<String, Collection<InvertedIndex.QueryResult>> query, Path path) throws IOException 
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{	
			if (!query.isEmpty())
			{
				writer.write("{\n");
				Iterator<String> i = query.keySet().iterator();
				
				if (i.hasNext())
				{
					String q = i.next();
					quote(q, writer, 1);
					writer.write(": [\n");
					queryToJson(query.get(q), writer);
				}
				
				while (i.hasNext())
				{
					String q = i.next();
					indent("],\n", writer, 1);
					quote(q, writer, 1);
					writer.write(": [\n");
					queryToJson(query.get(q), writer);
				}
				indent("]\n", writer, 1);
				writer.write("}\n");
			}
		}
	}

	/**
	 * Indents and then writes the String element.
	 *
	 * @param element the element to write
	 * @param writer the writer to use
	 * @param level the number of times to indent
	 * @throws IOException if an IO error occurs
	 */
	public static void indent(String element, Writer writer, int level) throws IOException 
	{
		writer.write("\t".repeat(level));
		writer.write(element);
	}

	/**
	 * Indents and then writes the text element surrounded by {@code " "}
	 * quotation marks.
	 *
	 * @param element the element to write
	 * @param writer the writer to use
	 * @param level the number of times to indent
	 * @throws IOException if an IO error occurs
	 */
	public static void quote(String element, Writer writer, int level) throws IOException 
	{
		writer.write("\t".repeat(level));
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}
}
