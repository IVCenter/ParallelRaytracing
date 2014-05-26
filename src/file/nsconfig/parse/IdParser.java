package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class IdParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IdParser() { keyToken = "id"; }
	

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
				throw new Exception("IdParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String id = tokens[1];
			
			//Make sure the Node ID is reasonable
			if (id == null || id.isEmpty())
				throw new Exception("IdParser: Node ID was invalid [" + id + "].");
			
			//Set it
			Configuration.setId(id);
			Logger.progress(-32, "ConfigFileLoader: Setting Node ID [" + id + "].");
			
		}catch(Exception e) {
			Logger.error(-36, "IdParser: Failed to parse a Node ID line. [" + str + "]");
			Logger.error(-36, StringUtils.stackTraceToString(e));
		}
	}
}