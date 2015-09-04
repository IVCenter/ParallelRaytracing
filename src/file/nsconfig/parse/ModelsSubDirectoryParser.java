package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class ModelsSubDirectoryParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ModelsSubDirectoryParser() { keyToken = "modelsSubDirectory"; }
	

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
				throw new Exception("ModelsSubDirectoryParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String modelDir = tokens[1];
			
			//Make sure the Models Directory is reasonable
			if (modelDir == null || modelDir.isEmpty())
				throw new Exception("ModelsSubDirectoryParser: Models Directory was invalid [" + modelDir + "].");
			
			//Set it
			Configuration.setModelsSubDirectory(modelDir);
			Logger.message(-32, "ConfigFileLoader: Setting Models Directory [" + modelDir + "].");
			
		}catch(Exception e) {
			Logger.error(-46, "ModelsSubDirectoryParser: Failed to parse a Models Directory line. [" + str + "]");
			Logger.error(-46, StringUtils.stackTraceToString(e));
		}
	}
}