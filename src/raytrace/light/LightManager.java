package raytrace.light;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class LightManager implements Iterable<Light>{


	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Light> lights;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public LightManager()
	{
		lights = new ArrayList<Light>();
	}
	

	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public void addLight(Light l)
	{
		if(l != null)
			lights.add(l);
	}
	
	public void addLights(Collection<Light> lights)
	{
		if(lights == null)
			return;
		
		for(Light light : lights)
			addLight(light);
	}
	

	/* *********************************************************************************************
	 * Iterable Overrides
	 * *********************************************************************************************/
	@Override
	public Iterator<Light> iterator()
	{
		return lights.iterator();
	}

}
