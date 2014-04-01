package system;

import process.Environment;

public class Configuration {
	
	/*
	 * A configuration of this running instance
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	//Environment for storing custom configuration parameters
	protected static final Environment env;
	
	//Defined parameters
	protected static String id = "Unassigned";

	
	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static {
		env = new Environment();
	}
	

	/* *********************************************************************************************
	 * Private Constructor to block instantiation
	 * *********************************************************************************************/
	private Configuration() { /**/ }

	/* *********************************************************************************************
	 * Static Getter/Setter Methods
	 * *********************************************************************************************/
	public static <T> T get(String key)
	{
		return env.get(key);
	}
	
	public static void set(String key, Object value)
	{
		env.set(key, value);
	}
	
	//ID
	public static String getId() { return id; }
	public static void setId(String id) { Configuration.id = id; }
	
	//
	
}
