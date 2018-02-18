package br.usp.libras.jonah;

import static br.usp.libras.jonah.interpolation.Point.point;

import br.usp.libras.jonah.interpolation.CircularInterpolation;
import br.usp.libras.jonah.interpolation.InterpolationTimer;
import br.usp.libras.jonah.interpolation.Point;
import br.usp.libras.sign.movement.Speed;
import br.usp.libras.sign.symbol.Hand;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;
import br.usp.libras.sign.transition.Path;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe responsável por renderizar as mãos do sinal (cada mão possui um objeto
 * diferente) É responsável por controlar diretamente a orientação e o plano da
 * mão, além de determinar a configuração de mão a ser aplicada pelo objeto da
 * classe AnimObj
 * 
 * @author leonardo, koga
 * 
 */
public class HandGraph {

    private static final float DEFAULT_INTERPOLATION_PASS = 0.01f;

    private Hand currentHand, nextHand;

    private PApplet processing;
    private AnimObj handModel;

    private InterpolationTimer interpolationTimer = new InterpolationTimer(DEFAULT_INTERPOLATION_PASS);

    public HandGraph(PApplet processing, Hand hand, Location location) {

        this.processing = processing;
        this.currentHand = hand;
        this.nextHand = hand;

        loadHandModel();
    }

    private void loadHandModel() {
        String shape = currentHand.getShape().name();
        if (currentHand.getSide() == HandSide.RIGHT)
            this.handModel = new AnimObj(processing, ModelsLoader.MODELS_PATH + "dir/" + shape + ".obj");
        else
            this.handModel = new AnimObj(processing, ModelsLoader.MODELS_PATH + "esq/" + shape + ".obj");
    }

    public void nextHand(Hand nextHand) {
        this.currentHand = this.nextHand;
        this.nextHand = nextHand;
        if (nextHand != null) {
            if (!spoke) {
                this.handModel.setNextPose(ModelsLoader.getHandModel(nextHand.getShape(), nextHand.getSide()));
            } else {
                this.handModel.setNextPose(ModelsLoader.getSpockModel(processing));
                spoke = false;
            }
            
            // Inicialização do movimento
            this.handModel.startAnim();
            this.interpolationTimer.reset();
            if (currentHand != null) {
                Speed speed = this.currentHand.getTransition().getSpeed();
                if (speed != null) {
                    if (speed == Speed.LENTO) {
                        interpolationTimer.slowDown(); 
                    }
                    if (speed == Speed.RAPIDO) {
                        interpolationTimer.speedUp(); 
                    }
                }
            }
            
        }
    }

    public void draw() {

        if (nextHand == null) {
            return;
        }

        interpolationTimer.increment();
        this.processing.pushMatrix();
        this.interpolateHandPosition();
        this.interpolateHandRotation();
        this.handModel.draw();
        this.processing.popMatrix();

    }

    // interpola a mão independente da localização da mão no espaço (fç de
    // configuração, orientação e plano)
    private void interpolateHandRotation() {

        // supõe-se que a orientação inicial da mão é BLACK no plano VERTICAL
        // => modelo OBJ deve seguir essa convenção

        float rotY;
        float rotX;
        float rotZ;
        float endY = (float) nextHand.getRotY();
        float endX = (float) nextHand.getRotX();
        float endZ = (float) nextHand.getRotZ();;
        if (currentHand != null) {
            float iniY = (float) currentHand.getRotY();
            float iniX = (float) currentHand.getRotX();
            float iniZ = (float) currentHand.getRotZ();
            rotY = PApplet.map(interpolationTimer.getTime(), 0, 1, iniY, endY);
            rotX = PApplet.map(interpolationTimer.getTime(), 0, 1, iniX, endX);
            rotZ = PApplet.map(interpolationTimer.getTime(), 0, 1, iniZ, endZ);
        } else {
            rotY = endY;
            rotX = endX;
            rotZ = endZ;
        }
        processing.rotateY(rotY);
        processing.rotateX(rotX);
        processing.rotateZ(rotZ);
    }

    /**
     * Calcula a interpolação entre a currentHand e a nextHand.
     */
    private void interpolateHandPosition() {
        
        PVector target = LocationsLoader.getVector(nextHand.getLocation(), nextHand.getSide());
        float x = 0, y = 0, z = 0;
        
        if (currentHand == null) {
            x = target.x;
            y = target.y;
            z = target.z;
        } else {
            PVector origin = LocationsLoader.getVector(currentHand.getLocation(), currentHand.getSide());
            
            float interpolationTime = interpolationTimer.getTime();
            Path path = currentHand.getTransition().getPath();
            if (path == Path.LINEAR) {
                x = PApplet.map(interpolationTime, 0, 1, origin.x, target.x);
                y = PApplet.map(interpolationTime, 0, 1, origin.y, target.y);
                z = PApplet.map(interpolationTime, 0, 1, origin.z, target.z);
                
            } else if (path.isCircular()) {
                Point originPoint = point(origin.x, origin.y, origin.z);
                Point targetPoint = point(target.x, target.y, target.z);
                CircularInterpolation interpolation = new CircularInterpolation(originPoint, targetPoint, path);
                float alpha = PApplet.map(interpolationTime, 0, 1, 0, PApplet.PI);
                if (alpha < PApplet.PI) {
                    System.out.println("alpha=" + alpha);
                }
                Point nextPoint = interpolation.interpolate(alpha);
                x = nextPoint.x;
                y = nextPoint.y;
                z = nextPoint.z;
            }
        }

        this.processing.translate(x, y, z);
    }

    ///////////////////////////////////////////

    protected Hand getHand() {
        return currentHand;
    }

    public boolean hasTransitionEnded() {
        return this.interpolationTimer.hasEnded();
    }

    ///////////////////////////////////////////

    private static boolean spoke = false;

    public void spock() {
        Hand hand = new Hand();
        spoke = true;
        nextHand(hand);
    }

}
