package file.obj.parse;

import file.StringParser;
import file.obj.ObjModelData;

public class VertexParser extends StringParser<ObjModelData> {

	@Override
	public void parse(String str, ObjModelData pop)
	{
		//Get the tokens
		String[] tokens = tokens(str, " ");
	}
}
