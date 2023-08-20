package engine.postProcessing;

import engine.finals.Constants;
import engine.models.RawModel;
import engine.postProcessing.effects.ContrastChanger;
import engine.postProcessing.gaussianBlur.HorizontalBlur;
import engine.postProcessing.gaussianBlur.VerticalBlur;
import engine.renderEngine.Loader;
import engine.renderEngine.WindowManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(WindowManager.getCurrentWidth() / Constants.BLUR_FACTOR, WindowManager.getCurrentHeight() / Constants.BLUR_FACTOR);
		vBlur = new VerticalBlur(WindowManager.getCurrentWidth() / Constants.BLUR_FACTOR, WindowManager.getCurrentHeight() / Constants.BLUR_FACTOR);
		hBlur2 = new HorizontalBlur(WindowManager.getCurrentWidth() / 2, WindowManager.getCurrentHeight() / 2);
		vBlur2 = new VerticalBlur(WindowManager.getCurrentWidth() / 2, WindowManager.getCurrentHeight() / 2);
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
//		hBlur2.render(colourTexture);
//		vBlur2.render(hBlur2.getOutputTexture());
//		hBlur.render(vBlur2.getOutputTexture());
//		vBlur.render(hBlur.getOutputTexture());
//		contrastChanger.render(vBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		hBlur2.cleanUp();
		vBlur2.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
