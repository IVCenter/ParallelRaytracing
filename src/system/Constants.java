package system;

import java.util.HashSet;
import process.logging.Logger;

public class Constants {
	
	//Sets the global debug flag, FALSE has precedence.
	public static final Boolean GLOBAL_DEBUG = true;
	public static final String lineDelimiter = System.getProperty("file.separator");
	
	//Working Directory
	public static final String workingDirectory = "/";
	
	
	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * Global Parameter Keys
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	public static class ParameterKeys
	{
		//Keep track of keys used to prevent duplicate keys from being used
		public static final KeySet KEYS = new KeySet();
		
		//The null key
		public static final String NULL = null;
		public static final String I_ARE_CAN_HAZ_KEY_PLEEZ = NULL;
		
		public static final String example = KEYS.addKey("EXAMPLE KEY");
	}
	

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * Scene Keys
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	public static class SceneKeys
	{
		//Keep track of keys used to prevent duplicate keys from being used
		public static final KeySet KEYS = new KeySet();

		public static final String TEST1 = KEYS.addKey("Test1");
		public static final String TEST2 = KEYS.addKey("Test2");
		public static final String CSE168_Project1 = KEYS.addKey("CSE168_Project1");
	}
	
	
	/*
	 * ParameterKeySet
	 * 
	 * Desc: Same thing as a HashSet, except that it reports to logging when a duplicate key is added
	 */
	@SuppressWarnings("serial")
	private static class KeySet extends HashSet<String>
	{
		public String addKey(String item)
		{
			if(this.contains(item)) {
				Logger.error(-1, "CONSTANTS ERROR: You have added the key [" + item + "] multiple times!");
				System.exit(-1);
			}else
				this.add(item);
			
			return item;
		}
	}
	
	
	
}



