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

    private static HandShape[] HAND_MODELS_TO_LOAD = new HandShape[] {HandShape._3DEDOS_O, 
    	HandShape.ANGULO_ABERTO_AFASTADO, HandShape.ANGULO_ABERTO, HandShape.ANGULO_COM_POLEGAR, 
    	HandShape.ANGULO_FECHADO, HandShape.ANGULO_SEM_POLEGAR, HandShape.C_AFASTADO, 
    	HandShape.CURVADOS_COM_POLEGAR, HandShape.CURVADOS, HandShape.CURVADO_U, 
    	HandShape.CURVADO_X, HandShape.FLEXIONADO1, HandShape.FLEXIONADO2, HandShape.FLEXIONADO3, 
    	HandShape.INDICADOR, HandShape.MAO_2, HandShape.MAO_3_6, HandShape.MAO_3, 
    	HandShape.MAO_3_PARA_FRENTE, HandShape.MAO_4, HandShape.MAO_5_COM_POLEGAR_PARA_FRENTE, 
    	HandShape.MAO_5, HandShape.MAO_A, HandShape.MAO_BANHEIRO, HandShape.MAO_B, 
    	HandShape.MAO_C_COM_POLEGAR_PARA_O_LADO, HandShape.MAO_CERTO_ABERTA, 
    	HandShape.MAO_CERTO, HandShape.MAO_CERTO_RETA, HandShape.MAO_C, HandShape.MAO_D, 
    	HandShape.MAO_DOENTE, HandShape.MAO_DROGA, HandShape.MAO_E, 
    	HandShape.MAO_ESTENDIDA_COM_POLEGAR, HandShape.MAO_ESTICADA, HandShape.MAO_FIGA, 
    	HandShape.MAO_F, HandShape.MAO_G, HandShape.MAO_I_AMANTE, HandShape.MAO_I_LOVE_YOU, HandShape.MAO_I, 
    	HandShape.MAO_JESUS, HandShape.MAO_K, HandShape.MAO_L_COM_C, HandShape.MAO_L_COM_O, 
    	HandShape.MAO_L, HandShape.MAO_N, HandShape.MAO_ONZE_APERTA, HandShape.MAO_ONZE, 
    	HandShape.MAO_O, HandShape.MAO_PASSARINHO_ABERTA, HandShape.MAO_PASSARINHO_FECHADA, 
    	HandShape.MAO_PATO_ABERTA, HandShape.MAO_PATO_FECHADA, HandShape.MAO_PROFISSAO, 
    	HandShape.MAO_REVOLVER, HandShape.MAO_R, HandShape.MAO_SETE, HandShape.MAO_S, 
    	HandShape.MAO_SO, HandShape.MAO_TERCA, HandShape.MAO_T, HandShape.MAO_U, HandShape.MAO_W, 
    	HandShape.MAO_Y, HandShape.O_CARACOL, HandShape.O_CURVADO, HandShape.U_COM_POLEGAR} ;
    
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