package br.usp.libras.sanbox;

import processing.core.PApplet;
import processing.core.PFont;

public class CircularMovementIn3D extends PApplet {

    private static final long serialVersionUID = 1L;

    private float alpha = 0.0f;

    private float cameraRotX = 0;
    private float cameraRotY = 0;
    private float cameraRotZ = 0;

    @Override
    public void setup() {
        size(800, 500, P3D);
        perspective(PI / 4, 1.0f * width / height, 0.1f, 1500);
        frameRate(20);
        lights();
    }

    @Override
    public void draw() {
        
        background(255, 238, 116);
        translate(width/2, height/2);
        rotateCamera();

        drawAxes();
        
        fill(255, 0, 0);
        float startX = -180, startY = 100, startZ = 50;
        pushMatrix();
        translate(startX, startY, startZ);
        sphere(5);
        popMatrix();

        fill(0, 255, 0);
        float endX = 200, endY = -80, endZ = -50;
        pushMatrix();
        translate(endX, endY, endZ);
        sphere(5);
        popMatrix();

        fill(0, 0, 255);
        float centerX = (endX + startX) / 2;
        float centerY = (endY + startY) / 2;
        float centerZ = (endZ + startZ) / 2;
        pushMatrix();
        translate(centerX, centerY, centerZ);
        sphere(5);
        popMatrix();

        
        float raio = dist(startX, startY, startZ, endX, endY, endZ) / 2;
        float x = centerX + cos(alpha) * raio;
        float y = centerY + sin(alpha) * raio;
        // (x-cx)^2 + (y-cy)^2 + (z-cz^2) = R^2
        double radicando = Math.pow(Math.round(raio), 2) - Math.pow(Math.round(x) - Math.round(centerX), 2) - Math.pow(Math.round(y) - Math.round(centerY), 2);
        System.out.println(x + " " + centerX + " " + y + " " + centerY + " " + raio + " " + radicando);
        double zDouble = Math.sqrt(radicando) + centerZ;
        float z = (float) zDouble;
//        System.out.println(x + " " + centerX + " " + y + " " + centerY + " " + raio + " " + zDouble);
        translate(x, y, z);
        
        alpha += 0.1;
        if(alpha > TWO_PI) {
            alpha = 0.0f;
        }
        
        fill(0);
        stroke(255);
        box(20);

    }
    
    private void drawAxes() {
         beginShape(LINES);
         stroke(255, 0, 0); // vermelho - 0x
         vertex(0, 0, 0);
         vertex(100, 0, 0);
         stroke(0, 255, 0); // verde - 0y
         vertex(0, 0, 0);
         vertex(0, 100, 0);
         stroke(0, 0, 255); // azul - 0z
         vertex(0, 0, 0);
         vertex(0, 0, 100);
         endShape(LINES);
         stroke(0);
    }

    private void rotateCamera() {
        rotateX(cameraRotX);
        rotateY(cameraRotY);
        rotateZ(cameraRotZ);
    }
    
    @Override
    public void keyPressed() {

        // controle de câmera
        if (key == 'a')
            cameraRotY += -0.1;
        if (key == 'd')
            cameraRotY += 0.1;
        if (key == 'w')
            cameraRotX += -0.1;
        if (key == 's')
            cameraRotX += 0.1;
        if (key == 'q')
            cameraRotZ += -0.1;
        if (key == 'e')
            cameraRotZ += 0.1;
    }


    public static void main(String[] args) {
        PApplet.main(new String[] { "br.usp.libras.sanbox.CircularMovementIn3D" });

    }

}
