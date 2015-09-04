package file.nsconfig;

import java.io.File;
import java.util.HashMap;

import process.logging.Logger;
import system.Configuration;
import file.LineParser;
import file.StringParser;
import file.nsconfig.parse.*;

public class ConfigFileLoader {
	
	/*
	 * A file loader for NS Config files (extension .nsconfig)
	 */ 

	/* *********************************************************************************************
	 * Static Parsers
	 * *********************************************************************************************/
	protected static HashMap<String, StringParser<Configuration>> parsers;
	protected static StringParser<Configuration> defaultParser;
	protected static StringParser<Configuration> commentParser;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		parsers = new HashMap<String, StringParser<Configuration>>();

		(new IdParser()).addTo(parsers);
		(new NodeIdPrefixParser()).addTo(parsers);
		(new FrameFileNamePrefixParser()).addTo(parsers);
		(new AnimationFolderNamePrefixParser()).addTo(parsers);
		
		(new ScreenWidthParser()).addTo(parsers);
		(new ScreenHeightParser()).addTo(parsers);
		
		(new IsClockParser()).addTo(parsers);
		(new IsControllerParser()).addTo(parsers);
		(new IsLeafParser()).addTo(parsers);
		(new IsDrawingToScreenParser()).addTo(parsers);
		(new IsRealTimeParser()).addTo(parsers);

		(new SceneParser()).addTo(parsers);

		(new CanWriteToDiskParser()).addTo(parsers);
		(new WorkingDirectoryParser()).addTo(parsers);
		(new ModelsSubDirectoryParser()).addTo(parsers);
		(new ScreenshotSubDirectoryParser()).addTo(parsers);
		(new AnimationSubDirectoryParser()).addTo(parsers);
		(new TextureSubDirectoryParser()).addTo(parsers);

		(new ControllerHostNameParser()).addTo(parsers);
		//(new WebInterfacePortParser()).addTo(parsers);
		//(new MessageReceivePortParser()).addTo(parsers);
		(new MessageSendPortParser()).addTo(parsers);
		(new MessageThreadCountParser()).addTo(parsers);
		
		//TODO: The rest
		
		
		defaultParser = new EnvironmentVariableParser();
		commentParser = new CommentParser();
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
		Logger.message(-32, "ConfigFileLoader: Starting loading of the config [" + file.getName() + "]...");
		
		//Iterate the lines in the given file
		String lineKey;
		StringParser<Configuration> parser;
		int firstEqualsSignIndex = -1;
		
		LineParser lines = new LineParser(file);
		
		for(String line : lines)
		{
			//Parse comments
			if(line.startsWith("#")) {
				commentParser.parse(line, null);
				continue;
			}
			
			//Skip empty ...
			if(line.trim().length() == 0) {
				continue;
			}

			firstEqualsSignIndex = line.indexOf("=");
			
			//...or malformed lines
			if(firstEqualsSignIndex < 0) {
				Logger.warning(-32, "ConfigFileLoader: Skipping line [" + line + "].");
				continue;
			}
			
			//Get the line key
			lineKey = line.substring(0, firstEqualsSignIndex).trim();
			
			//Get the associated parser
			parser = parsers.get(lineKey);
			
			//If we dont have a parser for this line, process as envvar and continue;
			if(parser == null) {
				Logger.warning(-32, "ConfigFileLoader.load(): Encountered a line key [" + lineKey + "] that does not have" +
						" an associated parser.  Setting as an environment variable.");
				defaultParser.parse(line, null);
				continue;
			}
			
			//Parse the line into the global configuration
			parser.parse(line, null);
		}
		
		Logger.message(-32, "ConfigFileLoader: Ending loading of the configuration [" + file.getName() + "]... (" + 
				(System.currentTimeMillis() - startTime) + "ms).");
	}

	
	/* *********************************************************************************************
	 * Private Constructor
	 * *********************************************************************************************/
	private ConfigFileLoader() { /*No Access*/ }
}