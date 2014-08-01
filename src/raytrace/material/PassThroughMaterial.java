package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;

public class PassThroughMaterial extends Material{
	
	/*
	 * An implementation of a pass-through material
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture texture;
	protected double refractiveIndex;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PassThroughMaterial()
	{
		this.texture = Color.white();
		this.refractiveIndex = AIR_REFRACTIVE_INDEX;
	}
	
	public PassThroughMaterial(Texture texture)
	{
		this.texture = texture;
		this.refractiveIndex = AIR_REFRACTIVE_INDEX;
	}
	
	public PassThroughMaterial(Texture texture, double refractiveIndex)
	{
		this.texture = texture;
		this.refractiveIndex = refractiveIndex;
	}
	

	/* *********************************************************************************************
	 * Material Override
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		//Get color from texture
		Color tint = texture.evaluate(data.getIntersectionData());
		
		//Setup the point of intersection
		Vector3 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		Vector3 normal = data.getIntersectionData().getNormal();
		
		//Ray Direction
		Vector3 rayDir = (new Vector3(data.getRay().getDirection())).normalizeM();
		
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(normal.dot(rayDir) > 0.0)
		{
			normal = normal.multiply(-1.0);
		}
		
		
		Color recursionColor = null;
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Pass right on through
			recursionColor = recurse(data, point, rayDir, refractiveIndex);
		}else{
			//Or terminate
			recursionColor = new Color();
		}
		
		return recursionColor.multiply3(tint);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	public void setRefractiveIndex(double refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}
	
	

}
