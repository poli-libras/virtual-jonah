package br.usp.libras.jonah;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	private static Map<Others, PImage> faceTextures = new HashMap<Others, PImage>();

	/**
	 * Execute este método antes de acessar os modelos!!!
	 * 
	 * @param processing
	 * 
	 */
	public static void loadModels(PApplet processing) {
		loadHandModels(processing);
		loadHeadModels(processing);
		loadTextures(processing);
		System.out.println("Models loaded");
	}

	private static void loadHandModels(PApplet processing) {
		System.out.println("Loading hand models");
		List<String> fileNames = getExistingHandModelFileNames();
		for (String fileName : fileNames) {
			try {
				HandShape shape = shapeFrom(fileName);
				HandModel rightHandModel = new HandModel(shape, HandSide.RIGHT);
				AnimObj rightAnumObj = new AnimObj(processing, MODELS_PATH + "dir/" + fileName);
				map.put(rightHandModel, rightAnumObj);
				HandModel leftHandModel = new HandModel(shape, HandSide.LEFT);
				AnimObj leftAnimObj = new AnimObj(processing, MODELS_PATH + "esq/" + fileName);
				map.put(leftHandModel, leftAnimObj);
			} catch (IllegalArgumentException e) {
				System.out.println(fileName + " does not correspond to a hand shape!");
			}
		}
	}
	
	private static List<String> getExistingHandModelFileNames() {
		File modelsFolder = new File(MODELS_PATH + "dir");
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".obj");
			}
		};
		File[] files = modelsFolder.listFiles(filter);
		List<String> fileNames = new ArrayList<String>();
		for (File f : files)
			fileNames.add(f.getName());
		return fileNames;
	}

	private static HandShape shapeFrom(String fileName) {
		String shapeStr = fileName.replace(".obj", "").toUpperCase();
		HandShape shape = HandShape.valueOf(shapeStr);
		return shape;
	}
	
	private static void loadHeadModels(PApplet processing) {
		System.out.println("Loadind head models");
		faceModel = new OBJModel(processing, MODELS_PATH + "face/cabeca.obj");
		hatModel = new OBJModel(processing, MODELS_PATH + "face/gorro.obj");
	}
	
	private static void loadTextures(PApplet processing) {
		System.out.println("Loading Face Textures");
		faceTextures.put(Others.NADA, processing
				.loadImage("resources/models/face/texturas/cabeca1.jpg"));
		faceTextures.put(Others.EXPRESSAO_RADIANTE, processing
				.loadImage("resources/models/face/texturas/cabeca1.jpg"));
		faceTextures.put(Others.EXPRESSAO_QUESTAO, processing
				.loadImage("resources/models/face/texturas/cabeca2.jpg"));
		faceTextures.put(Others.QUEIXO, processing
				.loadImage("resources/models/face/texturas/cabeca3.jpg"));
	}

	/**
	 * Retorna o modelo de acordo com a configuração e lado especificados. Antes
	 * de poder usar esta função, loadModels deve ser executado uma vez.
	 * 
	 * @param shape
	 *            configuração de mão
	 * @param side
	 *            qual das mãos
	 * @return modelo carregado a partir de arquivo obj
	 */
	public static AnimObj getHandModel(HandShape shape, HandSide side) {
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
	 * 
	 * @param other
	 * @return correspondent texture; If not found, returns some default;
	 */
	public static PImage getFaceTexture(Others other) {
		PImage texture = faceTextures.get(other);
		return texture != null ? texture : faceTextures
				.get(Others.EXPRESSAO_RADIANTE);
	}

}
