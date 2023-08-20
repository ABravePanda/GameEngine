package engine.entities;

import engine.components.Transform;
import engine.input.Input;
import engine.input.Mouse;
import engine.input.MouseInput;
import engine.input.ScrollInput;
import engine.renderEngine.WindowManager;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Transform transform;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 30;

    private float moveSpeed = 0.2f;
    private float runSpeed = 1f;
    private float speed;

    private final float CAMERA_ROTATE_SPEED = 3f;

    private Player player;

    //Pitch is X, Yaw is Y

    public Camera(Player player) {
        speed = moveSpeed;
        this.transform = new Transform();
        this.player = player;
    }

    public void move() {
        calculateAngleAroundPlayer();
        calculatePitch();
        calculateZoom();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        transform.setRotationY(180 - (player.getTransform().getRotationY() + angleAroundPlayer));
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(transform.getRotationX())));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(transform.getRotationX())));
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getTransform().getRotationY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        transform.setPositionX(player.getTransform().getPositionX() - offsetX);
        transform.setPositionZ(player.getTransform().getPositionZ() - offsetZ);
        transform.setPositionY(player.getTransform().getPositionY() + verticalDistance);
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getScrollDeltaY() * 30 * WindowManager.getDeltaTime();
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if(MouseInput.getMouseButtonDown(1)) {
            float pitchChange = (Mouse.getMouseDeltaY() * 20) * WindowManager.getDeltaTime();
            transform.setRotationX(transform.getRotationX() + pitchChange);
        }


        if(Input.getKeyDown(GLFW_KEY_PERIOD)) {
            transform.setRotationX(transform.getRotationX() + 10);
        }
        if(Input.getKeyDown(GLFW_KEY_COMMA)) {
            transform.setRotationX(transform.getRotationX() - 10);
        }
    }

    private void calculateAngleAroundPlayer() {
//        if(Mouse.getMouseButtonDown(0)) {
//            float angleChange = Mouse.getMouseDeltaX() * 10f * WindowManager.getDeltaTime();
//            angleAroundPlayer -= angleChange;
//        }

        if(Input.getKeyDown(GLFW_KEY_Q)) {
            angleAroundPlayer -= (CAMERA_ROTATE_SPEED * WindowManager.getDeltaTime()) * 100f;
        }
        if(Input.getKeyDown(GLFW_KEY_E)) {
            angleAroundPlayer += (CAMERA_ROTATE_SPEED * WindowManager.getDeltaTime()) * 100f;
        }

    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
