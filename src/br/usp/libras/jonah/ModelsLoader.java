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

        map.put(new HandModel(HandShape.INDICADOR, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH
                + "dir/indicador.obj"));
        map.put(new HandModel(HandShape.INDICADOR, HandSide.LEFT), new AnimObj(processing, MODELS_PATH
                + "esq/indicador.obj"));

        map.put(new HandModel(HandShape.MAO_5, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_5.obj"));
        map.put(new HandModel(HandShape.MAO_5, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_5.obj"));

        map.put(new HandModel(HandShape.MAO_5_COM_POLEGAR_PARA_FRENTE, HandSide.RIGHT), new AnimObj(processing,
                MODELS_PATH + "dir/mao_5_com_polegar_para_frente.obj"));
        map.put(new HandModel(HandShape.MAO_5_COM_POLEGAR_PARA_FRENTE, HandSide.LEFT), new AnimObj(processing,
                MODELS_PATH + "esq/mao_5_com_polegar_para_frente.obj"));

        map.put(new HandModel(HandShape.MAO_C, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_c.obj"));
        map.put(new HandModel(HandShape.MAO_C, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_c.obj"));

        map.put(new HandModel(HandShape.CURVADO_X, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH
                + "dir/curvado_x.obj"));
        map.put(new HandModel(HandShape.CURVADO_X, HandSide.LEFT), new AnimObj(processing, MODELS_PATH
                + "esq/curvado_x.obj"));

        map.put(new HandModel(HandShape.FLEXIONADO1, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH
                + "dir/flexionado1.obj"));
        map.put(new HandModel(HandShape.FLEXIONADO1, HandSide.LEFT), new AnimObj(processing, MODELS_PATH
                + "esq/flexionado1.obj"));

        map.put(new HandModel(HandShape.MAO_D, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_d.obj"));
        map.put(new HandModel(HandShape.MAO_D, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_d.obj"));

        map.put(new HandModel(HandShape.MAO_S, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_s.obj"));
        map.put(new HandModel(HandShape.MAO_S, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_s.obj"));

        map.put(new HandModel(HandShape.MAO_2, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_2.obj"));
        map.put(new HandModel(HandShape.MAO_2, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_2.obj"));

        map.put(new HandModel(HandShape.ANGULO_ABERTO_AFASTADO, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/angulo_aberto_afastado.obj"));
        map.put(new HandModel(HandShape.ANGULO_ABERTO_AFASTADO, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/angulo_aberto_afastado.obj"));
        
        map.put(new HandModel(HandShape.C_AFASTADO, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/angulo_aberto_afastado.obj"));
        map.put(new HandModel(HandShape.C_AFASTADO, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/angulo_aberto_afastado.obj"));

        map.put(new HandModel(HandShape.MAO_A, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_a.obj"));
        map.put(new HandModel(HandShape.MAO_A, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_a.obj"));

        map.put(new HandModel(HandShape.MAO_ESTENDIDA_COM_POLEGAR, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_estendida_com_polegar.obj"));
        map.put(new HandModel(HandShape.MAO_ESTENDIDA_COM_POLEGAR, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_estendida_com_polegar.obj"));

        map.put(new HandModel(HandShape.MAO_ONZE, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_onze.obj"));
        map.put(new HandModel(HandShape.MAO_ONZE, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_onze.obj"));

        map.put(new HandModel(HandShape.MAO_ONZE_APERTA, HandSide.RIGHT), new AnimObj(processing, MODELS_PATH + "dir/mao_onze_aperta.obj"));
        map.put(new HandModel(HandShape.MAO_ONZE_APERTA, HandSide.LEFT), new AnimObj(processing, MODELS_PATH + "esq/mao_onze_aperta.obj"));

        // modelo da cabeça
        faceModel = new OBJModel(processing, MODELS_PATH + "face/cabeca.obj");
        hatModel = new OBJModel(processing, MODELS_PATH + "face/gorro.obj");
        
        System.out.println("Loading Face Textures");
        faceTextures.put(Others.NADA, processing.loadImage("resources/models/face/texturas/cabeca1.jpg"));
        faceTextures.put(Others.EXPRESSAO_RADIANTE, processing.loadImage("resources/models/face/texturas/cabeca1.jpg"));
        faceTextures.put(Others.EXPRESSAO_QUESTAO, processing.loadImage("resources/models/face/texturas/cabeca2.jpg"));
        faceTextures.put(Others.QUEIXO, processing.loadImage("resources/models/face/texturas/cabeca3.jpg"));

        System.out.println("Models loaded");
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