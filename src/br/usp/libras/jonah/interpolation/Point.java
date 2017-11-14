package br.usp.libras.jonah.interpolation;

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
    
    public Point mais(Point p) {
        return point(x + p.x, y + p.y, z + p.z);
    }
    
    public Point comZ(float novoZ) {
        return point(x, y, novoZ);
    }

    public Point comX(float novoX) {
        return point(novoX, y, z);
    }

    public Point comY(float novoY) {
        return point(x, novoY, z);
    }
    
    public Point pontoMedioIndoPara(Point p) {
        return point((x + p.x) / 2, (y + p.y) / 2, (z + p.z) / 2);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
}
