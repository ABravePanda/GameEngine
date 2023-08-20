package engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosition extends GLFWCursorPosCallback {

    private static Vector2d currentMousePos = new Vector2d(0,0);
    private static Vector2f dMousePos = new Vector2f(0,0);
    private static Vector2d actualMousePos = new Vector2d(0,0);
    private static Vector2f oldMousePos = new Vector2f(0,0);

    @Override
    public void invoke(long window, double xOffset, double yOffset) {
        currentMousePos.x = xOffset;
        currentMousePos.y = yOffset;
        actualMousePos.x = xOffset;
        actualMousePos.y = yOffset;
    }

    public static void input() {
        dMousePos.x = (float) currentMousePos.x;
        dMousePos.y = (float) currentMousePos.y;

        currentMousePos.x = 0;
        currentMousePos.y = 0;
    }

    public static Vector2d getCurrentMousePos() {
        return actualMousePos;
    }

    public static Vector2f getMouseDeltaPosition() {
        return dMousePos;
    }

    public static float getMouseDeltaY() {
        return dMousePos.y;
    }

    public static float getMouseDeltaX() {
        return dMousePos.x;
    }

    public static float getMousePositionX() {
        return (float) actualMousePos.x;
    }

    public static float getMousePositionY() {
        return (float) actualMousePos.y;
    }
}
