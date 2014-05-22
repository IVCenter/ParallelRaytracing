package file.nsconfig;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;
import system.Configuration;
import file.LineParser;
import file.StringParser;
import file.nsconfig.parse.ScreenWidthParser;

public class ConfigFileLoader {
	
	/*
	 * A file loader for NS Config files (extension .nsconfig)
	 */ 

	/* *********************************************************************************************
	 * Static Parsers
	 * *********************************************************************************************/
	protected static HashMap<String, StringParser<Configuration>> parsers;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		parsers = new HashMap<String, StringParser<Configuration>>();

		(new ScreenWidthParser()).addTo(parsers);
		//TODO: The rest
	}
	

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static void load(String fileName) { load(new File(fileName)); }
	public static void load(File file)
	{
		//Make sure the file exists
		if(!file.exists()) {
			Logger.warning(-32, "ConfigFileLoader: The specified file does not exist [" + file.getPath() + "].");
			return;
		}

		long startTime = System.currentTimeMillis();
		Logger.progress(-32, "ConfigFileLoader: Starting loading of the config [" + file.getName() + "]...");
		
		//Iterate the lines in the given file
		String lineKey;
		StringParser<Configuration> parser;
		int firstSpaceIndex = -1;
		
		LineParser lines = new LineParser(file);
		
		for(String line : lines)
		{
			line = line.replace("\t", " ");
			firstSpaceIndex = line.indexOf(" ");
			
			//Skip empty or malformed lines
			if(line.length() == 0 || firstSpaceIndex < 0) {
				Logger.warning(-32, "ConfigFileLoader: Skipping line [" + line + "].");
				continue;
			}
			
			//Get the line key
			lineKey = line.substring(0, firstSpaceIndex).trim();
			
			//Get the associated parser
			parser = parsers.get(lineKey);
			
			//If we dont have a parser for this line, continue;
			if(parser == null) {
				Logger.warning(-32, "ConfigFileLoader.load(): Encountered a line key [" + lineKey + "] that does not have" +
						" an associated parser.");
				continue;
			}
			
			//Parse the line into the global configuration
			parser.parse(line, null);
		}
		
		Logger.progress(-32, "ConfigFileLoader: Ending loading of the configuration [" + file.getName() + "]... (" + 
				(System.currentTimeMillis() - startTime) + "ms).");
	}
}