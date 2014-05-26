package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IsRealTimeParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsRealTimeParser() { keyToken = "isRealTime"; }
	

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
				throw new Exception("IsRealTimeParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean isRealTime = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setRealTime(isRealTime);
			Logger.progress(-32, "ConfigFileLoader: Setting Realtime Flag [" + isRealTime + "].");
			
		}catch(Exception e) {
			Logger.error(-42, "IsRealTimeParser: Failed to parse a Realtime Flag line. [" + str + "]");
			Logger.error(-42, StringUtils.stackTraceToString(e));
		}
	}
}