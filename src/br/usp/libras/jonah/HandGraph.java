package br.usp.libras.jonah;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import br.usp.libras.jonah.interpolation.CircularInterpolation;
import br.usp.libras.jonah.interpolation.Point;
import br.usp.libras.sign.movement.Frequency;
import br.usp.libras.sign.movement.HandMovement;
import br.usp.libras.sign.movement.Segment;
import br.usp.libras.sign.movement.Speed;
import br.usp.libras.sign.symbol.Hand;
//import br.usp.libras.sign.symbol.HandOrientation;
//import br.usp.libras.sign.symbol.HandPlane;
//import br.usp.libras.sign.symbol.HandRotation;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;
import br.usp.libras.sign.transition.Path;

import static br.usp.libras.jonah.interpolation.Point.point;

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
	// if startInLocation:
	// INITIAL_INTERPOLATION leva mão para o ponto de locação
	// else
	// INITIAL_INTERPOLATION leva mão para um local de forma que ao fim do
	// movimento
	// (i.e.: depois dos segmentos) a mão estará no ponto de locação

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

			// If doesn't have movement
			if (nextHand.getMovement() == null) {
				this.posBeginMove = LocationsLoader.getVector(nextHand.getLocation(), nextHand.getSide());
				this.posEndMove = this.posBeginMove;
				this.interpMove = 1;
			} else {

				// Movement
				this.interpMove = 0;
				this.segments = new ArrayList<Segment>(nextHand.getMovement().getSegments());
				Segment segment = this.segments.remove(0); // remove próximo da
															// lista

				// seta posições da trajetória do movimento
				this.posBeginMove = this.calculateBeginMov(nextHand.getLocation(), nextHand.getMovement(), nextHand.getSide());
				this.posEndMove = PVector.add(this.posBeginMove,
						DirectionsTranslator.getDirectionVector(segment.getDirection()));

				// velocidade do movimento
				Speed speed = nextHand.getMovement().getSpeed();
				if (speed != null) {

					if (speed == Speed.LENTO)
						this.pass = DEFAULT_PASS / 2;
					if (speed == Speed.NORMAL)
						this.pass = DEFAULT_PASS;
					if (speed == Speed.RAPIDO)
						this.pass = 2 * DEFAULT_PASS;
				}

				// frequência do movimento
				Frequency freq = nextHand.getMovement().getFrequency();
				if (freq != null && freq == Frequency.REPETIDO) {

					// faz repetido 3 vezes e rápido
					this.pass = 2 * DEFAULT_PASS;
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
	 * Calculates the point where the hand must to be at the end of
	 * INITIAL_INTERPOLATION phase (this is the actual beggining of the
	 * movement)
	 * 
	 * @param location
	 * @param side
	 * @return
	 */
	private PVector calculateBeginMov(Location location, HandMovement mov, HandSide side) {

		PVector begin = LocationsLoader.getVector(location, side);
		if (!mov.isStartsInLocation()) {
			// prepara lista inversa
			List<Segment> inverse = new ArrayList<Segment>();
			for (int i = mov.getSegments().size() - 1; i >= 0; i--) {
				inverse.add(mov.getSegments().get(i));
			}
			// subtrai sucessivamente os segmentos para achar o ponto inicial
			for (Segment seg : inverse) {
				begin = PVector.sub(begin, DirectionsTranslator.getDirectionVector(seg.getDirection()));
			}
		}
		return begin;
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
				} else { // fim do movimento
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
