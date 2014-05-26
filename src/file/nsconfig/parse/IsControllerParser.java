package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IsControllerParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsControllerParser() { keyToken = "isController"; }
	

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
				throw new Exception("IsControllerParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean isController = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setController(isController);
			Logger.progress(-32, "ConfigFileLoader: Setting Controller Flag [" + isController + "].");
			
		}catch(Exception e) {
			Logger.error(-39, "IsControllerParser: Failed to parse a Controller Flag line. [" + str + "]");
			Logger.error(-39, StringUtils.stackTraceToString(e));
		}
	}
}