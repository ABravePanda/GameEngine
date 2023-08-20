package engine.engineTester;

import engine.components.Cache;
import engine.components.GameObject;
import engine.effects.Fog;
import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.Player;
import engine.input.Input;
import engine.input.Mouse;
import engine.models.TexturedModel;
import engine.objConverter.ModelData;
import engine.objConverter.OBJFileLoader;
import engine.postProcessing.Fbo;
import engine.postProcessing.PostProcessing;
import engine.renderEngine.*;
import engine.models.RawModel;
import engine.renderEngine.renderers.GUIRenderer;
import engine.renderEngine.renderers.MasterRenderer;
import engine.terrain.Terrain;
import engine.terrain.TerrainManager;
import engine.textures.ModelTexture;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import engine.toolbox.MousePicker;
import engine.ui.GUITexture;
import engine.ui.text.FontType;
import engine.ui.text.font.TextMaster;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {

    public static void main(String[] args) {

        initGame();

        long window = WindowManager.createWindow();
        Loader loader = new Loader();
        TextMaster.init(loader);

        FontType font = new FontType(loader.loadFontTexture("arial"), new File("src/main/resources/fonts/arial.fnt"));
       // GUIText text = new GUIText("1234567890", 4, font, new Vector2f(0,0), 1f, true);


        /*
                Terrain
         */

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass_terrain"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud_terrain"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass_flowers_terrain"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("tile_terrain"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0,0, loader, texturePack, blendMap);

        TerrainManager terrainManager = new TerrainManager(loader, texturePack, blendMap);
        terrainManager.addTerrain(terrain);

        /*
            Lighting
         */

        List<Light> lights = new ArrayList<>();
        Light sun = new Light(new Vector3f(20000,20000,20000), new Vector3f(1,1,1));
        //Light blue = new Light(new Vector3f(20000,40000,20000), new Vector3f(1,0,0));
//
       float x = 200;
        float z = -200;
        float y = terrain.getHeightOfTerrain(x,z) + 3f;
        Light red = new Light(new Vector3f(x,y,z), new Vector3f(0,0,1), new Vector3f(0.3f, 0.001f, 0.002f));

        lights.add(sun);
        //lights.add(blue);
        lights.add(red);

        MasterRenderer renderer = new MasterRenderer(loader);


        /*
                Objects (Trees, etc)
         */

        List<Entity> entities = new ArrayList<>();

        /*
             Player
         */

        ModelData playerData = OBJFileLoader.loadOBJ("bunny");
        RawModel playerRawModel = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());
        TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadTexture("white")));
        ModelTexture texture = playerModel.getTexture();
        texture.setReflectivity(10f);
        texture.setShineDamper(1f);

        Player player = new Player(playerModel, new Vector3f(0,10,0), new Vector3f(0,0,0), 1);
        Camera camera = new Camera(player);
        entities.add(player);

        ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonRawModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
        TexturedModel dragonModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("white")));
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setReflectivity(1f);
        dragonTexture.setShineDamper(1f);
        Entity dragonEntity = new Entity(dragonModel, new Vector3f(0,0,0), new Vector3f(0,0,0), 1);
        entities.add(dragonEntity);


        /*
            GUI
         */

        GUIRenderer guiRenderer = new GUIRenderer(loader);
        List<GUITexture> guiList = new ArrayList<>();
//        GUITexture gui = new GUITexture(loader.loadTexture("gui"), new Vector2f(0.5f,0.5f), new Vector3f(0,0,180), new Vector2f(0.25f,0.25f));
//        guiList.add(gui);

        Fog fog = new Fog(0.001f, 0.6f, new Vector3f(0.8f,0.8f,0.8f));

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        Cache cache = new Cache(loader);
        cache.init();

        Fbo multisampleFBO = new Fbo(WindowManager.getCurrentWidth(), WindowManager.getCurrentHeight());
        Fbo outputFBO = new Fbo(WindowManager.getCurrentWidth(), WindowManager.getCurrentHeight(), Fbo.DEPTH_TEXTURE);
        PostProcessing.init(loader);

        /*
            Main Loop
         */

        while ( !glfwWindowShouldClose(window) ) {
            camera.move();
            player.move(terrainManager.getTerrains());
            terrainManager.update(player);
            picker.update(player.getActiveTerrain());

            //System.out.println("X : " + player.getTransform().getPositionX() + " | Y: " + player.getTransform().getPositionY() + " | Z: " + player.getTransform().getPositionZ());

            if(picker.getCurrentTerrainPoint() != null) {
                dragonEntity.getTransform().setPosition(picker.getCurrentTerrainPoint());
                if(Mouse.mousePressed(0)) {
                    Entity newEntity = GameObject.instantiate(cache, "dragon", "white", dragonEntity.getTransform());
                    entities.add(newEntity);
                }
            }

            multisampleFBO.bindFrameBuffer();
            renderer.renderScene(entities, terrainManager.getTerrains(), lights, camera, fog);
            multisampleFBO.unbindFrameBuffer();
            multisampleFBO.resolveToFbo(outputFBO);
            PostProcessing.doPostProcessing(outputFBO.getColourTexture());

//            guiRenderer.render(guiList);
//            TextMaster.render();
            WindowManager.updateWindow();
        }

//        TextMaster.cleanUp();
//        guiRenderer.cleanUp();
        PostProcessing.cleanUp();
        outputFBO.cleanUp();
        multisampleFBO.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();

    }

    public static void initGame() {
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
    }

}
