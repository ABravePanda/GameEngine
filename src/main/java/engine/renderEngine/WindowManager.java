package engine.renderEngine;

import engine.input.Input;
import engine.input.MouseInput;
import engine.input.MousePosition;
import engine.input.ScrollInput;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class WindowManager {

    public static long window;

    private static Input keyCallback;
    private static ScrollInput scrollCallback;
    private static MouseInput mouseCallback;
    private static GLFWCursorPosCallback mousePositionCallback;

    private static final int WIDTH = 2560;
    private static final int HEIGHT = 1440;

    private static long lastFrameTime;
    private static float delta;

    private static int currentWidth = 0;
    private static int currentHeight = 0;

    private static int mouseX, mouseY;
    private static int mouseDX, mouseDY;


    public static long createWindow() {

        currentWidth = WIDTH;
        currentHeight = HEIGHT;
        lastFrameTime = getCurrentTime();
        mouseX = mouseDX = mouseY = mouseDY = 0;

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_SAMPLES, 8);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);

        glfwSetKeyCallback(window, keyCallback = new Input());
        glfwSetScrollCallback(window, scrollCallback = new ScrollInput());
        glfwSetMouseButtonCallback(window, mouseCallback = new MouseInput());
        glfwSetCursorPosCallback(window, mousePositionCallback = new GLFWCursorPosCallback(){

            @Override
            public void invoke(long window, double xpos, double ypos) {
                // Add delta of x and y mouse coordinates
                mouseDX += xpos - mouseX;
                mouseDY += ypos - mouseY;
                // Set new positions of x and y
                mouseX = (int) xpos;
                mouseY = (int) ypos;
            }
        });

        glfwSetWindowSizeCallback(window, (window, w, h) -> {
            currentWidth = w;
            currentHeight = h;
            glViewport(0, 0, w, h);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();

        return window;
    }

    public static void updateWindow() {
        Input.resetKeyboard();
        MouseInput.resetMouse();
        ScrollInput.input();
        MousePosition.input();

        GL.createCapabilities();
        glfwSwapBuffers(window);
        glfwPollEvents();

        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;

    }

    public static void closeWindow() {
        glfwSetWindowShouldClose(window, true);
    }


    public static int getCurrentWidth() {
        return currentWidth;
    }

    public static int getCurrentHeight() {
        return currentHeight;
    }

    private static long getCurrentTime() {
        return (long) (glfwGetTime()*1000);
    }

    public static float getDeltaTime() {
        return delta;
    }

    public static int getMouseDX() {
        return mouseDX | (mouseDX = 0);
    }

    public static int getMouseDY() {
        return mouseDY | (mouseDY = 0);
    }

    public static float getMouseX() {
        return mouseX;
    }

    public static float getMouseY() {
        return mouseY;
    }
}
