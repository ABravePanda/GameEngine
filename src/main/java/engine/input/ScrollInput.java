package engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollInput extends GLFWScrollCallback {

    private static Vector2d currentMouseWheel = new Vector2d(0,0);
    private static Vector2f dMouseWheel = new Vector2f(0,0);

    @Override
    public void invoke(long window, double xOffset, double yOffset) {
        currentMouseWheel.x = xOffset;
        currentMouseWheel.y = yOffset;
    }

    public static void input() {
        dMouseWheel.x = (float) currentMouseWheel.x;
        dMouseWheel.y = (float) currentMouseWheel.y;

        currentMouseWheel.x = 0;
        currentMouseWheel.y = 0;
    }

    public static Vector2f getMouseDelta() {
        return dMouseWheel;
    }

    public static float getMouseY() {
        return dMouseWheel.y;
    }
}
