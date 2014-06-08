package tests;

import process.logging.Logger;
import math.Vector4;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.ManhattanDistance3D;
import math.function._3D.TchebyshevDistance3D;
import raytrace.camera.ProgrammableCamera;
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
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.SimplexInterpolationT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.Material;
import raytrace.material.PassThroughMaterial;
import raytrace.material.SubSurfaceDiffusePTTestMaterial;
import raytrace.material.SubsurfaceScatterPTMaterial;
import raytrace.material.blend.binary.InterpolationBBlend;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.blend.binary.TextureMaskBBlend;
import raytrace.material.blend.unary.MultiplicativeUBlend;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class CSE168_Project3_Scene extends Scene
{	

	/*
	 * A scene for project 3 of CSE 168
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Sky Material
		skyMaterial = new ColorMaterial(new Color(0.8, 0.9, 1.0));
		//skyMaterial = new ColorMaterial(new Color(0.1, 0.1, 0.1));

		Configuration.setScreenWidth(800);
		Configuration.setScreenHeight(600);
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(6);
		activeCamera.setPosition(new Vector4(-0.5, 0.25, -0.2, 0));
		activeCamera.setViewingDirection(new Vector4(0.5, -0.1, 0.05, 0));
		//activeCamera.setPosition(new Vector4(-0.2, 0.077, 0.1, 0));
		//activeCamera.setViewingDirection(new Vector4(0.65, 0.3, -1.0, 0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.00005, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(0.50);
	
		
		//Ground
		MeshSurface ground = new Cube(2.0, 0.11, 2.0);
		ground.setMaterial(new DiffusePTMaterial(new Color(0.3, 0.3, 0.35)));
		this.addChild(ground);
		
		
		//Dragon 1
		Instance model = ResourceManager.create("dragon_smooth.obj");
		
		if(model != null)
		{
			//SimplexInterpolationT3DBlend sit3d = new SimplexInterpolationT3DBlend(
			//		new Color(100.4, 1.0, 0.95), new Color(100.3, 0.95, 100.3));
			
			SimplexNoiseTexture3D sntex1 = new SimplexNoiseTexture3D();
			sntex1.setFirstColor(new Color(0.0, 0.0, 0.0));
			sntex1.setSecondColor(new Color(1.0, 1.0, 1.0));
			
			MatrixTransformTexture3D mtrans1 = new MatrixTransformTexture3D(sntex1);
			mtrans1.getTransform().nonUniformScale(2, 64, 2);
			mtrans1.getTransform().rotateZ(-0.1);
			

			SimplexNoiseTexture3D sntex2 = new SimplexNoiseTexture3D();
			sntex2.setFirstColor(new Color(0.0, 0.0, 0.0));
			sntex2.setSecondColor(new Color(1.0, 1.0, 1.0));
			
			MatrixTransformTexture3D mtrans2 = new MatrixTransformTexture3D(sntex2);
			mtrans2.getTransform().nonUniformScale(5, 60, 10);
			mtrans2.getTransform().rotateZ(0.4);
			
			
			//MultiplicativeT3DBlend multiBlend = new MultiplicativeT3DBlend();
			//multiBlend.setFirstTexture(mtrans1);
			//multiBlend.setSecondTexture(mtrans2);

			AdditiveT3DBlend addBlend = new AdditiveT3DBlend();
			addBlend.setFirstTexture(mtrans1);
			addBlend.setSecondTexture(mtrans2);
			
			
			
			
			//MatrixTransformTexture3D mtex = new MatrixTransformTexture3D(sit3d);
			//mtex.getTransform().nonUniformScale(32, 64, 32);
			//mtex.getTransform().rotateZ(-0.05);
			
			model.getTransform().scale(0.1);
			model.getTransform().translate(0.0, 0.055, 0.0);
			model.bake(null);
			model.setMaterial(new AshikhminPTMaterial(Color.gray(0.7), Color.black(), 0.0,
					1.0, 0, 0));
			/*
			model.setMaterial(
					new TextureMaskBBlend(
							//new DielectricPTMaterial(new Color(1.3, 1.3, 0.92), 3.01),
							new DielectricPTMaterial(new Color(10000000000.2, 10000000000.95, 10000000000.2), 1.31),
							//new DiffusePTMaterial(new Color(0.35, 0.35, 0.35)),
							//new DiffusePTMaterial(new Color(0.9, 0.9, 0.9)),
							new AshikhminPTMaterial(Color.gray(0.0), new Color(0.95, 0.7, 0.3), 1.0, 0.0, 1, 1000),
							addBlend
						)
						);
						*/
			//this.addChild(model);
		}
		
		
		//Dragon 2
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{

			WorleyNoiseTexture3D worleyTex1 = new WorleyNoiseTexture3D();
			worleyTex1.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
			worleyTex1.getNoiseFunction().setSelectionFunction(new SelectDifferenceNthMthNearest2D(7,1));
			
			MatrixTransformTexture3D mtrans3 = new MatrixTransformTexture3D(worleyTex1);
			mtrans3.getTransform().scale(32);
			
			WorleyNoiseTexture3D worleyTex2 = new WorleyNoiseTexture3D();
			worleyTex2.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
			worleyTex2.getNoiseFunction().setSelectionFunction(new SelectNthNearest2D(3));
			worleyTex2.setSecondColor(new Color(1.1, 1.1, 1.1));
			
			MatrixTransformTexture3D mtrans4 = new MatrixTransformTexture3D(worleyTex2);
			mtrans4.getTransform().scale(8);
			
			SubtractiveT3DBlend subBlend = new SubtractiveT3DBlend(mtrans4, mtrans3);
			
			//MatrixTransformTexture3D subBlendTrans = new MatrixTransformTexture3D(subBlend);
			//subBlendTrans.getTransform().scale(2.0);
			
			
			model2.getTransform().scale(0.1);
			model2.getTransform().translate(0.0, 0.055, -0.1);
			model2.bake(null);
			model2.setMaterial(new AshikhminPTMaterial(Color.gray(0.0), new Color(0.9, 0.6, 0.5), 1.0,
					0.0, 100, 100));
			/*
			model2.setMaterial(
					new PosterMaskBBlend(
							new DiffusePTMaterial(new OneMinusT3DBlend(new MultiplicativeT3DBlend(subBlend, subBlend))),
							new AshikhminPTMaterial(Color.gray(0.0), new Color(0.95, 0.7, 0.3), 1.0, 0.0, 1, 1000),
							subBlend,
							0.60
						)
					);
					*/
			//this.addChild(model2);
		}
		

		//Dragon 3
		Instance model3 = ResourceManager.create("dragon_smooth.obj");
		
		if(model3 != null)
		{
			WorleyNoiseTexture3D worleyTex1 = new WorleyNoiseTexture3D();
			worleyTex1.getNoiseFunction().setDistanceFunction(new ManhattanDistance3D());
			worleyTex1.getNoiseFunction().setSelectionFunction(new SelectDifferenceNthMthNearest2D(6,1));
			
			MatrixTransformTexture3D mtrans3 = new MatrixTransformTexture3D(worleyTex1);
			mtrans3.getTransform().scale(16);
			
			WorleyNoiseTexture3D worleyTex2 = new WorleyNoiseTexture3D();
			worleyTex2.getNoiseFunction().setDistanceFunction(new ManhattanDistance3D());
			worleyTex2.getNoiseFunction().setSelectionFunction(new SelectNthNearest2D(3));
			worleyTex2.setSecondColor(new Color(1.1, 1.1, 1.1));
			
			MatrixTransformTexture3D mtrans4 = new MatrixTransformTexture3D(worleyTex2);
			mtrans4.getTransform().scale(8);
			
			SubtractiveT3DBlend subBlend = new SubtractiveT3DBlend(mtrans4, mtrans3);
			
			SphericalSineWaveTexture3D sineTex = new SphericalSineWaveTexture3D();
			MatrixTransformTexture3D sineTrans = new MatrixTransformTexture3D(sineTex);
			sineTrans.getTransform().scale(128);
			sineTrans.getTransform().translate(0, 3, 1);
			
			model3.getTransform().scale(0.1);
			model3.getTransform().translate(0.0, 0.055, -0.2);
			model3.bake(null);
			model3.setMaterial(new AshikhminPTMaterial(Color.gray(0.0), new Color(0.95, 0.7, 0.3), 1.0,
					0.0, 1, 1000));
			/*
			model3.setMaterial(
					new PosterMaskBBlend(
							//new DielectricPTMaterial(new Color(1000000000000000.0, 10000000.9, 1000000000000000.0), 1.31),
							new MultiplicativeUBlend(
									new DielectricPTMaterial(new Color(100000000.0, 0.80 * 100000.0, 0.80 * 10000.0), 0.95), 
									new Color(0.5, 0.5, 1.0)
								),
							//new PassThroughMaterial(new Color(0.7, 1.05, 1.05), 1.31),
							new PassThroughMaterial(Color.white(), 1.31),
							sineTrans,
							0.1
						)
					);
					*/
			//this.addChild(model3);
		}
		

		//Dragon 4
		Instance model4 = ResourceManager.create("dragon_smooth.obj");
		//Instance model4 = ResourceManager.create("bunny_n.obj");
		
		if(model4 != null)
		{
			Color color = new Color(0xccccccff);
			//DiffusePTMaterial dmat = new DiffusePTMaterial(color);
			//PassThroughMaterial pmat = new PassThroughMaterial(Color.gray(1.0));
			//InterpolationBBlend interblend = new InterpolationBBlend(pmat, dmat, 0.5);
			//FresnelDiffusePTMaterial mat = new FresnelDiffusePTMaterial(new Color(0xffccddff), 1.0, 0.4);
			//Material scatterMat = new SubsurfaceScatterPTMaterial(dmat, color, 1.01, 
			//		2.0, 1.0, 0.05);
			
			//Material dimat = new DielectricPTMaterial(new Color(1000000.0, 1000000.0, 10000.0),  1.31, 0.5);
			
			Material ssmat = new SubSurfaceDiffusePTTestMaterial(new DiffusePTMaterial(new Color(1.1, 1.1, 1.1)), 
					new ColorTexture3D(new Color(10.0, 10.0, 10.0)/*new Color(1.0 * 1.0, 1.3 * 100.0, 1.1 * 10.0)*/), 
					0.90, //scatter coeff 
					1.0, //refractive index
					1.0, //roughness
					10);
			
			
			model4.getTransform().scale(0.18);//was 0.1
			model4.getTransform().translate(0.0, 0.055, -0.2);//was -0.3 in z
			model4.bake(null);
			//model4.setMaterial(new AshikhminPTMaterial(new Color(1.0, 0.1, 0.1), new Color(1.0, 1.0, 1.0), 0.20,
			//		0.80, 1000, 1000));
			model4.setMaterial(ssmat);
			this.addChild(model4);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector4(2, -3, -2, 0));
		lightManager.addLight(directionalLight);
		
		//Yellow Light
		{
			PointLight plight = new PointLight();
			plight.setColor(new Color(0xffbb66ff));
			plight.setIntensity(8.0);
			plight.setPosition(new Vector4(0.1, 0.1, -0.4, 0));
			//lightManager.addLight(plight);
		}
		
		//Red Light
		{
			PointLight plight = new PointLight();
			plight.setColor(new Color(0xff0000ff));
			plight.setIntensity(4.0);
			plight.setPosition(new Vector4(0.0, 0.2, -0.3, 0));
			//lightManager.addLight(plight);
		}
		
		//Sphere Light
		{
			Sphere sphere = new Sphere(1.0, new Vector4(1.5, 1.5, 1.5, 1));
			sphere.setMaterial(new ColorMaterial(new Color(2.0, 2.0, 2.0)));
			//this.addChild(sphere);
		}
		
		
		
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
		this.updateBoundingBox();
		super.bake(data);
	}
}