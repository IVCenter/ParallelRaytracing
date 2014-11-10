package raytrace.surfaces;

import math.Matrix4;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.material.Material;

public class MatrixInterpolationInstance extends Instance {
	
	/*
	 * A convenient extension ;)
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Matrix4 startMatrix;
	protected Matrix4 endMatrix;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MatrixInterpolationInstance()
	{
		//
		super();
	}
	
	public MatrixInterpolationInstance(Matrix4 startMatrix, Matrix4 endMatrix)
	{
		super();
		this.startMatrix = startMatrix;
		this.endMatrix = endMatrix;
	}
	

	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public void setMaterial(Material material)
	{
		//this.material = material;
	}
	
	@Override
	public IntersectionData intersects(RayData data)
	{
		//Get time
		//Interpolate matrix
		//Set matrix
		
		//As awkward as this seems, it works.
		//double time = data.getRay().getRandomValue();
		
		
		
		
		
		return super.intersects(data);
	}
	

	/* *********************************************************************************************
	 * Geters/Setters
	 * *********************************************************************************************/
	public Matrix4 getStartMatrix() {
		return startMatrix;
	}

	public void setStartMatrix(Matrix4 startMatrix) {
		this.startMatrix = startMatrix;
	}

	public Matrix4 getEndMatrix() {
		return endMatrix;
	}

	public void setEndMatrix(Matrix4 endMatrix) {
		this.endMatrix = endMatrix;
	}
}