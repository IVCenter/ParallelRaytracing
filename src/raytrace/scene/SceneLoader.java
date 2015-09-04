package raytrace.scene;

import java.util.HashMap;

public class SceneLoader {
	
	/*
	 * A scene to be rendered
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected static HashMap<String, SceneConstructor> sceneConstructors;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	static
	{
		sceneConstructors = new HashMap<String, SceneConstructor>();
		
		sceneConstructors.put("", new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new EmptyScene();
				scene.setSceneKey("");
				return scene;
			}
		});
		
		//NOTE: These Scene implementations have been moved to: https://github.com/rexwest/ParallelRaytracing-Demos
		/*
		sceneConstructors.put(Constants.SceneKeys.TEST1, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene1();
				scene.setSceneKey(Constants.SceneKeys.TEST1);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.CSE168_Project1, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new CSE168_Project1_Scene();
				scene.setSceneKey(Constants.SceneKeys.CSE168_Project1);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST2, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene2();
				scene.setSceneKey(Constants.SceneKeys.TEST2);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST3, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene3();
				scene.setSceneKey(Constants.SceneKeys.TEST3);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST4, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene4();
				scene.setSceneKey(Constants.SceneKeys.TEST4);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.CSE168_Project2, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new CSE168_Project2_Scene();
				scene.setSceneKey(Constants.SceneKeys.CSE168_Project2);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST5, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene5();
				scene.setSceneKey(Constants.SceneKeys.TEST5);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.CSE168_Project3, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new CSE168_Project3_Scene();
				scene.setSceneKey(Constants.SceneKeys.CSE168_Project3);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.Performance_TEST1, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new PerformanceTest1();
				scene.setSceneKey(Constants.SceneKeys.Performance_TEST1);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST6, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene6();
				scene.setSceneKey(Constants.SceneKeys.TEST6);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST7, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene7();
				scene.setSceneKey(Constants.SceneKeys.TEST7);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST8, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene8();
				scene.setSceneKey(Constants.SceneKeys.TEST8);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST9, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene9();
				scene.setSceneKey(Constants.SceneKeys.TEST9);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST10, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene10();
				scene.setSceneKey(Constants.SceneKeys.TEST10);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST11, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene11();
				scene.setSceneKey(Constants.SceneKeys.TEST11);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST12, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene12();
				scene.setSceneKey(Constants.SceneKeys.TEST12);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST13, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new TestScene13();
				scene.setSceneKey(Constants.SceneKeys.TEST13);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.Diffuse_Glass_TEST, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new DiffuseGlassTestScene();
				scene.setSceneKey(Constants.SceneKeys.Diffuse_Glass_TEST);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.CSE167_2014_Project2, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new CSE167_2014_Project2();
				scene.setSceneKey(Constants.SceneKeys.CSE167_2014_Project2);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.VoxelTest1, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new VoxelTest1();
				scene.setSceneKey(Constants.SceneKeys.VoxelTest1);
				return scene;
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.CSE165_2015_DataSmoothingDiagrams, new SceneConstructor(){
			@Override
			public Scene load() {
				Scene scene = new CSE165_2015_DataSmoothingDiagrams();
				scene.setSceneKey(Constants.SceneKeys.CSE165_2015_DataSmoothingDiagrams);
				return scene;
			}
		});
		*/
	}
	
	public static Scene load(String key)
	{
		//TODO: Add support for a "scene folder" configuration option
		//TODO: Add support for reading and compiling a Scene java file from the "scene folder"
		
		//NOTE: For now loading a new scene after the engine has been started is unsupported
		//To launch a scene directly, create a subclass of Scene and implement a main that 
		//	sets up Configuration and hands off to ApplicationDelegate
		SceneConstructor constructor = sceneConstructors.get(key);
		Scene scene = null;
		
		if(constructor != null)
			scene = constructor.load();
		else
			scene = sceneConstructors.get("").load();
		
		return scene;
	}
	
	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private static abstract class SceneConstructor
	{
		public abstract Scene load();
	}

}
