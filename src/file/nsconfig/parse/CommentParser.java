package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class CommentParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CommentParser() { keyToken = "#"; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, Configuration pop)
	{	
		try{
			
			//First make sure the first token matches the key token
			if(!str.startsWith(keyToken))
				throw new Exception("CommentParser: Excepted a key token of [" + keyToken + "] but encountered [" + str + "]");
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse a comment line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}