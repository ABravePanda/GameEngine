package engine.renderEngine.renderers;

import engine.effects.Fog;
import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.models.TexturedModel;
import engine.normalMappingRenderer.NormalMappingRenderer;
import engine.renderEngine.Loader;
import engine.renderEngine.WindowManager;
import engine.shaders.StaticShader;
import engine.shaders.TerrainShader;
import engine.terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    public static final Vector3f SKY_COLOR = new Vector3f(0.82f, 0.82f, 0.82f);
    private static final int MAX_LIGHTS = 4;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private NormalMappingRenderer normalMapRenderer;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<>();

    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Fog fog) {
        for(Terrain terrain : terrains) {
            processTerrain(terrain);
        }
        for(Entity entity : entities) {
            processEntity(entity);
        }

        render(lights, camera, fog);
    }

    public void render(List<Light> lights, Camera camera, Fog fog) {
        prepare();

        shader.start();
        shader.loadSkyColor(fog.getColor());
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        shader.loadFog(fog);
        renderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(fog.getColor());
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadFog(fog);
        terrainShader.loadShineVariables(1f,1);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        skyboxRenderer.render(camera, fog.getColor());

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        if(batch != null)
            batch.add(entity);
        else
        {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        terrainShader.cleanUp();
        shader.cleanUp();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(SKY_COLOR.x,SKY_COLOR.y,SKY_COLOR.z,1f);
        //135,206,235
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) WindowManager.getCurrentWidth() / (float) WindowManager.getCurrentHeight();
        projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(FOV, aspectRatio, NEAR_PLANE, FAR_PLANE);
    }

    public static int getMaxLights() {
        return MAX_LIGHTS;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
