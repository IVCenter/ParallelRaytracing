package file.ppm;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;
import raytrace.map.texture._2D.Texture2D;
import file.LineParser;
import file.StringParser;
import file.xyz.XyzPointCloudData;
import file.xyz.parse.XyzEntryParser;

public class PpmFileLoader {

	/*
	 * A file loader for PPM Files
	 */

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static Texture2D load(String fileName) { return load(new File(fileName)); }
	public static Texture2D load(File file)
	{
		/*
		//Make sure the file exists
		if(!file.exists()) {
			Logger.warning(-73, "PpmFileLoader: The specified file does not exist [" + file.getPath() + "].");
			return null;
		}

		long startTime = System.currentTimeMillis();
		Logger.message(-73, "PpmFileLoader: Starting loading of the PPM File [" + file.getName() + "]...");
		
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
				Logger.warning(-73, "PpmFileLoader: Skipping line [" + line + "].");
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
		
		Logger.message(-73, "PpmFileLoader: Ending loading of the point cloud [" + file.getName() + "] with [" + 
				data.getPoints().size() + "] points... (" + (System.currentTimeMillis() - startTime) + "ms).");
		
		return data;
		*/
		
		return null;
	}
}