package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class DiffusePTMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected int sampleCount = 1;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DiffusePTMaterial(Color color, int sampleCount)
	{
		this.color = color;
		this.sampleCount = sampleCount;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Color shade = new Color(0x000000ff);
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal().normalize3();
		
		double DdotN = normal.dot3(data.getRay().getDirection());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply3(-1.0);
		}

		
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(normal.dot3(positiveYAxis) == 1.0)
			uTangent = normal.cross3(cosineWeightedSample()).normalize3();
		else
			uTangent = normal.cross3(positiveYAxis).normalize3();
		vTangent = uTangent.cross3(normal).normalize3();
		
		
		//Direct Illumination
		IlluminationData ildata;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor().multiply3(1.0/Math.PI), normal, ildata.getDirection()));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector4 sampleDir;
			for(int i = 0; i < sampleCount; ++i)
			{
				sampleDir = cosineWeightedSample(uTangent, normal, vTangent);
				
				rflectColor.add3M(recurse(data, point, sampleDir, 1.0));
			}
			
			//Add the direct shading and samples shading together
			shade.add3M(rflectColor.multiply3(1.0/sampleCount));
		}
		
		return shade.multiply3M(color);
	}

}