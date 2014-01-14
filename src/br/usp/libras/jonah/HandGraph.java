package br.usp.libras.jonah;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import br.usp.libras.sign.movement.Frequency;
import br.usp.libras.sign.movement.HandMovement;
import br.usp.libras.sign.movement.Segment;
import br.usp.libras.sign.movement.Speed;
import br.usp.libras.sign.symbol.Hand;
import br.usp.libras.sign.symbol.HandOrientation;
import br.usp.libras.sign.symbol.HandPlane;
import br.usp.libras.sign.symbol.HandRotation;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;

/**
 * Classe responsável por renderizar as mãos do sinal (cada mão possui um objeto diferente) É responsável por controlar
 * diretamente a orientação e o plano da mão, além de determinar a configuração de mão a ser aplicada pelo objeto da
 * classe AnimObj
 * 
 * @author leonardo, koga
 * 
 */
public class HandGraph {

    private static final float DEFAULT_PASS = 0.1f; // passo da interpolação

    private enum AnimationPhase {
        INITIAL_INTERPOLATION, MOVEMENT
    };
    // if startInLocation:
    // 		INITIAL_INTERPOLATION leva mão para o ponto de locação
    // else
    //		INITIAL_INTERPOLATION leva mão para um local de forma que ao fim do movimento 
    //								(i.e.: depois dos segmentos) a mão estará no ponto de locação

    private float pass = DEFAULT_PASS; 

    private Hand hand, nextHand;

    // vetores de deslocamento
    /** Position of last Hand */
    private PVector posHand;
    /** Position of new hand: where to go to begin movement */
    private PVector posBeginMove;
    /** Position of new hand: final point of movement */
    private PVector posEndMove;

    private AnimationPhase animationPhase;

    private List<Segment> segments;

    private PApplet processing;
    private AnimObj model;

    private float interp = 1; // interp = 0: início da interpolação; interp = 1: interpolação terminada
    private float interpMove = 1; // interpolação da parte do movimento
    private boolean ended;

    /**
     * 
     * @param processing sketch do Processing
     * @param hand definição da mão a ser renderizada
     * @param contact ponto de contato (pode ser null)
     */
    public HandGraph(PApplet processing, Hand hand, Location location, boolean handsInUnity) {

        this.processing = processing;
        this.hand = hand;
        this.nextHand = hand;

        this.animationPhase = AnimationPhase.INITIAL_INTERPOLATION;
        
        // primeira mão não pega de ModelsLoader pra não estragar o modelo (????)
        String shape = hand.getShape().name();
        if (hand.getSide() == HandSide.RIGHT)
            this.model = new AnimObj(processing, ModelsLoader.MODELS_PATH + "dir/" + shape + ".obj");
        else
            this.model = new AnimObj(processing, ModelsLoader.MODELS_PATH + "esq/" + shape + ".obj");

        this.posHand = LocationsLoader.getVector(location, hand.getSide());
        this.posBeginMove = this.posHand;
        this.posEndMove = this.posHand;
    }

    /**
     * Altera o estado da mão renderizada
     * 
     * @param nextHand próximo estado da mão a ser renderizado
     */
    public void nextHand(Hand nextHand, Location location, boolean handsInUnity) {
        
        if (nextHand != null) {
            this.ended = false;
            this.animationPhase = AnimationPhase.INITIAL_INTERPOLATION;
            this.nextHand = nextHand;
            if (!spoke) {
            	this.model.setNextPose(ModelsLoader.getHandModel(nextHand.getShape(), nextHand.getSide()));
            } else {
            	this.model.setNextPose(ModelsLoader.getSpockModel(processing));
            	spoke = false;
            }
            this.model.startAnim();
            this.interp = 0; // inicia interpolação

            // If doesn't have movement
            if (nextHand.getMovement() == null) {
                if (nextHand.getSide() == HandSide.LEFT && !handsInUnity) {
                    this.posBeginMove = LocationsLoader.getVector(Location.ESPACO_NEUTRO, nextHand.getSide());
                } else {
                    this.posBeginMove = LocationsLoader.getVector(location, nextHand.getSide());
                }
                this.posEndMove = this.posBeginMove;
                this.interpMove = 1;
            } else {

                // Movement
                this.interpMove = 0;
                this.segments = new ArrayList<Segment>(nextHand.getMovement().getSegments());
                Segment segment = this.segments.remove(0); // remove próximo da lista

                // seta posições da trajetória do movimento
                this.posBeginMove = this.calculateBeginMov(location, nextHand.getMovement(), nextHand.getSide());
                this.posEndMove = PVector.add(this.posBeginMove,
                		DirectionsTranslator.getDirectionVector(segment.getDirection()));
                
                // velocidade do movimento
                Speed speed = nextHand.getMovement().getSpeed();
                if (speed != null) {
                    
                    if (speed == Speed.LENTO)
                        this.pass = DEFAULT_PASS/2;
                    if (speed == Speed.NORMAL)
                        this.pass = DEFAULT_PASS;
                    if (speed == Speed.RAPIDO)
                        this.pass = 2*DEFAULT_PASS;
                }                
                
                // frequência do movimento
                Frequency freq = nextHand.getMovement().getFrequency();
                if (freq != null && freq == Frequency.REPETIDO) {
                    
                    // faz repetido 3 vezes e rápido
                    this.pass = 2*DEFAULT_PASS; 
                    Segment inv = segment.clone();
                    Segment again = segment.clone();
                    inv.invert();
                    this.segments.add(inv);
                    this.segments.add(again);
                    inv = inv.clone();
                    again = again.clone();
                    this.segments.add(inv);
                    this.segments.add(again);
                }
            }
        }
    }

    /**
     * Calculates the point where the hand must to be at the end of INITIAL_INTERPOLATION phase
     * (this is the actual beggining of the movement)
     * @param location
     * @param side
     * @return
     */
    private PVector calculateBeginMov(Location location, HandMovement mov, HandSide side) {

    	PVector begin = LocationsLoader.getVector(location, side);
    	if (!mov.isStartsInLocation()) {
    		// prepara lista inversa
    		List<Segment> inverse = new ArrayList<Segment>();
    		for (int i=mov.getSegments().size()-1; i>=0; i--) {
    			inverse.add(mov.getSegments().get(i));
    		}
    		// subtrai sucessivamente os segmentos para achar o ponto inicial
    		for (Segment seg: inverse) {
    			begin = PVector.sub(begin, DirectionsTranslator.getDirectionVector(seg.getDirection()));
    		}
    	}
    	return begin;
	}

	public void draw() {
        
        // movimento tem duas fases: uma de posicionamento para poder iniciar o movimento
        // e outra que é do propriamento dito
        // sendo que o movimento pode começar ou terminar no ponto de locação

        switch (this.animationPhase) {
            case INITIAL_INTERPOLATION:

                // incrementa a interpolação, se necessário
                if (interp < 1.0) {

                    interp = interp + pass; // incremento da interpolação

                    if (interp >= 1.0) { // fim da interpolação
                        interp = 1.0f;
                        this.animationPhase = AnimationPhase.MOVEMENT;
                        this.hand = this.nextHand;
                    }
                }
                this.processing.pushMatrix();
                this.interpolate(interp, this.posHand, this.posBeginMove);
                this.interpolateHand();
                this.model.draw();
                this.processing.popMatrix();
                break;

            case MOVEMENT:

                // incrementa a interpolação, se necessário
                if (interpMove < 1.0) {
                    interpMove = interpMove + pass; // incremento da interpolação
                }

                // desenha de fato a mão
                this.processing.pushMatrix();
                this.move();
                this.interpolateHand();
                this.model.draw();
                this.processing.popMatrix();

                if (interpMove >= 1.0) { // fim da interpolação
                                        
                    // checa se há mais segmentos
                    Segment seg = null;
                    if (this.segments != null && !this.segments.isEmpty())
                        seg = this.segments.remove(0);
                    
                    if (seg != null) { // mais segmentos
                        interp = 0;
                        interpMove = 0;
                        this.posHand = new PVector(posEndMove.x, posEndMove.y, posEndMove.z);
                        PVector dir = DirectionsTranslator.getDirectionVector(seg.getDirection()); 
                        this.posBeginMove = new PVector(this.posEndMove.x, this.posEndMove.y, this.posEndMove.z); 
                        this.posEndMove = PVector.add(this.posBeginMove, dir); 
                    }
                    else { // fim do movimento
                        interpMove = 1.0f;
                        this.hand = this.nextHand;
                        this.posHand = new PVector(posEndMove.x, posEndMove.y, posEndMove.z);
                        this.pass = DEFAULT_PASS;                        
                        this.ended = true;
                    }
                }
                
                break;
        }
    }
    
    // interpola a mão independente da localização da mão no espaço (fç de configuração, orientação e plano)
    private void interpolateHand() {

        // supõe-se que a orientação inicial da mão é BLACK no plano VERTICAL
        // => modelo OBJ deve seguir essa convenção

        float rot = 0, ini = 0, end = 0;
        int sense;

        if (nextHand.getSide() == HandSide.RIGHT)
            sense = 1;
        else
            // LEFT
            sense = -1;

        // controle de plano
        if (hand.getPlane() == HandPlane.VERTICAL)
            ini = 0;
        if (hand.getPlane() == HandPlane.HORIZONTAL)
            ini = -PApplet.PI / 2;
        if (nextHand.getPlane() == HandPlane.VERTICAL)
            end = 0;
        if (nextHand.getPlane() == HandPlane.HORIZONTAL)
            end = -PApplet.PI / 2;
        rot = PApplet.map(interp, 0, 1, ini, end);
        processing.rotateX(rot);

        rot = 0;
        ini = 0;
        end = 0;

        // controle de orientação
        if (hand.getOrientation() == HandOrientation.BLACK)
            ini = 0;
        if (hand.getOrientation() == HandOrientation.HALF)
            ini = PApplet.HALF_PI;
        if (hand.getOrientation() == HandOrientation.WHITE)
            ini = PApplet.PI;
        if (nextHand.getOrientation() == HandOrientation.BLACK)
            end = 0;
        if (nextHand.getOrientation() == HandOrientation.HALF)
            end = PApplet.HALF_PI;
        if (nextHand.getOrientation() == HandOrientation.WHITE)
            end = PApplet.PI;
        rot = PApplet.map(interp, 0, 1, ini, end);
        processing.rotateY(sense * rot);

        rot = 0;
        ini = 0;
        end = 0;

        float rot2 = 0;
        float ini2 = 0;
        float end2 = 0;

        boolean half = hand.getOrientation() == HandOrientation.HALF;
        boolean nextHalf = nextHand.getOrientation() == HandOrientation.HALF;

        // Rotacao no eixo Z (palma)
        if (!half) {
            if (hand.getRotation() == HandRotation.ZERO)
                ini = 0;
            if (hand.getRotation() == HandRotation.RETO)
                ini = PApplet.HALF_PI;
            if (hand.getRotation() == HandRotation.RASO)
                ini = PApplet.PI;
        } else {
            if (hand.getRotation() == HandRotation.ZERO)
                ini2 = 0;
            if (hand.getRotation() == HandRotation.RETO)
                ini2 = PApplet.HALF_PI;
            if (hand.getRotation() == HandRotation.RASO)
                ini2 = PApplet.PI;
        }
        if (!nextHalf) {
            if (nextHand.getRotation() == HandRotation.ZERO)
                end = 0;
            if (nextHand.getRotation() == HandRotation.RETO)
                end = PApplet.HALF_PI;
            if (nextHand.getRotation() == HandRotation.RASO)
                end = PApplet.PI;
        } else {
            if (nextHand.getRotation() == HandRotation.ZERO)
                end2 = 0;
            if (nextHand.getRotation() == HandRotation.RETO)
                end2 = PApplet.HALF_PI;
            if (nextHand.getRotation() == HandRotation.RASO)
                end2 = PApplet.PI;
        }
        rot = PApplet.map(interp, 0, 1, ini, end);
        rot2 = PApplet.map(interp, 0, 1, ini2, end2);
        processing.rotateZ(-sense * rot);
        processing.rotateX(-rot2);

    }

    private void interpolate(float value, PVector origin, PVector target) {
        // deslocamento relativo
        // calcula e aplica deslocamento na mão
        float x = PApplet.map(value, 0, 1, origin.x, target.x);
        float y = PApplet.map(value, 0, 1, origin.y, target.y);
        float z = PApplet.map(value, 0, 1, origin.z, target.z);

        this.processing.translate(x, y, z);
    }

    private void move() {

        if (nextHand.getMovement() == null) {
            interpolate(interpMove, this.posBeginMove, this.posEndMove);
        } else {
            switch (nextHand.getMovement().getType()) {
                case RETILINIO:
                    interpolate(interpMove, this.posBeginMove, this.posEndMove);
                    break;
                default:

            }
        }
    }

    ///////////////////////////////////////////

    protected Hand getHand() {
        return hand;
    }

    public boolean hasTransitionEnded() {
        return this.ended;
    }

    ///////////////////////////////////////////

    private static boolean spoke = false;
	public void spock() {
		Hand hand = new Hand();
		spoke = true;
		nextHand(hand, Location.ESPACO_NEUTRO, false);
	}
    
}
