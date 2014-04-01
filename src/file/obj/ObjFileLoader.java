package file.obj;

import java.io.File;
import java.util.HashMap;

import file.StringParser;

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
	protected HashMap<String, StringParser<ObjModelData>> parsers;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		//
	}
	

	/* *********************************************************************************************
	 * Static Loading Methods
	 * *********************************************************************************************/
	public static ObjModelData load(String fileName) { return load(new File(fileName)); }
	public static ObjModelData load(File file)
	{
		//
		return null;
	}
}
