package engine.toolbox;

import engine.components.Transform;
import engine.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathTools {

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity()
                .translate(translation)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector3f rotation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity()
                .translate(new Vector3f(translation.x, translation.y, 0))
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Transform transform = camera.getTransform();
        Vector3f cameraPos = transform.getPosition();
        Vector3f rotation = transform.getRotation();

        Matrix4f viewMatrix = new Matrix4f();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
