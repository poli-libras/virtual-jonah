package br.usp.libras.sandbox;

import processing.core.PApplet;

public class Temp extends PApplet {
    
    private float cameraRotX = 0;
    private float cameraRotY = 0;
    private float cameraRotZ = 0;
    private float cameraX = 0;
    private float cameraY = 0;
    private float cameraZ = 0;
    
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
        
        translate(width / 2, height / 2);
        
        // clock circle
        strokeWeight(2);
        ellipse(0, 0, 200, 200);
        
        // seconds
        strokeWeight(1);
        // degrees divided by number of second marks per round
        float radSec = 360 / 60 * second();
        pushMatrix();
        rotate(radians(radSec));
        line(0, 0, 0, -95);
        popMatrix();
        
        // minutes
        strokeWeight(2);
        float radMin = 360 / 60 * minute();
        pushMatrix();
        rotate(radians(radMin));
        line(0, 0, 0, -90);
        popMatrix();
        
        // hours
        strokeWeight(4);
        float radHour = 360 / 12 * hour();
        pushMatrix();
        rotate(radians(radHour));
        line(0, 0, 0, -50);
        popMatrix();
    }
    
    private void rotateCamera() {
        rotateX(cameraRotX);
        rotateY(cameraRotY);
        rotateZ(cameraRotZ);
        translate(cameraX, cameraY, cameraZ);
    }
}
