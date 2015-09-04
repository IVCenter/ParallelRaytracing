package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class AnimationFolderNamePrefixParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public AnimationFolderNamePrefixParser() { keyToken = "animationFolderNamePrefix"; }
	

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
				throw new Exception("AnimationFolderNamePrefixParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String animationFolderPrefix = tokens[1];
			
			//Make sure the Animation Folder Prefix is reasonable
			if (animationFolderPrefix == null || animationFolderPrefix.isEmpty())
				throw new Exception("AnimationFolderNamePrefixParser: Animation Folder Prefix was invalid [" + animationFolderPrefix + "].");
			
			//Set it
			Configuration.setAnimationFolderNamePrefix(animationFolderPrefix);
			Logger.message(-32, "ConfigFileLoader: Setting Animation Folder Prefix [" + animationFolderPrefix + "].");
			
		}catch(Exception e) {
			Logger.error(-39, "AnimationFolderNamePrefixParser: Failed to parse a Animation Folder Prefix line. [" + str + "]");
			Logger.error(-39, StringUtils.stackTraceToString(e));
		}
	}
}