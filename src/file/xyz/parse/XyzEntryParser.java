package file.xyz.parse;

import math.Vector3;
import process.logging.Logger;
import process.utils.StringUtils;
import raytrace.geometry.Vertex;
import file.StringParser;
import file.xyz.XyzPointCloudData;

public class XyzEntryParser extends StringParser<XyzPointCloudData> {
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public XyzEntryParser() { keyToken = ""; }
	

	/* *********************************************************************************************
	 * Override Parsing Methods
	 * *********************************************************************************************/
	@Override
	public void parse(String str, XyzPointCloudData pop)
	{
		//Get the tokens
		String[] tokens = tokens(str.trim(), " ");
		
		//Parse the tokens into the xyz model
		try{
			
			//First make sure the first token matches the key token
			//if(!tokens[0].equals(keyToken))
			//	throw new Exception("VertexParser: Excepted a key token of [" + keyToken + "] but encountered [" + tokens[0] + "]");
			
			
			//Create a vertex with a position and normal
			Vertex v = new Vertex();
			v.setPosition(new Vector3(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
			v.setNormal(new Vector3(Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5])));
			
			//Add the point
			pop.addPoint(v);
			
		}catch(Exception e) {
			Logger.error(-1, "Failed to parse an xyz line. [" + str + "]");
			Logger.error(-1, StringUtils.stackTraceToString(e));
			e.printStackTrace();
		}
	}
}
