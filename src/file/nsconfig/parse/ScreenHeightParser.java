package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class ScreenHeightParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ScreenHeightParser() { keyToken = "screenHeight"; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, Configuration pop)
	{
		//Get the tokens
		String[] tokens = splitAtFirst(str.trim(), "=");
		
		//Parse tokens
		try{
			
			//First make sure the first token matches the key token
			if(!tokens[0].equals(keyToken))
				throw new Exception("ScreenHeightParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			int screenHeight = Integer.parseInt(tokens[1]);
			
			//Make sure the screen height is reasonable
			if (screenHeight <= 0)
				throw new Exception("ScreenHeightParser: Screen height was invalid [" + screenHeight + "].");
			
			//Set it
			Configuration.setScreenHeight(screenHeight);
			Logger.progress(-32, "ConfigFileLoader: Setting screen height [" + screenHeight + "].");
			
		}catch(Exception e) {
			Logger.error(-34, "ScreenHeightParser: Failed to parse a screen height line. [" + str + "]");
			Logger.error(-34, StringUtils.stackTraceToString(e));
		}
	}
}