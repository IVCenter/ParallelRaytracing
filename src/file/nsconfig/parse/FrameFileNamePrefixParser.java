package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class FrameFileNamePrefixParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FrameFileNamePrefixParser() { keyToken = "frameFileNamePrefix"; }
	

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
				throw new Exception("FrameFileNamePrefixParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String framePrefix = tokens[1];
			
			//Make sure the Frame Prefix is reasonable
			if (framePrefix == null || framePrefix.isEmpty())
				throw new Exception("FrameFileNamePrefixParser: Frame Prefix was invalid [" + framePrefix + "].");
			
			//Set it
			Configuration.setFrameFileNamePrefix(framePrefix);
			Logger.progress(-32, "ConfigFileLoader: Setting Frame Prefix [" + framePrefix + "].");
			
		}catch(Exception e) {
			Logger.error(-38, "FrameFileNamePrefixParser: Failed to parse a Frame Prefix line. [" + str + "]");
			Logger.error(-38, StringUtils.stackTraceToString(e));
		}
	}
}