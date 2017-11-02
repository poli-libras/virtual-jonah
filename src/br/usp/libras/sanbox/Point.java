package br.usp.libras.sanbox;

import processing.core.PApplet;

public class Point {

    public final float x, y, z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point point(float x, float y, float z) {
        return new Point(x, y, z);
    }
    
    public boolean proximoDe(Point p, float tolerancia) {
        return distanciaDe(p) < tolerancia;
    }
    
    public float distanciaDe(Point p) {
        return PApplet.dist(this.x, this.y, this.z, p.x, p.y, p.z);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
}
