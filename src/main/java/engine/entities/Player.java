package engine.entities;

import engine.finals.Constants;
import engine.input.Input;
import engine.models.TexturedModel;
import engine.renderEngine.WindowManager;
import engine.terrain.Terrain;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity{

    private static final float WALK_SPEED = 20;
    private static final float RUN_MODIFIER = 3;
    private static final float TURN_SPEED = 100;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;


    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    private Terrain activeTerrain;

    public Terrain getActiveTerrain() {
        return activeTerrain;
    }

    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        this.activeTerrain = null;
    }

    public void move(List<Terrain> terrains) {
        getTerrainUnderPlayer(terrains);
        checkInputs();
        getTransform().increaseRotation(0, currentTurnSpeed * WindowManager.getDeltaTime(), 0);
        float distance = currentSpeed * WindowManager.getDeltaTime();
        float dx = (float) (distance * Math.sin(Math.toRadians(getTransform().getRotationY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getTransform().getRotationY())));
        getTransform().increasePosition(dx, 0, dz);

        upwardsSpeed += GRAVITY * WindowManager.getDeltaTime();
        getTransform().increasePosition(0, upwardsSpeed * WindowManager.getDeltaTime(), 0);

        float terrainHeight = activeTerrain.getHeightOfTerrain(getTransform().getPositionX(), getTransform().getPositionZ());
        if(getTransform().getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            getTransform().getPosition().y = terrainHeight;
        }
    }

    private void jump() {
        if(!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {

        if(Input.getKeyDown(GLFW_KEY_W)) {
            this.currentSpeed = WALK_SPEED;
        } else if(Input.getKeyDown(GLFW_KEY_S)) {
            this.currentSpeed = -WALK_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if(Input.getKeyDown(GLFW_KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else if(Input.getKeyDown(GLFW_KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if(Input.getKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            currentSpeed = WALK_SPEED * RUN_MODIFIER;
        }

        if(Input.getKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
    }

    private void getTerrainUnderPlayer(List<Terrain> terrains) {
        Vector3f playerPosition = getTransform().getPosition();
        int xGrid = (int) Math.floor((playerPosition.x / Constants.TERRAIN_SIZE));
        int zGrid = (int) Math.floor((playerPosition.z / Constants.TERRAIN_SIZE));

        for(Terrain terrain : terrains) {
            if(terrain.getGridX() == xGrid && terrain.getGridZ() == zGrid) {
                activeTerrain = terrain;
                return;
            }
        }


        activeTerrain = terrains.get(0);
    }
}
