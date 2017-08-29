package br.usp.libras.sanbox;

import processing.core.PApplet;

public class CircularMovementIn2D extends PApplet {

    private static final long serialVersionUID = 1L;

    private float alpha = 0.0f;

    public void setup() {
        size(800, 500, P3D);
        perspective(PI / 4, 1.0f * width / height, 0.1f, 1500);
        frameRate(20);
        lights();
    }

    public void draw() {
        
        background(255, 238, 116);
        
        fill(255, 0, 0);
        float startX = 200, startY = 350;
        ellipse(startX, startY, 20, 20);
        fill(0, 255, 0);
        float endX = 400, endY = 150;
        ellipse(endX, endY, 20, 20);

        float centerX = Math.abs(endX + startX) / 2;
        float centerY = Math.abs(endY + startY) / 2;

        fill(0, 0, 255);
        ellipse(centerX, centerY, 20, 20);

        
        float raio = dist(startX, startY, endX, endY) / 2;
        float x = centerX + cos(alpha) * raio;
        float y = centerY + sin(alpha) * raio;
        translate(x, y, 0);
//        rotate(alpha);
        
        alpha += 0.2;
        if(alpha > TWO_PI) {
            alpha = 0.0f;
        }
        
        fill(0);
        stroke(255);
        box(50);

    }


    public static void main(String[] args) {
        PApplet.main(new String[] { "br.usp.libras.sanbox.CircularMovementIn2D" });
    }

}
