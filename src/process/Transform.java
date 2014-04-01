package process;

import java.util.HashMap;

public class Transform
{
	/*
	 * Vars
	 */
	private HashMap<String, String> trans;
	
	public Transform()
	{
		trans = new HashMap<String, String>();
	}
	
	public void add(String input, String output)
	{
		trans.put(input, output);
	}
	
	public String remove(String input)
	{
		String output = transform(input);
		trans.remove(input);
		return output;
	}
	
	public String transform(String input)
	{
		String result = trans.get(input);
		if(result == null)
			return input;
		return result;
	}

}
