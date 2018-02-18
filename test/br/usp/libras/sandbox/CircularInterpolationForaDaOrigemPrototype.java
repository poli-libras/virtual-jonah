package br.usp.libras.sandbox;

import br.usp.libras.jonah.interpolation.CircularInterpolation;
import br.usp.libras.jonah.interpolation.Point;
import br.usp.libras.sign.transition.Path;
import processing.core.PApplet;

public class CircularInterpolationForaDaOrigemPrototype extends PApplet {

    private static final long serialVersionUID = 1L;

    private float alpha = 0.0f;

    private float cameraRotX = 0;
    private float cameraRotY = 0;
    private float cameraRotZ = 0;
    private float cameraX = 0;
    private float cameraY = 0;
    private float cameraZ = 0;
    
    private static final float ALPHA_STEP = 0.03f;
//    private static final float ALPHA_STEP = 0.01f;
    
//    float startX = 180, startY = 0, startZ = 0;
//    float endX = -180, endY = -50, endZ = 0; // y final desalinhado com y inicial
//    Path path = Path.CIRCULAR_ANTI_HORARIO_EM_XZ;

    float startX = -80, startY = -145, startZ = 150;
    float endX = -100, endY = 40, endZ = 150; // z final desalinhado com z inicial 
    Path path = Path.CIRCULAR_HORARIO_EM_XY;

//    float startX = 0, startY = 0, startZ = 0;
//    float endX = -30, endY = 0, endZ = 100; // x final desalinhado com x inicial
//    Path path = Path.CIRCULAR_ANTI_HORARIO_EM_YZ;

    boolean debug = true;
    
    private CircularInterpolation interpolation;
    
    @Override
    public void setup() {
        size(800, 500, P3D);
        perspective(PI / 4, 1.0f * width / height, 0.1f, 1500);
        frameRate(20);
        lights();
        interpolation = createCircularInterpolation();
    }

    @Override
    public void draw() {
        
        background(255, 238, 116);
        translate(width/2, height/2);
        rotateCamera();

        drawAxes();
        
        fill(255, 0, 0);
        pushMatrix();
        translate(startX, startY, startZ);
        sphere(5);
        popMatrix();

        fill(0, 255, 0);
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

        // Usando Circular Interpolation
        interpolation = createCircularInterpolation();
        Point point = interpolation.interpolate(alpha);
        translate(point.x, point.y, point.z);
        
        if (debug) {
            System.out.println("startX = " + startX);
            System.out.println("startY = " + startY);
            System.out.println("alpha = " + alpha);
            System.out.println("x = " + point.x);
            System.out.println("y = " + point.y);
            debug = false;
        }

        alpha += ALPHA_STEP;
        if(alpha > PI) {
            alpha = 0;
        }

        fill(0);
        stroke(255);
        box(10);

    }
    
    private void drawAxes() {
         beginShape(LINES);
         stroke(255, 0, 0); // vermelho - 0x
         vertex(0, 0, 0);
         vertex(50, 0, 0);
         stroke(0, 255, 0); // verde - 0y
         vertex(0, 0, 0);
         vertex(0, 50, 0);
         stroke(0, 0, 255); // azul - 0z
         vertex(0, 0, 0);
         vertex(0, 0, 50);
         endShape(LINES);
         stroke(0);
         
         // Oz sobre pontos de início e fim:
         beginShape(LINES);
         stroke(255, 0, 0); // vermelho - 0x
         vertex(startX-100, startY, startZ);
         vertex(startX+100, startY, startZ);
         vertex(endX-100, endY, endZ);
         vertex(endX+100, endY, endZ);
         stroke(0, 255, 0); // verde - 0y
         vertex(startX, startY-100, startZ);
         vertex(startX, startY+100, startZ);
         vertex(endX, endY-100, endZ);
         vertex(endX, endY+100, endZ);
         stroke(0, 0, 255); // azul - 0z
         vertex(startX, startY, startZ-100);
         vertex(startX, startY, startZ+100);
         vertex(endX, endY, endZ-100);
         vertex(endX, endY, endZ+100);
         endShape(LINES);
         stroke(0);
    }

    private void rotateCamera() {
        rotateX(cameraRotX);
        rotateY(cameraRotY);
        rotateZ(cameraRotZ);
        translate(cameraX, cameraY, cameraZ);
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
        if (key == 'i')
            cameraZ += +5;
        if (key == 'k')
            cameraZ += -5;
        if (key == 'l')
            cameraX += -5;
        if (key == 'j')
            cameraX += 5;
        if (key == 'u')
            cameraY += 5;
        if (key == 'o')
            cameraY += -5;
    }
    
    private CircularInterpolation createCircularInterpolation() {
        Point start = new Point(startX, startY, startZ);
        Point end = new Point(endX, endY, endZ);
        return new CircularInterpolation(start, end, path);
    }


    public static void main(String[] args) {
        PApplet.main(new String[] { "br.usp.libras.sandbox.CircularInterpolationForaDaOrigemPrototype" });

    }

}
