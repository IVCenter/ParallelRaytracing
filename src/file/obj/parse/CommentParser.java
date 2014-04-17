package file.obj.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import file.StringParser;
import file.obj.ObjModelData;

public class CommentParser extends StringParser<ObjModelData> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CommentParser() { keyToken = "#"; }
	

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
				throw new Exception("CommentParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			Logger.progress(-11, "ObjectFileLoader: Comment: " + str);
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse a texcoord line.");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}
