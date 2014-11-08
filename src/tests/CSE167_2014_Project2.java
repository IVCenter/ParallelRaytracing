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
		camera.setStratifiedSampling(true);
		camera.setSuperSamplingLevel(8);
		camera.setPosition(new Vector3(0,2.85,3));
		camera.setViewingDirection(new Vector3(0,-0.1,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new CircularAperture(0.06 * 0, 0.4));
		camera.setFocalPlaneDistance(6.2);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{
		
		Material skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				//new SkyGradientMaterial(new GradientTexture3D(new Color(0xfbfbfbff), new Color(0x999999ff), 4.0)),
				//new SkyGradientMaterial(new GradientTexture3D(new Color(0x7b8b9bff), new Color(0x445474ff), 5.0)),
				//new ColorMaterial(Color.black()),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffffffff), new Color(0x999999ff), 5.0)),
				1
				);
		
		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0x8c96eeff), 5.0)),
				1
				);
				
		//skyMaterial = new ColorMaterial(Color.black());
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

		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector3(1,-1,-1));
		//lightManager.addLight(directionalLight);

		pointLight = new PointLight();
		pointLight.setColor(Color.white());
		pointLight.setIntensity(36.14159);//was 36
		pointLight.setQuadraticAttenuation(1.0);
		pointLight.setConstantAttenuation(0.0);
		pointLight.setPosition(new Vector3(-2.0, 6.0, -0.5));
		lightManager.addLight(pointLight);
		lightColor = pointLight.getColor().multiply3(pointLight.getIntensity());

		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(new Color(0xddeeffff));
		ambientLight.setIntensity(0.1);
		//lightManager.addLight(ambientLight);
		
		
		
		//Point Cloud
		cloud = ResourceManager.create("hex_stand_2.obj");
		
		if(cloud != null) {
			//cloud.getTransform().scale(30.0);//ia
			//cloud.getTransform().scale(2.0);//tris
			//cloud.getTransform().scale(0.2);//abs
			cloud.getTransform().scale(0.6);//abs

			cloud.getTransform().translate(0, 0, -4.2);
			cloud.getTransform().rotateY(0.45);
			cloud.bake(null);

			//Place the cloud on the ground
			cloud.updateBoundingBox();
			BoundingBox bb = cloud.getBoundingBox();
			cloud.getTransform().translate(-1.0 * bb.getMidpoint().get(0), -1.0 * bb.min.get(1), 0);
			cloud.bake(null);
			
			
			Color flatColor = Color.gray(0.9);
			Matrix4 inv = cloud.getTransform();//.inverse();
/*
			//Texture3D tex = new SimplexNoiseTexture3D(new Color(0xe8e8e8ff), new Color(0xcc8844ff));
			Texture3D tex = new SimplexNoiseTexture3D(new Color(0xe8e8e8ff), (new Color(0xcc8844ff)).multiply3M(0.5));
			//SineWaveTexture3D sinTex = new SineWaveTexture3D(Color.gray(1), Color.gray(0));
			
			MatrixTransformTexture3D matTex = new MatrixTransformTexture3D(tex);
			matTex.getTransform().nonUniformScale(200, 50, 1);
			matTex.getTransform().rotateZ(0.3);
			*/
			
			//Set normal colors
			/*
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
				/*
				point.setMaterial(new ColorMaterial(diffuseLighting(
						pointLight.getPosition(), 
						inv.multiplyPt(point.getPosition()), 
						inv.multiply3(norm).normalizeM(), 
						lightColor, 
						flatColor)));
						*
			}*/

			//cloud.setMaterial(new DiffuseMaterial(Color.gray(0.9)));
			//cloud.setMaterial(new DiffusePTMaterial(Color.gray(0.9)));
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					(new Color(0xff0068ff)), 
					(new Color(0.99, 0.66, 0.2)), 
					0.6
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(0.09);
			gradientTrans.getTransform().translate(0, 0, 0);
			cloud.setMaterial(new FresnelDiffusePTMaterial(gradientTrans/*new Color(0.99, 0.66, 0.2)*/, 1.0, 2.0));
			//cloud.setMaterial(new DiffusePTMaterial(matTex));
			//cloud.setMaterial(new ColorMaterial(new Color(0xf8f8f8ff)));
			//cloud.setMaterial(new DielectricPTMaterial(new Color(3.0, 1.1, 1.0),  1.31, 0.05));
			
			this.addChild(cloud);
		}else{
			Logger.error(-13, "CSE167_2014_Project2: Cloud was null!");
		}
		
		/*
		ArrayList<Triangle> splines = new ArrayList<Triangle>();
	
		Spline s = new Spline();
		s.add(new Vector3(2.0, -1.0, -4.2));
		s.add(new Vector3(5.0, 1.0, -4.2));
		s.add(new Vector3(5.0, 3.0, -7.2));
		s.add(new Vector3(2.0, 5.0, -7.2));
		
		splines.addAll(s.tessellate(32, 16, 0.5, 0.0001));
		
		
		AABVHSurface splineAccel = AABVHSurface.makeAABVH(splines);
		
		
		Instance wrapper = new Instance();
		wrapper.addChild(splineAccel);
		wrapper.setMaterial(new DiffusePTMaterial(Color.gray(0.9)));
		this.addChild(wrapper);
		*/
		
		
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		
		//Add a plane to the scene
		Grid plane = new Grid(100, 100);
		//plane.setMaterial(new ReflectiveMaterial(new Color(30.0,30.0,30.0), 0.10));
		plane.setMaterial(new DiffusePTMaterial(Color.gray(1.0)));
		//plane.setMaterial(new DiffuseMaterial(Color.gray(0.9)));
		//plane.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.1), 0.9, 2.5));
		
		//Vector3 cloudMid = cloud.getBoundingBox().getMidpoint();
		//plane.getTransform().translate(cloudMid.get(0), 0, cloudMid.get(2));
		
		this.addChild(plane);
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
		
		//Vector3 position = activeCamera.getPosition();
		//position.set(Math.cos(elapsed * 1 + Math.PI/2) * 1.8, 2.85, Math.sin(elapsed * 1 + Math.PI/2) * 1.8);
		//activeCamera.setPosition(position);
		
		//activeCamera.setViewingDirection(position.multiply(-1.0));
		//activeCamera.getViewingDirection().set(1, -0.1);
		
		//activeCamera.getPosition().set(2, activeCamera.getPosition().get(2) + 1.2);
		

		cloud.getTransform().rotateY(data.getDt());
		cloud.bake(null);
		
		/*
		Color flatColor = Color.gray(0.9);
		Matrix4 inv = cloud.getTransform();
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
			
			
			((ColorMaterial)point.getMaterial()).setTexture(diffuseLighting(
					pointLight.getPosition(), 
					inv.multiplyPt(point.getPosition()), 
					inv.multiply3(norm).normalizeM(), 
					lightColor, 
					flatColor));
		}
		*/
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		this.updateBoundingBox();
		super.bake(data);
	}
}