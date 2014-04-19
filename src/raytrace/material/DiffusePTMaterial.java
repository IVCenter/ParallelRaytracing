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
		Vector4 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		double DdotN = normal.dot3(data.getRay().getDirection());
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			normal = normal.multiply3(-1.0);
		}
		
		
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		
		if(data.getRecursionDepth() < 1)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector4 sampleDir = new Vector4(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, 0);
			for(int i = 0; i < sampleCount; ++i)
			{
				while(sampleDir.dot3(normal) <= 0) {
					sampleDir.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, 0);
				}
				//sampleDir.set(0.4 * (Math.random() - 0.5), 0.4 * (Math.random() - 0.5), 0.4 * (Math.random() - 0.5), 0);
				//sampleDir = normal.add3(sampleDir).normalize3();
				
				sampleDir.normalize3();
				rflectColor.add3M(
						diffuse(recurse(data, point, sampleDir, 1.0).multiply3(1.0/sampleCount), normal, sampleDir.multiply3(-1.0)));//TODO: Should we use data.refractiveInde here?
			}
			
			//Add the direct shading and samples shading together
			shade.add3M(rflectColor);
		}
		
		return shade.multiply3M(color);
	}

}