package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class CanWriteToDiskParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CanWriteToDiskParser() { keyToken = "canWriteToDisk"; }
	

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
				throw new Exception("CanWriteToDiskParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean canWrite = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setCanWriteToDisk(canWrite);
			Logger.message(-32, "ConfigFileLoader: Setting Can Write to Disk Flag [" + canWrite + "].");
			
		}catch(Exception e) {
			Logger.error(-44, "CanWriteToDiskParser: Failed to parse a Can Write to Disk Flag line. [" + str + "]");
			Logger.error(-44, StringUtils.stackTraceToString(e));
		}
	}
}