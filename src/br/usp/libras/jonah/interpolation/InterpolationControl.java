package br.usp.libras.jonah.interpolation;

public class InterpolationControl {

    private float interpolationPass;
    private float initialInterpolationPass;
    
    private float interpolationTime = 1;
    
    public InterpolationControl(float interpolationPass) {
        this.initialInterpolationPass = interpolationPass;
    }

    public void reset() {
        interpolationTime = 0;
        interpolationPass = initialInterpolationPass;
    }
    
    public void speedUp() {
        interpolationPass = interpolationPass * 2;
    }

    public void slowDown() {
        interpolationPass = interpolationPass / 2;
    }

    public void increment() {
        if (hasNotEnded()) {
            interpolationTime = interpolationTime + interpolationPass;
        }
    }
    
    public boolean hasEnded() {
        return interpolationTime >= 1;
    }

    public boolean hasNotEnded() {
        return !hasEnded();
    }
    
    public float getInterpolationTime() {
        return interpolationTime;
    }


}
