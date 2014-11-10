package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Matrix4;
import math.Spline;
import math.Vector3;
import math.ray.CircularRayStencil;
import process.logging.Logger;
import raster.pixel.ColorInversionPT;
import raytrace.bounding.BoundingBox;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.Grid;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.geometry.pointclouds.PointCloudSurface;
import raytrace.geometry.pointclouds.PointSurface;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.map.texture._3D.CircularGradientTexture3D;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SineWaveTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.Texture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.MaskT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.Material;
import raytrace.material.ReflectiveMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.blend.binary.SelectDarkestBBlend;
import raytrace.material.blend.unary.TwoToneNPRUBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import resource.ResourceManager;
import system.Configuration;

public class CSE167_2014_Project2 extends Scene
{	
	/*
	 * A simple test scene for cse167 project2 inspirations (2014)
	 */

	/* *********************************************************************************************
	 * Local Variables
	 * *********************************************************************************************/
	double elapsed = 0.0;
	
	
	/* *********************************************************************************************
	 * Tracers
	 * *********************************************************************************************/
	@Override
	protected List<Tracer> configureTracers()
	{
		ArrayList<Tracer> tracers = new ArrayList<Tracer>(2);
		
		//Standard ray tracer
		tracers.add(new RayTracer());
		
		
		//Pixel Transform Tracer
		ProgrammablePixelTracer pixeler = new ProgrammablePixelTracer();
		pixeler.addTransform(new ColorInversionPT());
		//tracers.add(pixeler);
		
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.0005, 3, 24));
		outliner.setCreaseTexture(new Color(0x111111ff));
		outliner.setOcclusionTexture(new Color(0x111111ff));
		outliner.setSilhouetteTexture(new Color(0x111111ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		//tracers.add(outliner);
		
		return tracers;
	}

	
	/* *********************************************************************************************
	 * Camera
	 * *********************************************************************************************/
	@Override
	protected Camera configureCamera()
	{
		ProgrammableCamera camera = new ProgrammableCamera();
		
		camera = new ProgrammableCamera();
		camera.setStratifiedSampling(false);
		camera.setSuperSamplingLevel(1);
		camera.setPosition(new Vector3(0,2.85,3));
		camera.setViewingDirection(new Vector3(0,-0.1,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new CircularAperture(0.02, 0.4));
		camera.setFocalPlaneDistance(7.2);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{				
		Material skyMaterial = new ColorMaterial(Color.black());
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	Instance cloud;
	ArrayList<PointSurface> points;
	PointLight pointLight;
	Color lightColor;
	@Override
	protected void configureWorld()
	{
		//Point light
		pointLight = new PointLight();
		pointLight.setColor(Color.white());
		pointLight.setIntensity(36.14159);//was 36
		pointLight.setQuadraticAttenuation(1.0);
		pointLight.setConstantAttenuation(0.0);
		pointLight.setPosition(new Vector3(-2.0, 6.0, -2.5));
		lightManager.addLight(pointLight);
		lightColor = pointLight.getColor().multiply3(pointLight.getIntensity());
		
		
		//Point Cloud
		cloud = ResourceManager.create("dragon.xyz");
		
		if(cloud != null) {
			cloud.getTransform().scale(30.0);//ia

			cloud.getTransform().translate(0, 0, -4.2);
			cloud.getTransform().rotateY(0.45);
			cloud.bake(null);

			//Place the cloud on the ground
			BoundingBox bb = cloud.getBoundingBox();
			cloud.getTransform().translate(-1.0 * bb.getMidpoint().get(0), -1.0 * bb.min.get(1), 0);
			cloud.bake(null);
			
			
			Color flatColor = Color.gray(0.9);
			Matrix4 inv = cloud.getTransform();//.inverse();
			
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					(new Color(0xff0068ff)).multiply3M(0.95), 
					(new Color(0.99, 0.66, 0.2)).multiply3M(0.95), 
					0.6
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(3.13);
			gradientTrans.getTransform().translate(0, 0, 0);

			
			//Set normal colors
			points = ((PointCloudSurface)cloud.getChildren().get(0)).getPointSurfaces();
			for(PointSurface point : points)
			{
				Vector3 norm = new Vector3(point.getPoint().getNormal());
				//Vector3 norm = new Vector3(point.getPoint().getPosition());
				norm.normalizeM();
				if(norm.magnitude() == 0.0)
				{
					//point.setMaterial(new ColorMaterial(new Color(norm.get(0), norm.get(1), norm.get(2))));
				}
				else
				{
					//point.setMaterial(new DiffusePTMaterial(new Color(norm.get(0) / 2.0 + 0.5, norm.get(1) / 2.0 + 0.5, norm.get(2) / 2.0 + 0.5)));
					//point.setMaterial(new ColorMaterial(new Color(norm.get(0) / 2.0 + 0.5, norm.get(1) / 2.0 + 0.5, norm.get(2) / 2.0 + 0.5)));
				}
				
				point.setMaterial(new ColorMaterial(diffuseLighting(
						pointLight.getPosition(), 
						inv.multiplyPt(point.getPosition()), 
						inv.multiply3(norm).normalizeM(), 
						lightColor, 
						gradientTrans.evaluate(point.getPosition().get(0), point.getPosition().get(1), point.getPosition().get(2)))));
				
			}

			//cloud.setMaterial(new DiffusePTMaterial(gradientTrans));
			
			this.addChild(cloud);
		}else{
			Logger.error(-13, "CSE167_2014_Project2: Cloud was null!");
		}
		
	}
	
	private Color diffuseLighting(Vector3 lightPos, Vector3 objPos, Vector3 normal, Color lightColor, Color objColor)
	{
		Color result = new Color();
		
		final double oneOverPi = 1.0 / Math.PI; 
		Vector3 toLight = lightPos.subtract(objPos);
		double distToLight = toLight.magnitude();

		toLight.normalizeM();
		
		if(toLight.magnitudeSqrd() == 0.0)
			return lightColor.multiply3(1.0);

		double dot = normal.dot(toLight);
		
		if(dot <= 0.0)
			return Color.black();
		
		result.add3M(lightColor);
		result.multiply3M(1.0 / (distToLight * distToLight));
		result.multiply3M( dot * oneOverPi );
		result.multiply3M(objColor);
		
		//result = Color.gray(distToLight/10);
		
		return result;
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();

		cloud.getTransform().rotateY(data.getDt());
		
		
		Color flatColor = Color.gray(0.9);
		Matrix4 inv = cloud.getTransform();//.inverse();
		
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				(new Color(0xff0068ff)).multiply3M(0.95), 
				(new Color(0.99, 0.66, 0.2)).multiply3M(0.95), 
				0.6
				);
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
		gradientTrans.getTransform().scale(3.13);
		gradientTrans.getTransform().translate(0, 0, 0);

		
		//Set normal colors
		points = ((PointCloudSurface)cloud.getChildren().get(0)).getPointSurfaces();
		for(PointSurface point : points)
		{
			Vector3 norm = new Vector3(point.getPoint().getNormal());
			//Vector3 norm = new Vector3(point.getPoint().getPosition());
			norm.normalizeM();
			if(norm.magnitude() == 0.0)
			{
				//point.setMaterial(new ColorMaterial(new Color(norm.get(0), norm.get(1), norm.get(2))));
			}
			else
			{
				//point.setMaterial(new DiffusePTMaterial(new Color(norm.get(0) / 2.0 + 0.5, norm.get(1) / 2.0 + 0.5, norm.get(2) / 2.0 + 0.5)));
				//point.setMaterial(new ColorMaterial(new Color(norm.get(0) / 2.0 + 0.5, norm.get(1) / 2.0 + 0.5, norm.get(2) / 2.0 + 0.5)));
			}
			
			point.setMaterial(new ColorMaterial(diffuseLighting(
					pointLight.getPosition(), 
					inv.multiplyPt(point.getPosition()), 
					inv.multiply3(norm).normalizeM(), 
					lightColor, 
					gradientTrans.evaluate(point.getPosition().get(0), point.getPosition().get(1), point.getPosition().get(2)))));
		}
		
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		super.bake(data);
	}
}