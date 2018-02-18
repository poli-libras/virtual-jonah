package br.usp.libras.jonah.interpolation;

public class InterpolationTimer {

    private float pass;
    private float initialPass;
    
    private float time = 0;
    
    public InterpolationTimer(float pass) {
        this.initialPass = pass;
        this.pass = pass;
    }

    public void reset() {
        time = 0;
        pass = initialPass;
    }
    
    public void speedUp() {
        pass = pass * 2;
    }

    public void slowDown() {
        pass = pass / 2;
    }

    public float increment() {
        if (hasNotEnded()) {
            time = time + pass;
        }
        return time;
    }
    
    public boolean hasEnded() {
        return time >= 1;
    }

    public boolean hasNotEnded() {
        return !hasEnded();
    }
    
    public float getTime() {
        return time;
    }


}
