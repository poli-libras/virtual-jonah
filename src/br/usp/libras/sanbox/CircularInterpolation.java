package br.usp.libras.sanbox;

import br.usp.libras.sign.transition.Path;
import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.atan;
import static processing.core.PApplet.acos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.map;

import static br.usp.libras.sanbox.Point.point;
import static processing.core.PConstants.*;


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
        if(alpha > TWO_PI) {
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
        
        Point center = point((end.x + start.x) / 2, (end.y + start.y) / 2, (end.z + start.z) / 2);
        float raio = PApplet.dist(start.x, start.y, start.z, end.x, end.y, end.z) / 2;
        float x = 0, y = 0, z = 0;

        if (path.planoXY()) {
            
//            float angulo = atan(start.x / start.y) + alpha;
            float angulo = alpha;

            if (start.x < end.x) {
                angulo = angulo + PI;
            }
            // TODO continue...
//            if (start.x < end.x && start.y < end.y) {
//                angulo = angulo + PI/2;
//            }
//            if (start.x < end.x && start.y < end.y) {
//                angulo = angulo + PI;
//            }
//            if (start.x > end.x && start.y < end.y) {
//                angulo = angulo + 3*PI/2;
//            }

            x = center.x + cos(angulo) * raio;
            y = center.y + sin(angulo) * raio;
            if (alpha < PI) {
                z = map(alpha, 0, PI, start.z, end.z);
            } else {
                z = map(alpha, PI, TWO_PI, end.z, start.z);
            }
        }

        return point(x, y, z);
    }
}
