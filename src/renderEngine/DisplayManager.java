package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 1280;
	private static final int FPS_CAP = 120;
	
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);;
		/* Creates an OpenGL Context i.e. an openGL Machine
		 * Higher version: 3
		 * Lower version: 2
		 */
		
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			/* Creating display by taking into consideration the Current OpenGL Context/Machine State
			 * & PixelFormats which could be "VGA", "SVGA" etc based on the hardware & driver
			 */
			Display.setTitle("Our First Display");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		// This is the Area/Window/Region where OpenGL can draw
	}
	
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);		//For v-sync
		Display.update();			//For updating the display
	}
	
	public static void closeDisplay()
	{
		Display.destroy();
	}
	
}