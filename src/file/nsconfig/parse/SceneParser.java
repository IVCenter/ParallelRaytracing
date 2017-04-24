package file.nsconfig.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import raytrace.scene.Scene;
import raytrace.scene.SceneLoader;
import system.Configuration;
import file.StringParser;

public class SceneParser extends StringParser<Configuration> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SceneParser() { keyToken = "scene"; }
	

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
				throw new Exception("SceneParser: Expected a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			String scene = tokens[1];
			
			//If scene is NONE, bounce and let the caller handle it (this is an explcit command to do nothing)
			if(scene == "NONE")
				return;
			
			//Make sure the Scene is reasonable
			if (scene == null || scene.isEmpty())
				throw new Exception("SceneParser: Scene was invalid [" + scene + "].");

			Logger.message(-32, "ConfigFileLoader: Setting Scene [" + scene + "].");
			Scene masterScene = SceneLoader.load(scene);
			
			//Make sure the master scene isnt null
			if (masterScene == null)
				throw new Exception("SceneParser: Scene was invalid [" + scene + "].");
			
			//Set it
			Configuration.setMasterScene(masterScene);
			
		}catch(Exception e) {
			Logger.error(-43, "SceneParser: Failed to parse a scene line. [" + str + "]");
			Logger.error(-43, StringUtils.stackTraceToString(e));
		}
	}
}