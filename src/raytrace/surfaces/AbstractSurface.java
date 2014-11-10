package raytrace.surfaces;

import java.util.concurrent.atomic.AtomicInteger;

import raytrace.framework.Node;
import raytrace.framework.Surface;

public abstract class AbstractSurface implements Node, Surface {
	
	/*
	 * A base class for nodes of a surface graph
	 */
	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static AtomicInteger nextSurfaceID = new AtomicInteger(1);
	
	
	/* *********************************************************************************************
	 * Static Methods
	 * *********************************************************************************************/
	private static int nextSurfaceID() { return nextSurfaceID.getAndIncrement(); }
	

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected int surfaceID = nextSurfaceID();
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int getSurfaceID() {
		return surfaceID;
	}

	public void setSurfaceID(int surfaceID) {
		this.surfaceID = surfaceID;
	}
	

}
