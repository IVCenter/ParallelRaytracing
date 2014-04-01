package file;

public abstract class StringParser<POPULATES_THIS_TYPE> {
	
	/*
	 * A base class for parsing strings and returning an object
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected String keyToken = null;
	
	/* *********************************************************************************************
	 * Abstract Parsing Methods
	 * *********************************************************************************************/
	public abstract void parse(String str, POPULATES_THIS_TYPE pop);
	

	/* *********************************************************************************************
	 * Consrete Utility Methods
	 * *********************************************************************************************/
	protected String[] tokens(String str, String pattern)
	{
		return str.trim().split(pattern);
	}
	
	public String getKeyToken() { return keyToken; }
}
