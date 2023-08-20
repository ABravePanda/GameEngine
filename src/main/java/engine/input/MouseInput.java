package engine.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInput extends GLFWMouseButtonCallback {

    private static final int MOUSE_SIZE = 65535;

    private static boolean[] buttons = new boolean[MOUSE_SIZE];
    private static int[] buttonStates = new int[MOUSE_SIZE];

    private static int NO_STATE = -1;

    @Override
    public void invoke(long l, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;
        buttonStates[button] = action;
    }

    public static void resetMouse()
    {
        for (int i = 0; i < buttonStates.length; i++)
        {
            buttonStates[i] = NO_STATE;
        }
    }

    public static boolean getMouseButtonDown(int button) {
        return buttons[button];
    }

    public static boolean mousePressed(int button)
    {
        return buttonStates[button] == GLFW_PRESS;
    }

    public static boolean mouseReleased(int button)
    {
        return buttonStates[button] == GLFW_RELEASE;
    }
}
