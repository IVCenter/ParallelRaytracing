package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class ControllerHostNameParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ControllerHostNameParser() { keyToken = "controllerHostName"; }
	

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
				throw new Exception("ControllerHostNameParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String hostname = tokens[1];
			
			//Make sure the Controller Host Name is reasonable
			if (hostname == null || hostname.isEmpty())
				throw new Exception("ControllerHostNameParser: Controller Host Name was invalid [" + hostname + "].");
			
			//Set it
			Configuration.Networking.setControllerHostName(hostname);
			Logger.progress(-32, "ConfigFileLoader: Setting Controller Host Name [" + hostname + "].");
			
		}catch(Exception e) {
			Logger.error(-50, "ControllerHostNameParser: Failed to parse a Controller Host Name line. [" + str + "]");
			Logger.error(-50, StringUtils.stackTraceToString(e));
		}
	}
}