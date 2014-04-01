package math;

import javax.vecmath.Vector4d;

public class Vector4 extends Vector4d {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6002825425579913543L;

	
	
	/*
	 * An extension of the Vector4d class to allow for additional features
	 */
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vector4()
	{
		super(0,0,0,0);
	}
	
	public Vector4(double x, double y, double z, double w)
	{
		super(x,y,z,w);
	}
	
	public Vector4(Vector4 v)
	{
		super(v.x,v.y,v.z,v.w);
	}
}
