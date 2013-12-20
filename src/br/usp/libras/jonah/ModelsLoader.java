package br.usp.libras.jonah;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import saito.objloader.OBJModel;
import br.usp.libras.sign.face.Others;
import br.usp.libras.sign.symbol.HandShape;
import br.usp.libras.sign.symbol.HandSide;

/**
 * Carrega e disponibiliza os modelos (obj)
 * 
 * @author leonardo
 * 
 */
public class ModelsLoader {

    public static final String MODELS_PATH = "resources/models/";

    private static HandShape[] HAND_MODELS_TO_LOAD = new HandShape[] {HandShape.ANGULO_ABERTO_AFASTADO, 
    	HandShape.ANGULO_ABERTO, HandShape.ANGULO_FECHADO, HandShape.CURVADO_X, HandShape.INDICADOR, 
    	HandShape.MAO_2, HandShape.MAO_5_COM_POLEGAR_PARA_FRENTE, HandShape.MAO_5, HandShape.MAO_A, HandShape.MAO_B, 
    	HandShape.MAO_CERTO, HandShape.MAO_ESTENDIDA_COM_POLEGAR, HandShape.MAO_ESTICADA, HandShape.MAO_L_COM_C, 
    	HandShape.MAO_L, HandShape.MAO_ONZE, HandShape.MAO_PASSARINHO_FECHADA, HandShape.MAO_S, HandShape.MAO_U, 
    	HandShape.MAO_Y} ;
    
    private static Map<HandModel, AnimObj> map = new HashMap<HandModel, AnimObj>();

    private static OBJModel faceModel, hatModel;
    
    private static Map<Others,PImage> faceTextures = new HashMap<Others,PImage>();

    /**
     * Execute este método antes de acessar os modelos!!!
     * 
     * @param processing
     * 
     */
    public static void loadModels(PApplet processing) {

        System.out.println("Loading models");

        for (HandShape shape : HAND_MODELS_TO_LOAD)
        	loadHandModel(shape, processing);
        
        loadFaceModels(processing);
        
        loadFaceTextures(processing);

        System.out.println("Models loaded");
    }

    private static void loadHandModel(HandShape shape, PApplet processing) {
        map.put(new HandModel(shape, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/" + shape.name() + ".obj"));
        map.put(new HandModel(shape, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/" + shape.name() + ".obj"));
    }

	private static void loadFaceModels(PApplet processing) {
        faceModel = new OBJModel(processing, MODELS_PATH + "face/cabeca.obj");
        hatModel = new OBJModel(processing, MODELS_PATH + "face/gorro.obj");
	}

	private static void loadFaceTextures(PApplet processing) {
		System.out.println("Loading Face Textures");
        faceTextures.put(Others.NADA, processing.loadImage("resources/models/face/texturas/cabeca1.jpg"));
        faceTextures.put(Others.EXPRESSAO_RADIANTE, processing.loadImage("resources/models/face/texturas/cabeca1.jpg"));
        faceTextures.put(Others.EXPRESSAO_QUESTAO, processing.loadImage("resources/models/face/texturas/cabeca2.jpg"));
        faceTextures.put(Others.QUEIXO, processing.loadImage("resources/models/face/texturas/cabeca3.jpg"));
	}

    /**
     * Retorna o modelo de acordo com a configuração e lado especificados Antes de poder usar esta função, loadModels
     * deve ser executado uma vez
     * 
     * @param shape configuração de mão
     * @param side lado da mão
     * @return modelo carregado a partir de arquivo obj
     */
    public static AnimObj getHandModel(HandShape shape, HandSide side) {
        // TODO: coleção interna de objs não deveria ser modificável por irresponsáveis que usam este método
        return map.get(new HandModel(shape, side));
    }

    /**
     * 
     * @return modelo (obj) do rosto
     */
    public static OBJModel getFaceModel() {
        return faceModel;
    }

    /**
     * 
     * @return modelo (obj) do gorrinho do muleque
     */
    public static OBJModel getHatModel() {
        return hatModel;
    }
    
    /**
     * Returns the adequate face texture
     * @param other
     * @return correpondent texture; If not found, returns some default;
     */
    public static PImage getFaceTexture(Others other) {
        PImage texture = faceTextures.get(other);
        return texture != null ? texture : faceTextures.get(Others.EXPRESSAO_RADIANTE);
    }

}