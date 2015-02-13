package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Matrix4;
import math.Spline;
import math.Vector3;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.TchebyshevDistance3D;
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
import raytrace.framework.Surface;
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
import raytrace.map.texture._3D.WorleyNoiseTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.MaskT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.Material;
import raytrace.material.ReflectiveMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.SubSurfaceDiffusePTTestMaterial;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.blend.binary.SelectDarkestBBlend;
import raytrace.material.blend.unary.MultiplicativeUBlend;
import raytrace.material.blend.unary.TwoToneNPRUBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import resource.ResourceManager;
import system.Configuration;

public class CSE165_2015_DataSmoothingDiagrams extends Scene
{	
	/*
	 * A simple scene for cse165 project2 that generates diagrams of the affects of smoothing/de-noising (2015)
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
	tracers.add(pixeler);
		
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.001, 3, 24));
		outliner.setCreaseTexture(new Color(0x222222ff));
		outliner.setOcclusionTexture(new Color(0x222222ff));
		outliner.setSilhouetteTexture(new Color(0x222222ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		tracers.add(outliner);
		
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
		camera.setStratifiedSampling(true);
		camera.setSuperSamplingLevel(4);
		camera.setPosition(new Vector3(0,0,3));
		camera.setViewingDirection(new Vector3(0,0,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new CircularAperture(0.02, 0.4));
		camera.setFocalPlaneDistance(3.0);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{				
		Material skyMaterial = new ColorMaterial(Color.white());
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	@Override
	protected void configureWorld()
	{
		//Generate data points
		//Animate the data stream and algorithm
		
		ColorMaterial cmat = new ColorMaterial(new Color(0x666666ff));
		Sphere s;
		ArrayList<AbstractSurface> surfaces = new ArrayList<AbstractSurface>();
		
		for(int i = 0; i < 100; ++i)
		{
			s = new Sphere(0.03, (Vector3.uniformSphereSample()).multiplyM(3.0).multiplyM(new Vector3(1, 1, 0)));
			s.setMaterial(cmat);
			surfaces.add(s);
		}
		
		
		AbstractSurface accel = AABVHSurface.makeAABVH(surfaces);
		
		this.addChild(accel);
		
	}
	
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		
		
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