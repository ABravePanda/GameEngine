package engine.shaders;

import engine.entities.Camera;
import engine.renderEngine.WindowManager;
import engine.toolbox.MathTools;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/resources/shaders/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/skybox/skyboxFragmentShader.txt";

    private static final float ROTATE_SPEED = 1f;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;

    private float rotation = 0;

	public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MathTools.createViewMatrix(camera);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        rotation += ROTATE_SPEED * WindowManager.getDeltaTime();
        matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0));
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector3(location_fogColor, new Vector3f(r,g,b));
    }

    public void loadFogColor(Vector3f color) {
        super.loadVector3(location_fogColor, color);
    }

    public void loadBlendFactor(float blend) {
        super.loadFloat(location_blendFactor, blend);
    }

    public void connectTextureUnits() {
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }


    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("blendFactor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}