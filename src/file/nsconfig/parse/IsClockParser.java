package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IsClockParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsClockParser() { keyToken = "isClock"; }
	

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
				throw new Exception("IsClockParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean isClock = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setClock(isClock);
			Logger.progress(-32, "ConfigFileLoader: Setting Clock Flag [" + isClock + "].");
			
		}catch(Exception e) {
			Logger.error(-39, "IsClockParser: Failed to parse a Clock Flag line. [" + str + "]");
			Logger.error(-39, StringUtils.stackTraceToString(e));
		}
	}
}