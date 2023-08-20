package engine.effects;

import org.joml.Vector3f;

public class Fog {

    private float density;
    private float gradient;
    private Vector3f color;

    public Fog(float density, float gradient, Vector3f color) {
        this.density = density;
        this.gradient = gradient;
        this.color = color;
    }

    public float getDensity() {
        return density;
    }

    public float getGradient() {
        return gradient;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setGradient(float gradient) {
        this.gradient = gradient;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
