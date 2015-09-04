package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class MessageSendPortParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MessageSendPortParser() { keyToken = "messageSendPort"; }
	

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
				throw new Exception("MessageSendPortParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			int port = Integer.parseInt(tokens[1]);
			
			//Make sure the Message Send Port is reasonable
			if (port <= 0)
				throw new Exception("MessageSendPortParser: Message Send Port was invalid [" + port + "].");
			
			//Set it
			Configuration.Networking.setMessageSendPort(port);
			Logger.message(-32, "ConfigFileLoader: Setting Message Send Port [" + port + "].");
			
		}catch(Exception e) {
			Logger.error(-51, "MessageSendPortParser: Failed to parse a Message Send Port line. [" + str + "]");
			Logger.error(-51, StringUtils.stackTraceToString(e));
		}
	}
}