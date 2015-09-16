package system;

import java.util.HashSet;

import process.logging.Logger;

public class Constants {
	
	//Sets the global debug flag, FALSE has precedence.
	public static final Boolean GLOBAL_DEBUG = true;
	public static final String lineDelimiter = System.getProperty("file.separator");
	
	//Working Directory
	public static final String workingDirectory = "/";
	public static final String configurationFileExtension = ".nsconfig";
	
	//Tracing constants
	public static final double RECURSIVE_EPSILON = 0.00001;
	
	
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

		//NOTE: Scene implementations have been removed from the engine source code
		//TODO: Remove these scene constants
		/*
		public static final String TEST1 = KEYS.addKey("Test1");
		public static final String TEST2 = KEYS.addKey("Test2");
		public static final String TEST3 = KEYS.addKey("Test3");
		public static final String TEST4 = KEYS.addKey("Test4");
		public static final String TEST5 = KEYS.addKey("Test5");
		public static final String TEST6 = KEYS.addKey("Test6");
		public static final String TEST7 = KEYS.addKey("Test7");
		public static final String TEST8 = KEYS.addKey("Test8");
		public static final String TEST9 = KEYS.addKey("Test9");
		public static final String TEST10 = KEYS.addKey("Test10");
		public static final String TEST11 = KEYS.addKey("Test11");
		public static final String TEST12 = KEYS.addKey("Test12");
		public static final String TEST13 = KEYS.addKey("Test13");
		
		public static final String CSE168_Project1 = KEYS.addKey("CSE168_Project1");
		public static final String CSE168_Project2 = KEYS.addKey("CSE168_Project2");
		public static final String CSE168_Project3 = KEYS.addKey("CSE168_Project3");
		
		public static final String CSE167_2014_Project2 = KEYS.addKey("CSE167_2014_Project2");
		
		public static final String Performance_TEST1 = KEYS.addKey("Performance_Test1");
		
		public static final String Diffuse_Glass_TEST = KEYS.addKey("Diffuse_Glass_Test");
		
		public static final String VoxelTest1 = KEYS.addKey("VoxelTest1");
		
		public static final String CSE165_2015_DataSmoothingDiagrams = KEYS.addKey("CSE165_2015_DataSmoothingDiagrams");
		*/
	}
	
	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * Message Keys
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	public static class Message
	{
		public static class Registrion
		{
			//Keep track of keys used to prevent duplicate keys from being used
			public static final KeySet KEYS = new KeySet();

			//public static final String PERFORMANCE_SCORE = KEYS.addKey("Test1");
			//public static final String CORES = KEYS.addKey("Test1");
			//public static final String PERFORMANCE_SCORE = KEYS.addKey("Test1");
		}
		
		
		//Keep track of keys used to prevent duplicate keys from being used
		public static final KeySet KEYS = new KeySet();

		public static final String NODE_ID = KEYS.addKey("NODE_ID");
		public static final String NODE_CORES = KEYS.addKey("NODE_CORES");
		public static final String NODE_IP = KEYS.addKey("NODE_IP");

		public static final String SCREEN_WIDTH = KEYS.addKey("SCREEN_WIDTH");
		public static final String SCREEN_HEIGHT = KEYS.addKey("SCREEN_HEIGHT");

		public static final String STATE_IS_LEAF = KEYS.addKey("STATE_IS_LEAF");
		public static final String STATE_IS_DRAWINGTOSCREEN = KEYS.addKey("STATE_IS_DRAWINGTOSCREEN");
		public static final String STATE_IS_CLOCK = KEYS.addKey("STATE_IS_CLOCK");
		public static final String STATE_IS_CONTROLLER = KEYS.addKey("STATE_IS_CONTROLLER");
		public static final String STATE_IS_REALTIME = KEYS.addKey("STATE_IS_REALTIME");
		
		public static final String SCENE_KEY = KEYS.addKey("SCENE_KEY");

		//This is unsafe, add specific items only
		//public static final String RENDER_DATA = KEYS.addKey("RENDER_DATA");
		//public static final String UPDATE_DATA = KEYS.addKey("UPDATE_DATA");

		public static final String NODE_CAMERA = KEYS.addKey("NODE_CAMERA");
		public static final String NODE_PIXELS = KEYS.addKey("NODE_PIXELS");
		public static final String DELTA_TIME = KEYS.addKey("DELTA_TIME");
		//public static final String SCENE_KEY = KEYS.addKey("SCENE_KEY");
		
		
	}

	
	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * Default Values
	 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	public static class Default
	{
		public static final String NODE_ID = "Unassigned";
		//TODO: Put these on the configuration.
		public static final int REGISTRATION_LOOP_SLEEP_TIME = 10000;
		public static final int INTERMEDIATE_RESULT_LOOP_SLEEP_TIME = 1000;
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
			if(this.contains(item))
			{
				Logger.error(-1, "CONSTANTS ERROR: You have added the key [" + item + "] multiple times!");
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.exit(-1);
				
			}else{
				this.add(item);
			}
			
			return item;
		}
	}
	
	
	
}



