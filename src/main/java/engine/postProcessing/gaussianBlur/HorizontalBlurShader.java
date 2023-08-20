package engine.postProcessing.gaussianBlur;

import engine.shaders.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/resources/shaders/postProcessing/horizontalBlurVertex.txt";
	private static final String FRAGMENT_FILE = "src/main/resources/shaders/postProcessing/blurFragment.txt";
	
	private int location_targetWidth;
	
	protected HorizontalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void loadTargetWidth(float width){
		super.loadFloat(location_targetWidth, width);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
