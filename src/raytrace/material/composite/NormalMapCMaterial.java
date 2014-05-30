package raytrace.material.composite;

import math.Matrix4;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.normal.FlatNormalMap;
import raytrace.map.normal.NormalMap;
import raytrace.material.Material;

public class NormalMapCMaterial extends CompositeMaterial {

	/*
	 * A unary blend that modifies the normal of a surface
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected NormalMap normalMap;
	protected double sampleRadius;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NormalMapCMaterial(Material material)
	{
		super(material);
		normalMap = new FlatNormalMap();
		this.sampleRadius = 0.0001;
	}
	
	public NormalMapCMaterial(Material material, NormalMap normalMap)
	{
		super(material);
		this.normalMap = normalMap;
		this.sampleRadius = 0.0001;
	}
	
	public NormalMapCMaterial(Material material, NormalMap normalMap, double sampleRadius)
	{
		super(material);
		this.normalMap = normalMap;
		this.sampleRadius = sampleRadius;
	}

	
	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		//TODO: Modify the normal vector
		Vector4 normal = normalMap.evaluate(data.getIntersectionData());
		
		//THIS GOES IN NORMAL MAP 3D!!!
		//Create u,v tangents
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(Math.abs(normal.dot3(positiveZAxis)) == 1.0)
			uTangent = positiveYAxis.cross3(normal).normalize3();
		else
			uTangent = normal.cross3(positiveZAxis).normalize3();
		vTangent = uTangent.cross3(normal).normalize3();
		
		//Setup a sample point
		Vector4 samplePoint = new Vector4();
		

		
		
		
		//Store the old normal
		Vector4 oldNormal = data.getIntersectionData().getNormal().normalize3();
		
		//TODO: These next few lines are expensive.....
		//Determine the angle between the oldNormal and the yaxis
		double theta = Math.acos(oldNormal.dot3(positiveYAxis));
		Vector4 axisOfRotation = oldNormal.cross3(positiveYAxis);
		
		//Rotate the normal
		Matrix4 rotationMatrix = new Matrix4();
		rotationMatrix.identity();
		rotationMatrix.rotateArbitrary(axisOfRotation, theta);
		
		normal = rotationMatrix.multiply3(normal);
		
		
		//Set the normal to the new one
		data.getIntersectionData().setNormal(normal);
		
		//Shade
		Color shade = material.shade(data);
		
		//Put the old normal back
		data.getIntersectionData().setNormal(oldNormal);
		return shade;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public NormalMap getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(NormalMap normalMap) {
		this.normalMap = normalMap;
	}

}
