package engine.shaders;

import engine.effects.Fog;
import engine.entities.Camera;
import engine.entities.Light;
import engine.renderEngine.renderers.MasterRenderer;
import engine.toolbox.MathTools;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class StaticShader extends ShaderProgram{

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/main/resources/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;

    private int location_gradient;
    private int location_density;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_gradient = super.getUniformLocation("gradient");
        location_density = super.getUniformLocation("density");

        location_lightPosition = new int[MasterRenderer.getMaxLights()];
        location_lightColor = new int[MasterRenderer.getMaxLights()];
        location_attenuation = new int[MasterRenderer.getMaxLights()];
        for(int i = 0; i < MasterRenderer.getMaxLights(); i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadFog(Fog fog) {
        super.loadFloat(location_gradient, fog.getGradient());
        super.loadFloat(location_density, fog.getDensity());
    }

    public void loadLights(List<Light> lights) {
        for(int i = 0; i < MasterRenderer.getMaxLights(); i++) {
            if(i < lights.size()) {
                super.loadVector3(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector3(location_lightColor[i], lights.get(i).getColor());
                super.loadVector3(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector3(location_lightPosition[i], new Vector3f(0,0,0));
                super.loadVector3(location_lightColor[i], new Vector3f(0,0,0));
                super.loadVector3(location_attenuation[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, reflectivity);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadFakeLightingVariable(boolean useFake) {
        super.loadBoolean(location_useFakeLighting, useFake);
    }

    public void loadSkyColor(Vector3f color) {
        super.loadVector3(location_skyColor, color);
    }
}
