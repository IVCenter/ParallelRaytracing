package raytrace.scene;

import java.util.HashMap;

import system.Constants;
import tests.CSE168_Project1_Scene;
import tests.CSE168_Project2_Scene;
import tests.CSE168_Project3_Scene;
import tests.PerformanceTest1;
import tests.TestScene1;
import tests.TestScene10;
import tests.TestScene11;
import tests.TestScene12;
import tests.TestScene13;
import tests.TestScene2;
import tests.TestScene3;
import tests.TestScene4;
import tests.TestScene5;
import tests.TestScene6;
import tests.TestScene7;
import tests.TestScene8;
import tests.TestScene9;

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
	}
	
	public static Scene load(String key)
	{
		return sceneConstructors.get(key).load();
	}
	
	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private static abstract class SceneConstructor
	{
		public abstract Scene load();
	}

}
