package raytrace.data;

import raytrace.surfaces.CompositeSurface;

public class ShadingData{
	
	/*
	 * A storage class for shading data used by Material.shade()
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected CompositeSurface rootSurface;
	protected IntersectionData intersectionData;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ShadingData()
	{
		
	}


	
	
	public CompositeSurface getRootSurface() {
		return rootSurface;
	}

	public void setRootSurface(CompositeSurface rootSurface) {
		this.rootSurface = rootSurface;
	}

	public IntersectionData getIntersectionData() {
		return intersectionData;
	}

	public void setIntersectionData(IntersectionData intersectionData) {
		this.intersectionData = intersectionData;
	}
	
	
	
	

}
