package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import system.Configuration;
import file.StringParser;

public class WorkingDirectoryParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public WorkingDirectoryParser() { keyToken = "workingDirectory"; }
	

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
				throw new Exception("WorkingDirectoryParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String workDir = tokens[1];
			
			//Make sure the Working Directory is reasonable
			if (workDir == null || workDir.isEmpty())
				throw new Exception("WorkingDirectoryParser: Working Directory was invalid [" + workDir + "].");
			
			//Set it
			Configuration.setWorkingDirectory(workDir);
			Logger.progress(-32, "ConfigFileLoader: Setting Working Directory [" + workDir + "].");
			
		}catch(Exception e) {
			Logger.error(-45, "WorkingDirectoryParser: Failed to parse a Working Directory line. [" + str + "]");
			Logger.error(-45, StringUtils.stackTraceToString(e));
		}
	}
}