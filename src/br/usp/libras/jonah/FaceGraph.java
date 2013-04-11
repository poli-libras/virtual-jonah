package br.usp.libras.jonah;

import processing.core.PApplet;
import processing.core.PImage;
import br.usp.libras.sign.face.Face;
import br.usp.libras.sign.face.Others;

/**
 * Classe responável por renderizar o rosto do avatar e fornecer as posições das localizações do rosto
 * 
 * @author leonardo
 * @author mlk
 * 
 */
public class FaceGraph {

    private Face face;
    private PApplet processing;

    // Actual implementation of facial expressions only uses preset faces instead of
    // moving each part of the face. Preset faces are set by the "Others" attribute of the face.

    public FaceGraph(PApplet processing, Face face) {

        this.face = face;
        this.processing = processing;
    }

    void draw() {

        changeExpression();
        ModelsLoader.getFaceModel().draw();

        processing.pushMatrix();
        processing.translate(0, -10, 25);
        ModelsLoader.getHatModel().draw();
        processing.popMatrix();
    }

    void nextSign(Face nextFace) {
        this.face = nextFace;
    }

    void changeExpression() {
        PImage faceTexture;
        if (this.face != null) {
            faceTexture = ModelsLoader.getFaceTexture(face.getOthers());
        } else {
            faceTexture = ModelsLoader.getFaceTexture(Others.NADA);
        }
        ModelsLoader.getFaceModel().setTexture(faceTexture);
    }
}
