package testingEngine;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entity.Camera;
import entity.Entity;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		//Creating Window using CreateDisplay() method of class DisplayManager
		
		Loader loader = new Loader();
		//Loader class instance
		
		StaticShader shader = new StaticShader();
		//Static Shader created here
		
		Renderer renderer = new Renderer(shader);
		//Renderer class instance
		
		float[] vertices = {
					-0.5f, 0.5f, 0,
					-0.5f, -0.5f, 0,
					0.5f, -0.5f, 0,
					0.5f, 0.5f, 0,
				};
		
		int[] indices = {
					0, 1, 3,
					3, 1, 2
				};
		
		
		float[] textureCoords = {
				0, 0,
				0, 1,
				1, 1,
				1, 0
				};
		
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		//Generating model using Loader.loadToVAO(float[], int[])
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("photo"));
		
		TexturedModel staticModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(staticModel, new Vector3f(-1, 0, 0), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested())
		{
			
			entity.increasePosition(0.002f, 0, 0);
			//entity.increaseRotation(0, 1, 0);
			
			camera.move();
			
			renderer.prepare();
			//Preparing renderer
			
			shader.start();
			//starting StaticShader
			
			shader.loadViewMatrix(camera);
			
			renderer.render(entity, shader);
			//Render the model:Model
			
			shader.stop();
			//Stop the StaticShader
			
			DisplayManager.updateDisplay();		
			//Clearing the display or updating it
			//may be SwapBuffer
			//or glFlush() 
			//or glFinish()
		}
		
		shader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
