package file.obj;

import java.util.ArrayList;

public class ObjModelData {
	
	/*
	 * A storage object for data loaded from an .obj file
	 * Note: This is a stateful object under mutation.  
	 * 		The add() methods will change behavior based on the internal state
	 * In contrast,
	 * 		The get() methods will always return deterministically
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Object> vertices;//TODO
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ObjModelData()
	{
		
	}

	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	public static class Object {
		
	}
	
	public static class Vertex {
		
	}
	
	public static class Normal {
		
	}
	
	public static class TexCoord {
		
	}
	
	public static class Face {
		
	}
}
