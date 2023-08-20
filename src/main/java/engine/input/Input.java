package engine.input;

import engine.renderEngine.WindowManager;
import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback {

    private static final int KEYBOARD_SIZE = 65535;

    private static boolean[] keys = new boolean[KEYBOARD_SIZE];
    private static int[] keyStates = new int[KEYBOARD_SIZE];

    private static int NO_STATE = -1;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            WindowManager.closeWindow(); // We will detect this in the rendering loop
        if(key == -1) return;
        keys[key] = action != GLFW_RELEASE;
        keyStates[key] = action;
    }

    public static void resetKeyboard()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }

    public static boolean getKeyDown(int key) {
        return keys[key];
    }

    public static boolean keyPressed(int key)
    {
        return keyStates[key] == GLFW_PRESS;
    }

    public static boolean keyReleased(int key)
    {
        return keyStates[key] == GLFW_RELEASE;
    }
}
