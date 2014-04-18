package file.obj.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import file.StringParser;
import file.obj.ObjModelData;

public class TexCoordParser extends StringParser<ObjModelData> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public TexCoordParser() { keyToken = "vt"; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, ObjModelData pop)
	{
		//Get the tokens
		String[] tokens = tokens(str.trim(), " ");
		
		//Parse the tokens into the obj model
		try{
			
			//First make sure the first token matches the key token
			if(!tokens[0].equals(keyToken))
				throw new Exception("TexCoordParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			if(tokens.length == 4)
			{
				pop.addTexCoord(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
			}else if(tokens.length == 3)
			{
				pop.addTexCoord(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), 0.0);
			}
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse a texcoord line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}