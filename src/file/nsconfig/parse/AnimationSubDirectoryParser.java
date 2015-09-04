package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class AnimationSubDirectoryParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public AnimationSubDirectoryParser() { keyToken = "animationSubDirectory"; }
	

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
				throw new Exception("AnimationSubDirectoryParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String animDir = tokens[1];
			
			//Make sure the Animation Directory is reasonable
			if (animDir == null || animDir.isEmpty())
				throw new Exception("AnimationSubDirectoryParser: Animation Directory was invalid [" + animDir + "].");
			
			//Set it
			Configuration.setAnimationSubDirectory(animDir);
			Logger.message(-32, "ConfigFileLoader: Setting Animation Directory [" + animDir + "].");
			
		}catch(Exception e) {
			Logger.error(-48, "AnimationSubDirectoryParser: Failed to parse a Animation Directory line. [" + str + "]");
			Logger.error(-48, StringUtils.stackTraceToString(e));
		}
	}
}