package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class ScreenWidthParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ScreenWidthParser() { keyToken = "screenWidth"; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, Configuration pop)
	{
		//Get the tokens
		String[] tokens = tokens(str.trim(), " ");
		
		//Parse tokens
		try{
			
			//First make sure the first token matches the key token
			if(!tokens[0].equals(keyToken))
				throw new Exception("ScreenWidthParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			int screenWidth = Integer.parseInt(tokens[1]);
			
			//Make sure the screen width is reasonable
			if (screenWidth <= 0)
				throw new Exception("ScreenWidthParser: Screen width was invalid [" + screenWidth + "].");
			
			//Set it
			Configuration.setScreenWidth(screenWidth);
			Logger.progress(-11, "ConfigFileLoader: Setting screen width [" + screenWidth + "].");
			
		}catch(Exception e) {
			Logger.error(-1, "ScreenWidthParser: Failed to parse a screen width line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
		}
	}
}
