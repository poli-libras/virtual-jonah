package br.usp.libras.jonah;

import static br.usp.libras.jonah.interpolation.Point.point;

import br.usp.libras.jonah.interpolation.CircularInterpolation;
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

	private static final float DEFAULT_PASS = 0.1f; // passo da interpolação

	private enum AnimationPhase {
		INITIAL_INTERPOLATION, MOVEMENT
	};

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

	private PApplet processing;
	private AnimObj model;

	private float interp = 1; // interp = 0: início da interpolação; interp = 1:
								// interpolação terminada
	private float interpMove = 1; // interpolação da parte do movimento
	private boolean ended;

	/**
	 * 
	 * @param processing
	 *            sketch do Processing
	 * @param hand
	 *            definição da mão a ser renderizada
	 * @param contact
	 *            ponto de contato (pode ser null)
	 */
	public HandGraph(PApplet processing, Hand hand, Location location) {

		this.processing = processing;
		this.hand = hand;
		this.nextHand = hand;

		this.animationPhase = AnimationPhase.INITIAL_INTERPOLATION;

		// primeira mão não pega de ModelsLoader pra não estragar o modelo
		// (????)
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
	 * @param nextHand
	 *            próximo estado da mão a ser renderizado
	 */
	public void nextHand(Hand nextHand) {
		this.nextHand = nextHand;
		if (nextHand != null) {
			this.ended = false;
			this.animationPhase = AnimationPhase.INITIAL_INTERPOLATION;
			if (!spoke) {
				this.model.setNextPose(ModelsLoader.getHandModel(nextHand.getShape(), nextHand.getSide()));
			} else {
				this.model.setNextPose(ModelsLoader.getSpockModel(processing));
				spoke = false;
			}
			this.model.startAnim();
			this.interp = 0; // inicia interpolação

			this.posBeginMove = LocationsLoader.getVector(nextHand.getLocation(), nextHand.getSide());
			this.posEndMove = this.posBeginMove;
			this.interpMove = 1;
		}
	}

	public void draw() {
		
		if (nextHand == null) {
			return;
		}

		// movimento tem duas fases: uma de posicionamento para poder iniciar o
		// movimento
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
			this.interpolatePosition(interp, this.posHand, this.posBeginMove);
			this.interpolateHandRotation();
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
			this.interpolateHandRotation();
			this.model.draw();
			this.processing.popMatrix();

			if (interpMove >= 1.0) { // fim da interpolação
			    interpMove = 1.0f;
			    this.hand = this.nextHand;
			    this.posHand = new PVector(posEndMove.x, posEndMove.y, posEndMove.z);
			    this.pass = DEFAULT_PASS;
			    this.ended = true;
			}

			break;
		}
	}

	// interpola a mão independente da localização da mão no espaço (fç de
	// configuração, orientação e plano)
	private void interpolateHandRotation() {

		// supõe-se que a orientação inicial da mão é BLACK no plano VERTICAL
		// => modelo OBJ deve seguir essa convenção

		float rotY = 0;
		float iniY = (float) hand.getRotY();
		float endY = (float) nextHand.getRotY();
		rotY = PApplet.map(interp, 0, 1, iniY, endY);
		processing.rotateY(rotY);

		float rotX = 0;
		float iniX = (float) hand.getRotX();
		float endX = (float) nextHand.getRotX();
		rotX = PApplet.map(interp, 0, 1, iniX, endX);
		processing.rotateX(rotX);

		float rotZ = 0;
		float iniR = 0;
		float endR = 0;
		iniR = (float) hand.getRotZ();
		endR = (float) nextHand.getRotZ();
		rotZ = PApplet.map(interp, 0, 1, iniR, endR);
		processing.rotateZ(rotZ);
	}

	private void interpolatePosition(float value, PVector origin, PVector target) {
		// deslocamento relativo
		// calcula e aplica deslocamento na mão

	    float x = 0, y = 0, z = 0;
	    Path path = this.hand.getTransition().getPath();
	    
        // velocidade do movimento
        Speed speed = this.hand.getTransition().getSpeed();
        if (speed != null) {
            if (speed == Speed.LENTO) {
                this.pass = DEFAULT_PASS / 2;
            }
            if (speed == Speed.RAPIDO) {
                this.pass = 2 * DEFAULT_PASS;
            }
        }

	    if (path == Path.LINEAR) {
    		x = PApplet.map(value, 0, 1, origin.x, target.x);
    		y = PApplet.map(value, 0, 1, origin.y, target.y);
    		z = PApplet.map(value, 0, 1, origin.z, target.z);
	    }
	    
	    if (path.isCircular()) {
	        Point originPoint = point(origin.x, origin.y, origin.z);
	        Point targetPoint = point(target.x, target.y, target.z);
            CircularInterpolation interpolation = new CircularInterpolation(originPoint, targetPoint, path);
            float alpha = PApplet.map(value, 0, 1, 0, PApplet.PI/2);
            Point nextPoint = interpolation.interpolate(alpha);
            x = nextPoint.x;
            y = nextPoint.y;
            z = nextPoint.z;
	    }

	    this.processing.translate(x, y, z);
	}

	private void move() {

		if (nextHand.getMovement() == null) {
			interpolatePosition(interpMove, this.posBeginMove, this.posEndMove);
		} else {
			interpolatePosition(interpMove, this.posBeginMove, this.posEndMove);
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
		nextHand(hand);
	}

}
