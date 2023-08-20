package engine.input;

import engine.renderEngine.WindowManager;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Mouse {

    //Scroll
    public static Vector2f getScrollDelta() {
        return ScrollInput.getMouseDelta();
    }

    public static float getScrollDeltaY() {
        return ScrollInput.getMouseY();
    }

    //Mouse Position
    public static float getMouseDeltaY() {
        return WindowManager.getMouseDY();
    }

    public static float getMouseDeltaX() {
        return WindowManager.getMouseDX();
    }

    public static float getMousePositionX() {
        return WindowManager.getMouseX();
    }

    public static float getMousePositionY() {
        return WindowManager.getMouseY();
    }

    //Mouse Input
    public static boolean getMouseButtonDown(int button) {
        return MouseInput.getMouseButtonDown(button);
    }

    public static boolean mousePressed(int button)
    {
        return MouseInput.mousePressed(button);
    }

    public static boolean mouseReleased(int button)
    {
        return MouseInput.mouseReleased(button);
    }

}
