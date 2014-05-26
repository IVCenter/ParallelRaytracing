package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IsDrawingToScreenParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsDrawingToScreenParser() { keyToken = "isDrawingToScreen"; }
	

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
				throw new Exception("IsDrawingToScreenParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean isDrawingToScreen = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setDrawToScreen(isDrawingToScreen);
			Logger.progress(-32, "ConfigFileLoader: Setting Drawing to Screen Flag [" + isDrawingToScreen + "].");
			
		}catch(Exception e) {
			Logger.error(-41, "IsDrawingToScreenParser: Failed to parse a Drawing to Screen Flag line. [" + str + "]");
			Logger.error(-41, StringUtils.stackTraceToString(e));
		}
	}
}