package file.sslevel;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;
import file.LineParser;
import file.StringParser;
import file.sslevel.parse.SslEntryParser;

public class SslFileLoader {

	/* *********************************************************************************************
	 * Static Parsers
	 * *********************************************************************************************/
	protected static HashMap<String, StringParser<SslLevelData>> parsers;
	protected static StringParser<SslLevelData> defaultParser;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		parsers = new HashMap<String, StringParser<SslLevelData>>();

		defaultParser = new SslEntryParser();
		(defaultParser).addTo(parsers);
	}
	

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static SslLevelData load(String fileName) { return load(new File(fileName)); }
	public static SslLevelData load(File file)
	{
		//Make sure the file exists
		if(!file.exists()) {
			Logger.warning(-70, "SslFileLoader: The specified file does not exist [" + file.getPath() + "].");
			return null;
		}

		long startTime = System.currentTimeMillis();
		Logger.message(-70, "SslFileLoader: Starting loading of the point cload [" + file.getName() + "]...");
		
		//Create a new Ssl Level Data object
		SslLevelData data = new SslLevelData();
		
		//Iterate the lines in the given file
		String lineKey = null;
		StringParser<SslLevelData> parser;
		
		LineParser lines = new LineParser(file);
		
		for(String line : lines)
		{
			//Replace tabs with spaces
			line = line.trim().replace("\t", " ");
			
			if(line.length() == 0) {
				Logger.warning(-70, "SslFileLoader: Skipping line [" + line + "].");
				continue;
			}
			
			//Get the associated parser
			parser = parsers.get(lineKey);
			
			//If we dont have a parser for this line, continue;
			if(parser == null) {
				parser = defaultParser;
			}
			
			//Parse the line into the level data
			parser.parse(line, data);
		}
		
		Logger.message(-70, "SslFileLoader: Ending loading of the space slalom level [" + file.getName() + "] with [" + 
				data.getGates().size() + "] gates... (" + (System.currentTimeMillis() - startTime) + "ms).");
		
		return data;
	}
}
