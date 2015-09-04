package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class MessageThreadCountParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MessageThreadCountParser() { keyToken = "messageThreadCount"; }
	

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
				throw new Exception("MessageThreadCountParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			int threadCount = Integer.parseInt(tokens[1]);
			
			//Make sure the Message Thread Count is reasonable
			if (threadCount <= 0)
				throw new Exception("MessageThreadCountParser: Message Thread Count was invalid [" + threadCount + "].");
			
			//Set it
			Configuration.Networking.setMessageThreadCount(threadCount);
			Logger.message(-32, "ConfigFileLoader: Setting Message Thread Count [" + threadCount + "].");
			
		}catch(Exception e) {
			Logger.error(-52, "MessageThreadCountParser: Failed to parse a Message Thread Count line. [" + str + "]");
			Logger.error(-52, StringUtils.stackTraceToString(e));
		}
	}
}