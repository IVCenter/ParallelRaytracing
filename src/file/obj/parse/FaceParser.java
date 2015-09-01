package file.obj.parse;

import process.logging.Logger;
import process.utils.StringUtils;
import file.StringParser;
import file.obj.ObjModelData;

public class FaceParser extends StringParser<ObjModelData> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FaceParser() { keyToken = "f"; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, ObjModelData pop)
	{
		//Get the tokens
		String[] tokens = tokens(str.trim().replace("  ", " "), " ");
		
		//Parse the tokens into the obj model
		try{
			
			//First make sure the first token matches the key token
			if(!tokens[0].equals(keyToken))
				throw new Exception("FaceParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			pop.addFace();
			
			//If its a vertices only face
			if(tokens.length == 4 && str.indexOf("/") == -1)
			{
				for(int i = 1; i < tokens.length; ++i)
				{
					pop.addVertexToFace(Integer.parseInt(tokens[i]), 0, 0);
				}
				
				return;
			}
			
			
			//For all of the face tokens
			String[] faceComponents;
			for(int i = 1; i < tokens.length; ++i)
			{
				faceComponents = tokens(tokens[i], "/");
				
				if(faceComponents.length == 2)
				{
					faceComponents = new String[]{faceComponents[0], "0", faceComponents[1]};
				}
				
				if(faceComponents.length != 3)
				//	break;
					throw new Exception("FaceParser: The line [" + str + "] has an improperly formatted part [" + tokens[i] + "].");
				
				for(int j = 0; j < faceComponents.length; ++j)
				{
					if(faceComponents[j].length() == 0)
						faceComponents[j] = "0";
				}
				
				pop.addVertexToFace(Integer.parseInt(faceComponents[0]), Integer.parseInt(faceComponents[1]), Integer.parseInt(faceComponents[2]));
			}
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse a face line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}