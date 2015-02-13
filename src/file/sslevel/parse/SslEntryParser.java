package file.sslevel.parse;

import math.Vector3;
import process.logging.Logger;
import process.utils.StringUtils;
import file.StringParser;
import file.sslevel.Gate;
import file.sslevel.SslLevelData;

public class SslEntryParser extends StringParser<SslLevelData> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SslEntryParser() { keyToken = ""; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, SslLevelData pop)
	{
		//Get the tokens
		String[] tokens = tokens(str.trim(), " ");
		
		//Parse the tokens into the ssl level
		try{
			
			//Make sure there are enough tokens
			if(tokens.length != 9)
				throw new Exception("SslEntryParser: Excepted a token count of [" + 9 + "] but encountered [" + tokens.length + "]");
			
			//Create a gate with center, right and up vectors
			Gate g = new Gate();
			g.setCenter(new Vector3(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
			g.setRight(new Vector3(Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5])));
			g.setUp(new Vector3(Double.parseDouble(tokens[6]), Double.parseDouble(tokens[7]), Double.parseDouble(tokens[8])));
			
			//Add the point
			pop.addGate(g);
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse an ssl line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}