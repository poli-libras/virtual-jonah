package br.usp.libras.sandbox;

import br.usp.libras.sign.transition.Path;
import processing.core.PApplet;

public class RotacionandoPrototype extends PApplet {

    private static final long serialVersionUID = 1L;

    ////////////////////////
    // Ajustes aqui para variar a demonstração
    private static final float ALPHA_STEP = 0.05f;
    Path path = Path.CIRCULAR_HORARIO_EM_XY;
    //////////////////////
    
    float startX = 0, startY = 0, startZ = 0;
    float endX = 0, endY = 0, endZ = 0;

    private float alpha = 0.0f;

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
        setStartAndEndPoints();
    }

    private void setStartAndEndPoints() {

        if (path.planoXY()) {
            startX = -180;
            startY = -60;
            startZ = 0;
            endX = 100;
            endY = 10;
            endZ = -30; // z final desalinhado com z inicial
        }

        if (path.planoXZ()) {
            startX = 150;
            startY = 0;
            startZ = 10;
            endX = -180;
            endY = -50;
            endZ = 0; // y final desalinhado com y inicial
        }
        
        if (path.planoYZ()) {
            startX = 0;
            startY = -50;
            startZ = 0;
            endX = -30;
            endY = -10;
            endZ = 100; // x final desalinhado com x inicial
        }

    }

    @Override
    public void draw() {

        background(255, 238, 116);
        translate(width / 2, height / 2);
        rotateCamera();

        drawAxes();

        drawStart();

        drawEnd();

        float centerX = (endX + startX) / 2;
        float centerY = (endY + startY) / 2;
        float centerZ = (endZ + startZ) / 2;
        drawCenter(centerX, centerY, centerZ);

        float raio = PApplet.dist(startX, startY, startZ, endX, endY, endZ) / 2;
        int sentido = path.horario() ? -1 : 1;

        if (path.planoXY()) {
            float shiftZ = 0;
            if (alpha < PI) {
                shiftZ = map(alpha, 0, PI, startZ, endZ);
            } else {
                shiftZ = map(alpha, PI, TWO_PI, endZ, startZ);
            }
            translate(centerX, centerY, shiftZ);
            float alinhamento = atan((startY-centerY) / (startX-centerX));
            rotateZ(alinhamento + alpha * sentido);
            translate(-raio, 0, 0);
        }

        if (path.planoXZ()) {
            float shiftY = 0;
            if (alpha < PI) {
                shiftY = map(alpha, 0, PI, startY, endY);
            } else {
                shiftY = map(alpha, PI, TWO_PI, endY, startY);
            }
            translate(centerX, shiftY, centerZ);
            float alinhamento = atan((startX-centerX) / (startZ-centerZ));
            rotateY(alinhamento + alpha * sentido);
            translate(0, 0, raio);
        }
        
        if (path.planoYZ()) {
            float shiftX = 0;
            if (alpha < PI) {
                shiftX = map(alpha, 0, PI, startX, endX);
            } else {
                shiftX = map(alpha, PI, TWO_PI, endX, startX);
            }
            translate(shiftX, centerY, centerZ);
            float alinhamento = atan((startZ-centerZ) / (startY-centerY));
            rotateX(alinhamento + alpha * sentido);
            translate(0, -raio, 0);
        }

        // draw box
        fill(0);
        stroke(255);
        box(10);

        alpha += ALPHA_STEP;
        if (alpha > TWO_PI) {
            alpha = 0;
        }

    }

    private void drawCenter(float centerX, float centerY, float centerZ) {
        fill(0, 0, 255);
        pushMatrix();
        translate(centerX, centerY, centerZ);
        sphere(5);
        popMatrix();
    }

    private void drawEnd() {
        fill(0, 255, 0);
        pushMatrix();
        translate(endX, endY, endZ);
        sphere(5);
        popMatrix();
    }

    private void drawStart() {
        fill(255, 0, 0);
        pushMatrix();
        translate(startX, startY, startZ);
        sphere(5);
        popMatrix();
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
        vertex(startX - 100, startY, startZ);
        vertex(startX + 100, startY, startZ);
        vertex(endX - 100, endY, endZ);
        vertex(endX + 100, endY, endZ);
        stroke(0, 255, 0); // verde - 0y
        vertex(startX, startY - 100, startZ);
        vertex(startX, startY + 100, startZ);
        vertex(endX, endY - 100, endZ);
        vertex(endX, endY + 100, endZ);
        stroke(0, 0, 255); // azul - 0z
        vertex(startX, startY, startZ - 100);
        vertex(startX, startY, startZ + 100);
        vertex(endX, endY, endZ - 100);
        vertex(endX, endY, endZ + 100);
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

    public static void main(String[] args) {

        PApplet.main(new String[] { "br.usp.libras.sandbox.RotacionandoPrototype" });

    }

}
