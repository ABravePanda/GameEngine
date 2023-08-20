package engine.postProcessing.effects;

import engine.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/resources/shaders/postProcessing/contrastVertex.txt";
	private static final String FRAGMENT_FILE = "src/main/resources/shaders/postProcessing/contrastFragment.txt";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
