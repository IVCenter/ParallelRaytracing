package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class TextureSubDirectoryParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public TextureSubDirectoryParser() { keyToken = "textureSubDirectory"; }
	

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
				throw new Exception("TextureSubDirectoryParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String textureDir = tokens[1];
			
			//Make sure the Texture Directory is reasonable
			if (textureDir == null || textureDir.isEmpty())
				throw new Exception("TextureSubDirectoryParser: Texture Directory was invalid [" + textureDir + "].");
			
			//Set it
			Configuration.setTextureSubDirectory(textureDir);
			Logger.progress(-32, "ConfigFileLoader: Setting Texture Directory [" + textureDir + "].");
			
		}catch(Exception e) {
			Logger.error(-49, "TextureSubDirectoryParser: Failed to parse a Texture Directory line. [" + str + "]");
			Logger.error(-49, StringUtils.stackTraceToString(e));
		}
	}
}