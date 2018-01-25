package br.usp.libras.jonah;

import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Symbol;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe responsável por orquestrar a execução do sinal de acordo com o especificado por um objeto da classe Sign; é
 * esta classe que determina a posição da cabeça e das mãos, incluindo a translação das mãos para os pontos de
 * localização do sinal.
 * 
 * @author leonardo
 * 
 */
public class SymbolGraph {

    private static final PVector POS_FACE = new PVector(0, -140, 0);

    private Symbol symbol;
    private HandGraph leftHandGraph, rightHandGraph;
    private FaceGraph faceGraph;
    private PApplet processing;

    /**
     * Construtor
     * 
     * @param processing sketch do Processing
     * @param symbol definição do sinal a ser renderizado
     */
    public SymbolGraph(PApplet processing, Symbol symbol) {
        this.symbol = symbol;
        this.faceGraph = new FaceGraph(processing, symbol.getFace());
        this.processing = processing;
        this.rightHandGraph = new HandGraph(processing, symbol.getRightHand(), symbol.getRightHand().getLocation());
        if (symbol.getLeftHand() != null)
            this.leftHandGraph = new HandGraph(processing, symbol.getLeftHand(), symbol.getLeftHand().getLocation());
    }

    public void draw() {
        
        processing.pushMatrix();
        processing.translate(POS_FACE.x, POS_FACE.y, POS_FACE.z);
        faceGraph.draw();
        processing.popMatrix(); 

        this.rightHandGraph.draw();

        if (this.leftHandGraph != null)
            this.leftHandGraph.draw();
    }

    /**
     * 
     * @param nextSymbol definição do próximo sinal a ser renderizado
     */
    public void nextSymbol(Symbol nextSymbol) {
    	
        this.faceGraph.nextSign(nextSymbol.getFace());
    	this.rightHandGraph.nextHand(nextSymbol.getRightHand());
        this.leftHandGraph.nextHand(nextSymbol.getLeftHand());
        this.symbol = nextSymbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
    
    public FaceGraph getFaceGraph() {
        return this.faceGraph;
    }
    
    public HandGraph getHandGraph(HandSide side) {
    	if (side == HandSide.LEFT)
    		return this.leftHandGraph;
    	else
    		return this.rightHandGraph;
    }
    
    public boolean hasTransitionEnded() {
        boolean ok = this.rightHandGraph.hasTransitionEnded();
        if (this.leftHandGraph != null)
            return ok && this.leftHandGraph.hasTransitionEnded();
        else
            return ok;
    }
    
    public void spock() {
    	this.rightHandGraph.spock();
    }

}
