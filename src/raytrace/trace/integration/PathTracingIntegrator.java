package raytrace.trace.integration;

import java.util.Iterator;
import java.util.LinkedList;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntegrationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.light.Light;
import raytrace.material.Material;
import raytrace.medium.Medium;
import raytrace.scene.Scene;

public class PathTracingIntegrator extends Integrator {

	/*
	 * An implementation of a path tracing integrator
	 */

	/* *********************************************************************************************
	 * Integrate Override
	 * *********************************************************************************************/
	@Override
	public IntegrationData integrate(Scene scene, LinkedList<Medium> mediums, RayData rayData, int recursionDepth)
	{
		//Extract data variables
		
		//IntegrationData
		IntegrationData integrationData = new IntegrationData();
		
		//Illumination Data
		IlluminationData ildata;

		//Light accumulator
		Color result = new Color();

		//LinkedList<Medium> applicableMediumList = getApplicableMediumList(mediums);
		Medium applicableMedium = mediums.getLast();
		//double rayMarchDistance = requestedRayMarchingDistance(applicableMediumList);
		double rayMarchDistance = applicableMedium.getRayMarchDistance();
		
		//Light transport points
		double stochasticRayMarchDistance = Math.random() * rayMarchDistance;
		Vector3 lightTransportStartPoint = rayData.getRay().getOrigin().add(rayData.getRay().getDirection().multiply(stochasticRayMarchDistance));
		Vector3 lightTransportEndPoint = rayData.getRay().getOrigin();
		
		//Get the ray-scene intersection data
		IntersectionData intersectionData = scene.intersects(rayData);
		
		//If there was 
		if(intersectionData != null && 
				rayMarchDistance < Double.POSITIVE_INFINITY && 
				stochasticRayMarchDistance < intersectionData.getDistance() &&
				recursionDepth < DO_NOT_EXCEED_RECURSION_LEVEL && 
				recursionDepth < SYSTEM_RESURSION_LIMIT)
		{
			//Set the intersectionData 
			//TODO: Evaluate the light passing through the medium
			
			//If inside of a medium that requires ray marching
			//Handle the ray step: Cast an auxiliary rays, query any lights, recurse
			//Apply medium to light/recursion result
			
			//Get a sample ray from the medium
			RayData sample = applicableMedium.sample(lightTransportStartPoint, lightTransportEndPoint);
			
			//Integrate the sample
			IntegrationData idata = integrate(scene, mediums, sample, recursionDepth + 1);
			
			//Result color of evaluating the medium
			result = applicableMedium.transmit(lightTransportStartPoint, lightTransportEndPoint, idata.getColor());
			
			//Handle scatter in
			for(Light light : scene.getLightManager())
			{
				//Get illumination data for the current light
				ildata = light.illuminate(scene, lightTransportStartPoint);
				
				//
				ildata.setColor(applicableMedium.transmit(light.getPosition(), lightTransportStartPoint, ildata.getColor()));
				
				//Evaluate the material
				result.add3M(applicableMedium.scatterIn(lightTransportStartPoint, lightTransportEndPoint, ildata.getColor(), ildata.getDirection()));
			}
			
			//Copy across the recursive integration data and set the color value
			integrationData.copy(idata);
			integrationData.getColor().set(result);
			
			return integrationData;
		}
		

		
		//If there was no intersection, evaluate the sky material
		if(intersectionData == null)
		{
			//Evaluate the sky material
			result = scene.getSkyMaterial().evaluateEmission(null, rayData);
			
			//Set the integration data color and return
			integrationData.getColor().set(result);
			return integrationData;
		}
		

		//Otherwise, handle the surface intersection, material evaluation, and any medium evaluation
		
		//Update the light transport start point
		lightTransportStartPoint = intersectionData.getPoint();

		//Material
		Material material;
		
		//Pick the material to use
		if(scene.isUseDefaultMaterial())
		{
			material = scene.getDefaultMaterial();
		}else{
			material = intersectionData.getMaterial();
		}
		
		
		//Determine if the ray is exiting the surface
		boolean exiting = (intersectionData.getRay().getDirection().dot(intersectionData.getNormal()) > 0.0);
		
		//If we are exiting the material, remove the medium of this material from the medium stack
		if(material.getMedium() != null && exiting)
		{
			//TODO: Re-deisgn how the medium stack works
			//Also take note of list.remove remvoing items with the lowest index first 
			mediums.remove(material.getMedium());
		}
		
		
		//Re-usable Material Evaluation Data object
		//MaterialEvaluationData mdata = new MaterialEvaluationData();
		
		
		//For all lights
		//Test for shadowing
		//Evaluate the material
		//Accumulate the light leaving the surface in the direction of the parameter ray
		Vector3 intersectionPoint = intersectionData.getPoint();
		Color mediumScatterIn = new Color();
		if(material.isAffectedByLightSources() || applicableMedium.getType().compareTo(Medium.Type.NONE) != 0)
		{
			for(Light light : scene.getLightManager())
			{
				//Get illumination data for the current light
				ildata = light.illuminate(scene, intersectionPoint);
				
				//If inside a medium, evaluate the light passing through the medium (from intersection point to ray origin)
				if(applicableMedium.getType().compareTo(Medium.Type.NONE) != 0)
				{
					//Result color of evaluating the medium
					ildata.setColor(applicableMedium.transmit(light.getPosition(), lightTransportStartPoint, ildata.getColor()));
				}
				
				//Evaluate the material
				if(material.isAffectedByLightSources())
					result.add3M(material.evaluateDirectLight(intersectionData, rayData, ildata.getColor(), ildata.getDirection()));
				
				if(applicableMedium.getType().compareTo(Medium.Type.NONE) != 0)
					mediumScatterIn.add3M(applicableMedium.scatterIn(lightTransportStartPoint, lightTransportEndPoint, ildata.getColor(), ildata.getDirection()));
			}
		}
		
		
		//Determine if a sample needs to be cast
		//If yes, use the material to generate a sample
		//If transmitting, and material has a medium
		//	If entering, push onto medium stack
		//	If exiting, remove from medium stack
		
		//If we are not at any recursion limits, and the path isn't terminated by chance
		Recurse:
		if(recursionDepth < DO_NOT_EXCEED_RECURSION_LEVEL && 
				recursionDepth < SYSTEM_RESURSION_LIMIT && 
				material.isGloballyIlluminated() &&
				Math.random() > pathTerminationProbability)
		{
			//Create a RayData and cast integrate the sample
			RayData sample = material.sample(intersectionData, rayData);

			//If there is not a sample, no recursion
			if(sample == null)
				break Recurse;
			
			//If transmitting, push on the medium of the current material
			if(!exiting && sample.getType().compareTo(RayData.Type.TRASMIT) == 0 && material.getMedium() != null)
				mediums.addLast(material.getMedium());
			
			//Integrate the sample
			IntegrationData idata = integrate(scene, mediums, sample, recursionDepth + 1);
			
			//If transmitting, pop off the medium of the current material
			if(!exiting && sample.getType().compareTo(RayData.Type.TRASMIT) == 0 && material.getMedium() != null)
				mediums.remove(material.getMedium());
			
			//Evaluate the material
			result.add3M(material.evaluateSampledLight(intersectionData, rayData, idata.getColor(), sample));
		}
		
		
		//Handle emission
		if(material.emitsLight())
		{
			result.add3M(material.evaluateEmission(intersectionData, rayData));
		}
		
		
		//If inside a medium, evaluate the light passing through the medium (from intersection point to ray origin)
		if(applicableMedium.getType().compareTo(Medium.Type.NONE) != 0)
		{
			//Result color of evaluating the medium
			result = applicableMedium.transmit(lightTransportStartPoint, lightTransportEndPoint, result);
			
			//Handle scatter in
			result.add3M(mediumScatterIn);
		}
		
		
		//Set the integration data fields
		integrationData.setDidIntersect(true);
		integrationData.setDistance(intersectionData.getDistance());
		integrationData.getPoint().set(integrationData.getPoint());
		integrationData.getNormal().set(integrationData.getNormal());
		integrationData.getColor().set(result);
		
		return integrationData;
	}
	
	protected LinkedList<Medium> getApplicableMediumList(LinkedList<Medium> mediums)
	{
		LinkedList<Medium> applicable = new LinkedList<Medium>();
		
		Iterator<Medium> iter = mediums.descendingIterator();
		Medium m;
		while(iter.hasNext())
		{
			m = iter.next();
			applicable.addFirst(m);
			
			if(m.getType().compareTo(Medium.Type.PURE) == 0)
				break;
		}
		
		return applicable;
	}
	
	protected double requestedRayMarchingDistance(LinkedList<Medium> mediums)
	{
		double minimumDistance = Double.POSITIVE_INFINITY;
		double distance;
		
		for(Medium m: mediums)
		{
			if(m.isRayMarching())
			{
				distance = m.getRayMarchDistance();
				if(distance < minimumDistance)
					minimumDistance = distance;
			}
		}
		
		return minimumDistance;
	}

}
