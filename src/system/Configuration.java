package system;

import process.Environment;
import raytrace.scene.Scene;

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
	protected static int screenWidth = 1024;
	protected static int screenHeight = 640;
	
	protected static boolean drawToScreen = false;
	protected static boolean isClock = false;
	protected static boolean isLeaf = false;
	
	protected static Scene masterScene = null;

	
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

	//Screen Width
	public static int getScreenWidth() { return screenWidth; }
	public static void setScreenWidth(int screenWidth) { Configuration.screenWidth = screenWidth; }

	//Screen Height
	public static int getScreenHeight() { return screenHeight; }
	public static void setScreenHeight(int screenHeight) { Configuration.screenHeight = screenHeight; }

	//Draw to screen
	public static boolean isDrawingToScreen() { return drawToScreen; }
	public static void setDrawToScreen(boolean drawToScreen) { Configuration.drawToScreen = drawToScreen; }

	//Clock
	public static boolean isClock() { return isClock; }
	public static void setClock(boolean isClock) { Configuration.isClock = isClock; }
	
	//Leaf
	public static boolean isLeaf() { return isLeaf; }
	public static void setLeaf(boolean isLeaf) { Configuration.isLeaf = isLeaf; }

	//Master Scene
	public static Scene getMasterScene() { return masterScene; }
	public static void setMasterScene(Scene masterScene) { Configuration.masterScene = masterScene; }
	
}
