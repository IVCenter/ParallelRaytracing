package tests;

import math.Spline;
import math.Vector3;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.ManhattanDistance3D;
import math.function._3D.TchebyshevDistance3D;
import process.logging.Logger;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.ProgrammableCameraController;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalSineWaveTexture3D;
import raytrace.map.texture._3D.WorleyNoiseTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.Material;
import raytrace.material.PassThroughMaterial;
import raytrace.material.SubSurfaceDiffusePTTestMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class TestScene12 extends Scene
{	

	/*
	 * A scene for sss material testing
	 */
	double elapsed = 0.0;
	double sslevel = 0.0;
	
	ProgrammableCameraController camController;
	
	Instance dragon;
	SubSurfaceDiffusePTTestMaterial ssmat;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Sky Material
		skyMaterial = new ColorMaterial(new Color(0.8, 0.9, 1.0));
		//skyMaterial = new ColorMaterial(new Color(0.1, 0.1, 0.1));
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(10);
		activeCamera.setPosition(new Vector3(-0.5, 0.25, -0.2));
		activeCamera.setViewingDirection(new Vector3(0.5, -0.1, 0.05));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.00005, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(0.50);	
		
		
		//Ground
		MeshSurface ground = new Cube(2.0, 0.11, 2.0);
		ground.setMaterial(new DiffusePTMaterial(new Color(0.3, 0.3, 0.35)));
		this.addChild(ground);
		

		//Dragon 4
		dragon = ResourceManager.create("dragon_smooth.obj");
		
		if(dragon != null)
		{
			sslevel = 0.0;
			ssmat = new SubSurfaceDiffusePTTestMaterial(//new DielectricMaterial(new Color(1.0, 1000.0, 10.0), 1.31)
					new DiffusePTMaterial(new Color(0.9, 0.9, 0.9)), 
					new ColorTexture3D(new Color(1000.0, 1000.0, 1.0)), 
					0.95, //scatter coeff 
					1.0, //refractive index
					1.0, //roughness
					2);
			
			dragon.getTransform().scale(0.18);//was 0.1
			dragon.getTransform().translate(0.0, 0.055, -0.2);//was -0.3 in z
			dragon.setDynamic(true);
			dragon.updateBoundingBox();
			dragon.bake(null);
			dragon.setDynamic(false);
			dragon.setMaterial(ssmat);
			this.addChild(dragon);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector3(2, -3, -2));
		lightManager.addLight(directionalLight);
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		long startTime = System.currentTimeMillis();
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(this.getChildren(), 1, 2);
		this.getChildren().clear();
		this.addChild(aabvh);
		
		//Refresh
		this.updateBoundingBox();
		
		Logger.progress(-1, "Ending AABVH creation... (" + (System.currentTimeMillis() - startTime) + "ms).");
	}
	
	int startFrame = 102;
	int frame = 0;
	
	@Override
	public void update(UpdateData data)
	{
		do{
			dragon.getTransform().rotateY(data.getDt() * Math.PI);
			dragon.setDynamic(true);
			dragon.updateBoundingBox();
			dragon.bake(null);
			dragon.setDynamic(false);
			
			ssmat.setScatterCoeff(sslevel);
			
			
			
			elapsed += data.getDt();
			
			sslevel += data.getDt() / 2.0;
			
			//Update the children
			super.update(data);
			
			frame++;
		
		} while (frame < startFrame);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		this.updateBoundingBox();
		super.bake(data);
	}
}
