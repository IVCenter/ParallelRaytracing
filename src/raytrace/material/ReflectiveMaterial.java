package raytrace.material;

import math.Ray;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class ReflectiveMaterial  extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	double reflectivePercent = 0.5;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ReflectiveMaterial(Color color, double reflectivePercent)
	{
		this.color = color;
		this.reflectivePercent = reflectivePercent;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Color shade = new Color(0x000000ff);
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		IntersectionData shadowData;
		
		//Diffuse lighting and shadows
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(point);
			
			//Get the shadow data for the current light
			shadowData = shadowed(data.getRootScene(), 
					new Ray(point, ildata.getDirection().multiply3(-1.0), 0, 0), 
					ildata.getDistance());
			
			//If we are not in shadow, add to the shade
			if(shadowData == null)
				shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}

		//If reflective, go divin'
		Color rflectColor = new Color();
		if(reflectivePercent != 0.0)
		{
			RayData rdata = new RayData();
			Vector4 dir = data.getIntersectionData().getRay().getDirection();
			Vector4 reflect = dir.add3( normal.multiply3( -2.0 * dir.dot3(normal) ) ).normalize3();
			Ray ray = new Ray(point, reflect, 0, 0);
			rdata.setRay(ray);
			rdata.setTStart(RECURSIVE_EPSILON);
			
			IntersectionData idata = data.getRootScene().intersects(rdata);
			
			ShadingData sdata = new ShadingData();
			sdata.setRay(rdata.getRay());
			sdata.setRootScene(data.getRootScene());
			
			if(idata != null) {
				sdata.setIntersectionData(idata);
				rflectColor = idata.getMaterial().shade(sdata);
			
			//If there wasn't an intersection, use the sky material
			}else{
				sdata.setIntersectionData(null);
				rflectColor = data.getRootScene().getSkyMaterial().shade(sdata);
			}
		}
		
		Color diffuseColor = color.multiply3(shade).multiply3(1.0 - reflectivePercent);
		Color reflectiveColor = rflectColor.multiply3(reflectivePercent);
		return diffuseColor.add3(reflectiveColor);
	}

}