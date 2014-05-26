package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class NodeIdPrefixParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NodeIdPrefixParser() { keyToken = "nodeIdPrefix"; }
	

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
				throw new Exception("NodeIdPrefixParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String idPrefix = tokens[1];
			
			//Make sure the Node ID Prefix is reasonable
			if (idPrefix == null || idPrefix.isEmpty())
				throw new Exception("NodeIdPrefixParser: Node ID Prefix was invalid [" + idPrefix + "].");
			
			//Set it
			Configuration.setNodeIdPrefix(idPrefix);
			Logger.progress(-32, "ConfigFileLoader: Setting Node ID Prefix [" + idPrefix + "].");
			
		}catch(Exception e) {
			Logger.error(-37, "NodeIdPrefixParser: Failed to parse a Node ID Prefix line. [" + str + "]");
			Logger.error(-37, StringUtils.stackTraceToString(e));
		}
	}
}