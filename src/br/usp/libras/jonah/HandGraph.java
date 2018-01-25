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

    private static final float DEFAULT_INTERPOLATION_PASS = 0.1f;

    private Hand currentHand, nextHand;

    private PVector finalPositionLastHand;
    private PVector initialPositionNextHand;

    private PApplet processing;
    private AnimObj handModel;

    private InterpolationTimer interpolation = new InterpolationTimer(DEFAULT_INTERPOLATION_PASS);
    private boolean ended;

    public HandGraph(PApplet processing, Hand hand, Location location) {

        this.processing = processing;
        this.currentHand = hand;
        this.nextHand = hand;

        loadHandModel();
        this.finalPositionLastHand = LocationsLoader.getVector(location, hand.getSide());
        this.initialPositionNextHand = this.finalPositionLastHand;
    }

    private void loadHandModel() {
        String shape = currentHand.getShape().name();
        if (currentHand.getSide() == HandSide.RIGHT)
            this.handModel = new AnimObj(processing, ModelsLoader.MODELS_PATH + "dir/" + shape + ".obj");
        else
            this.handModel = new AnimObj(processing, ModelsLoader.MODELS_PATH + "esq/" + shape + ".obj");
    }

    public void nextHand(Hand nextHand) {
        this.nextHand = nextHand;
        if (nextHand != null) {
            this.ended = false;
            if (!spoke) {
                this.handModel.setNextPose(ModelsLoader.getHandModel(nextHand.getShape(), nextHand.getSide()));
            } else {
                this.handModel.setNextPose(ModelsLoader.getSpockModel(processing));
                spoke = false;
            }
            this.handModel.startAnim();
            interpolation.reset();

            this.initialPositionNextHand = LocationsLoader.getVector(nextHand.getLocation(), nextHand.getSide());
        }
    }

    public void draw() {

        // TODO ????
        if (nextHand == null) {
            return;
        }

        if (interpolation.hasNotEnded()) { 

            interpolation.increment(); 

            if (interpolation.hasEnded()) {
                this.currentHand = this.nextHand;
                
                this.finalPositionLastHand = new PVector(initialPositionNextHand.x, initialPositionNextHand.y, initialPositionNextHand.z);
                this.ended = true;
            }
        }
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

        float rotY = 0;
        float iniY = (float) currentHand.getRotY();
        float endY = (float) nextHand.getRotY();
        rotY = PApplet.map(interpolation.getTime(), 0, 1, iniY, endY);
        processing.rotateY(rotY);

        float rotX = 0;
        float iniX = (float) currentHand.getRotX();
        float endX = (float) nextHand.getRotX();
        rotX = PApplet.map(interpolation.getTime(), 0, 1, iniX, endX);
        processing.rotateX(rotX);

        float rotZ = 0;
        float iniR = 0;
        float endR = 0;
        iniR = (float) currentHand.getRotZ();
        endR = (float) nextHand.getRotZ();
        rotZ = PApplet.map(interpolation.getTime(), 0, 1, iniR, endR);
        processing.rotateZ(rotZ);
    }

    private void interpolateHandPosition() {
        
        PVector origin = this.finalPositionLastHand;
        PVector target = this.initialPositionNextHand;
        float interpolationTime = interpolation.getTime();
        
        // deslocamento relativo
        // calcula e aplica deslocamento na mão

        float x = 0, y = 0, z = 0;
        Path path = this.currentHand.getTransition().getPath();

        // velocidade do movimento
        Speed speed = this.currentHand.getTransition().getSpeed();
        if (speed != null) {
            if (speed == Speed.LENTO) {
                interpolation.slowDown(); 
            }
            if (speed == Speed.RAPIDO) {
                this.interpolation.speedUp(); 
            }
        }

        if (path == Path.LINEAR) {
            x = PApplet.map(interpolationTime, 0, 1, origin.x, target.x);
            y = PApplet.map(interpolationTime, 0, 1, origin.y, target.y);
            z = PApplet.map(interpolationTime, 0, 1, origin.z, target.z);
        }

        if (path.isCircular()) {
            Point originPoint = point(origin.x, origin.y, origin.z);
            Point targetPoint = point(target.x, target.y, target.z);
            CircularInterpolation interpolation = new CircularInterpolation(originPoint, targetPoint, path);
            float alpha = PApplet.map(interpolationTime, 0, 1, 0, PApplet.PI / 2);
            Point nextPoint = interpolation.interpolate(alpha);
            x = nextPoint.x;
            y = nextPoint.y;
            z = nextPoint.z;
        }

        this.processing.translate(x, y, z);
    }

    ///////////////////////////////////////////

    protected Hand getHand() {
        return currentHand;
    }

    public boolean hasTransitionEnded() {
        return this.ended;
    }

    ///////////////////////////////////////////

    private static boolean spoke = false;

    public void spock() {
        Hand hand = new Hand();
        spoke = true;
        nextHand(hand);
    }

}
