package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class EnvironmentVariableParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public EnvironmentVariableParser() { keyToken = ""; }
	

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
			
			String key = tokens[0];
			String value = tokens[1];
			
			//Set it
			Configuration.set(key, value);
			Logger.progress(-32, "ConfigFileLoader: Setting an environment variable Key[" + key + "] -> Value[" + value + "].");
			
		}catch(Exception e) {
			Logger.error(-35, "EnvironmentVariableParser: Failed to parse an environment variable line. [" + str + "]");
			Logger.error(-35, StringUtils.stackTraceToString(e));
		}
	}
}