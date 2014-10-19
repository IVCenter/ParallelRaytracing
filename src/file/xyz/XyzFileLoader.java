package file.xyz;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;
import file.LineParser;
import file.StringParser;
import file.xyz.parse.XyzEntryParser;

public class XyzFileLoader{

	/* *********************************************************************************************
	 * Static Parsers
	 * *********************************************************************************************/
	protected static HashMap<String, StringParser<XyzPointCloudData>> parsers;
	protected static StringParser<XyzPointCloudData> defaultParser;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		parsers = new HashMap<String, StringParser<XyzPointCloudData>>();

		defaultParser = new XyzEntryParser();
		(defaultParser).addTo(parsers);
		//TODO: The rest?
	}
	

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static XyzPointCloudData load(String fileName) { return load(new File(fileName)); }
	public static XyzPointCloudData load(File file)
	{
		//Make sure the file exists
		if(!file.exists()) {
			Logger.warning(-70, "XyzFileLoader: The specified file does not exist [" + file.getPath() + "].");
			return null;
		}

		long startTime = System.currentTimeMillis();
		Logger.progress(-70, "XyzFileLoader: Starting loading of the point cload [" + file.getName() + "]...");
		
		//Create a new Xyz Point Cload Data object
		XyzPointCloudData data = new XyzPointCloudData();
		
		//Iterate the lines in the given file
		String lineKey;
		StringParser<XyzPointCloudData> parser;
		int firstSpaceIndex = -1;
		
		LineParser lines = new LineParser(file);
		
		for(String line : lines)
		{
			line = line.replace("\t", " ");
			firstSpaceIndex = line.indexOf(" ");
			
			if(line.length() == 0 || firstSpaceIndex < 0) {
				Logger.warning(-70, "XyzFileLoader: Skipping line [" + line + "].");
				continue;
			}
			
			//Get the line key
			lineKey = line.substring(0, firstSpaceIndex).trim();
			
			//Get the associated parser
			parser = parsers.get(lineKey);
			
			//If we dont have a parser for this line, continue;
			if(parser == null) {
				//Logger.warning(-70, "XyzFileLoader.load(): Encountered a line key [" + lineKey + "] that does not have" +
				//		" an associated parser.");
				//continue;
				parser = defaultParser;
			}
			
			//Parse the line into the point cloud data
			parser.parse(line, data);
		}
		
		Logger.progress(-70, "XyzFileLoader: Ending loading of the point cloud [" + file.getName() + "] with [" + 
				data.getPoints().size() + "] points... (" + (System.currentTimeMillis() - startTime) + "ms).");
		
		return data;
	}
}
