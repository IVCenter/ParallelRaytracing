package file.obj;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;

import file.LineParser;
import file.StringParser;
import file.obj.parse.CommentParser;
import file.obj.parse.FaceParser;
import file.obj.parse.NormalParser;
import file.obj.parse.ObjectParser;
import file.obj.parse.TexCoordParser;
import file.obj.parse.VertexParser;

public class ObjFileLoader {
	
	/*
	 * load a set of keyed parsers
	 * load a storage object (ObjModel object?)
	 * 
	 * Open a line parser
	 * tokenize each line
	 * use the first token to request parser
	 * parse line into storage object
	 */

	/* *********************************************************************************************
	 * Static Parsers
	 * *********************************************************************************************/
	protected static HashMap<String, StringParser<ObjModelData>> parsers;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		parsers = new HashMap<String, StringParser<ObjModelData>>();

		(new ObjectParser()).addTo(parsers);
		(new VertexParser()).addTo(parsers);
		(new NormalParser()).addTo(parsers);
		(new TexCoordParser()).addTo(parsers);
		(new FaceParser()).addTo(parsers);
		(new CommentParser()).addTo(parsers);
		//TODO: The rest
	}
	

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static ObjModelData load(String fileName) { return load(new File(fileName)); }
	public static ObjModelData load(File file)
	{
		//Make sure the file exists
		if(!file.exists()) {
			Logger.warning(-9, "ObjFileLoader: The specified file does not exist [" + file.getPath() + "].");
			return null;
		}

		long startTime = System.currentTimeMillis();
		Logger.progress(-9, "ObjFileLoader: Starting loading of the model [" + file.getName() + "]...");
		
		//Create a new Object Model Data object
		ObjModelData data = new ObjModelData();
		
		//Iterate the lines in the given file
		String lineKey;
		StringParser<ObjModelData> parser;
		int firstSpaceIndex = -1;
		
		LineParser lines = new LineParser(file);
		
		for(String line : lines)
		{
			line = line.replace("\t", " ");
			line = line.replace("  ", " ");
			firstSpaceIndex = line.indexOf(" ");
			
			if(line.length() == 0 || firstSpaceIndex < 0) {
				Logger.warning(-9, "ObjFileLoader: Skipping line [" + line + "].");
				continue;
			}
			
			//Get the line key
			lineKey = line.substring(0, firstSpaceIndex).trim();
			
			//Get the associated parser
			parser = parsers.get(lineKey);
			
			//If we dont have a parser for this line, continue;
			if(parser == null) {
				Logger.warning(-9, "ObjFileLoader.load(): Encountered a line key [" + lineKey + "] that does not have" +
						" an associated parser.");
				continue;
			}
			
			//Parse the line into the model data
			parser.parse(line, data);
		}
		
		Logger.progress(-9, "ObjFileLoader: Ending loading of the model [" + file.getName() + "]... (" + 
				(System.currentTimeMillis() - startTime) + "ms).");
		
		return data;
	}
}
