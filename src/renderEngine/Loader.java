package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices)
	{
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		//This calls the method which in actually uses a 
		//# vboID # glGenBuffers() # glBindBuffers(type, data) # glBufferData(type, data, GL_STATIC_DRAW)
		//uses type = GL_ELEMENT_ARRAY_BUFFER
		storeDataInAttributeList(0, 3, positions);
		//This calls the method with (attributeListPos, how many points in vertex, data)
		storeDataInAttributeList(1, 2, textureCoords);
		
		unbindVAO();
		return (new RawModel(vaoID, indices.length ) );
		//Raw Model is nothing else but keeps the Record of the vaoID and the Length Of Indices Binded to it)
	}
	
	
	public int loadTexture(String filename)
	{
		Texture texture = null;
		try {
			//Texture class provided by Slick Library
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + filename + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attribNumber, int coordinateSize, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	
	
	
	/* --------------------------------
	 *    Unbinding VAO And CleanUp
	   ---------------------------------
	 */
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	public void cleanUP()
	{
		for(int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}

}
