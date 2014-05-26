package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IsLeafParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsLeafParser() { keyToken = "isLeaf"; }
	

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
				throw new Exception("IsLeafParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			boolean isLeaf = Boolean.parseBoolean(tokens[1]);
			
			//Set it
			Configuration.setLeaf(isLeaf);
			Logger.progress(-32, "ConfigFileLoader: Setting Leaf Flag [" + isLeaf + "].");
			
		}catch(Exception e) {
			Logger.error(-40, "IsLeafParser: Failed to parse a Leaf Flag line. [" + str + "]");
			Logger.error(-40, StringUtils.stackTraceToString(e));
		}
	}
}