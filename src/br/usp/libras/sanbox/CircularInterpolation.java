package br.usp.libras.sanbox;

import br.usp.libras.sign.transition.Path;
import processing.core.PApplet;

public class CircularInterpolation {

    private static final float ALPHA_STEP = 0.08f;
    
    private Point start;
    private Point end;
    private Path path;

    private float alpha = 0.0f;
    
    public CircularInterpolation(Point start, Point end, Path path) {
        this.start = start;
        this.end = end;
        this.path = path;
    }

    public Point next() {
        Point nextPoint = interpolate(alpha);
        alpha += ALPHA_STEP;
        if(alpha > PApplet.TWO_PI) {
            alpha = 0;
        }
        return nextPoint;
    }
    
    /**
     * 
     * @param alpha \in [0, 2_PI]
     * @return
     */
    public Point interpolate(float alpha) {
        return null;
    }
}
