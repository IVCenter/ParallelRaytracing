package raytrace.scene;

import java.util.HashMap;

import system.Constants;
import tests.CSE168_Project1_Scene;
import tests.CSE168_Project2_Scene;
import tests.TestScene1;
import tests.TestScene2;
import tests.TestScene3;
import tests.TestScene4;
import tests.TestScene5;

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
