package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class ScreenshotSubDirectoryParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ScreenshotSubDirectoryParser() { keyToken = "screenshotSubDirectory"; }
	

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
				throw new Exception("ScreenshotSubDirectoryParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String screenshotDir = tokens[1];
			
			//Make sure the Screenshot Directory is reasonable
			if (screenshotDir == null || screenshotDir.isEmpty())
				throw new Exception("ScreenshotSubDirectoryParser: Screenshot Directory was invalid [" + screenshotDir + "].");
			
			//Set it
			Configuration.setScreenshotSubDirectory(screenshotDir);
			Logger.message(-32, "ConfigFileLoader: Setting Screenshot Directory [" + screenshotDir + "].");
			
		}catch(Exception e) {
			Logger.error(-47, "ScreenshotSubDirectoryParser: Failed to parse a Screenshot Directory line. [" + str + "]");
			Logger.error(-47, StringUtils.stackTraceToString(e));
		}
	}
}