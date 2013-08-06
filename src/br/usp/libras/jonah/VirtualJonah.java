package br.usp.libras.jonah;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import processing.core.PApplet;
import processing.core.PFont;
import br.usp.libras.sign.Sign;
import br.usp.libras.sign.face.Face;
import br.usp.libras.sign.symbol.Hand;
import br.usp.libras.sign.symbol.HandOrientation;
import br.usp.libras.sign.symbol.HandPlane;
import br.usp.libras.sign.symbol.HandShape;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Location;
import br.usp.libras.sign.symbol.Symbol;
import br.usp.libras.xml.XMLParser;

/**
 * Esta classe é a aplicação, o sketch do Processing, que chama o parser do XML
 * e responde aos comandos do usuário.
 * 
 * @author Leonardo Leite
 * @author Marcelo Li Koga
 * 
 */
public class VirtualJonah extends PApplet {

	private static final long serialVersionUID = 1L;

	private SymbolGraph symbolGraph;

	private int signIndex; // indica passo da sequência carregada do arquivo
	private int symbolIndex; // indica qual simbolo atual dentro do sinal

	private List<Sign> signs = new ArrayList<Sign>();;

	// rotação do modelo controlada interativamente:
	private float rotX = 0;
	private float rotY = 0;
	private float rotZ = 0;

	private boolean playing;
	private PFont font;
	private String signName;

	private boolean initialized = false;
	private boolean initializing = false;

	/**
	 * Esse método é chamado pelo processing no começo da execução.
	 */
	public void setup() {
		size(800, 600, P3D);
		perspective(PI / 4, 1.0f * width / height, 0.1f, 1500);
		frameRate(20);
		setupText();
	}

	private void setupText() {
		// font = loadFont("resources/fonts/SansSerif.bold-48.vlw");
		font = createFont("Arial", 48);
		textFont(font, 48);
		textMode(SCREEN);
		textAlign(CENTER);
	}

	/**
	 * Esse método é chamado pelo processing a cada frame.
	 */
	public void draw() {
		if (!this.initialized && !this.initializing) {
			startInitialization();
		} else if (this.initializing) {
			loadModels();
		} else {
			drawWhenInitialized();
		}
	}

	private void startInitialization() {
		text("Carregando...Aguarde.", this.width / 2, this.height / 2);
		this.initializing = true;
	}

	/**
	 * Movimento do mouse e teclas de câmera (w, s, a, d, q, e)
	 */
	private void rotateModel() {
		rotateX(rotX);
		rotateY(rotY);
		rotateZ(rotZ);
	}

	/**
	 * Desenha eixos coordenados (x,y)
	 */
	private void drawAxis() {
		background(150, 150, 150);
		beginShape(LINES);
		stroke(0, 255, 0); // verde - 0x
		vertex(-200, 0, 0);
		vertex(200, 0, 0);
		stroke(0, 0, 255); // azul - 0y
		vertex(0, -200, 0);
		vertex(0, 200, 0);
		endShape(LINES);
		noStroke();
	}
	
	private void drawWhenInitialized() {
		lights();
		pushMatrix();
		translate(width / 2, 0.6f * height, -300);
		rotateModel();
		drawAxis();
		if (playing && this.symbolGraph.hasEnded()) {
			goToNextSymbol();
		}
		// renderiza sinal (composto de modelos obj)
		this.symbolGraph.draw();
		popMatrix();
		// Desenha na tela
		fill(255);
		text(signName, width / 2, height - 50);
	}	

	/**
	 * Loads all models. May take a while.
	 */
	private void loadModels() {
		// carrega objs
		ModelsLoader.loadModels(this);
		LocationsLoader.loadLocations();

		// mão inicial deve ser sempre esta:
		Hand lh = new Hand(HandSide.LEFT, HandShape.INDICADOR,
				HandOrientation.BLACK, HandPlane.VERTICAL, null, null);
		Hand rh = new Hand(HandSide.RIGHT, HandShape.MAO_2,
				HandOrientation.BLACK, HandPlane.VERTICAL, null, null);
		Symbol initial = new Symbol(new Face(), lh, rh, Location.ESPACO_NEUTRO,
				null, 0, false);
		this.reset();
		this.symbolGraph = new SymbolGraph(this, initial);

		playing = true;
		signName = "";

		this.initialized = true;
		this.initializing = false;
	}

	/**
	 * Controle de rotação do modelo.
	 */
	public void mouseDragged() {
		rotX += (mouseX - pmouseX) * 0.01;
		rotY -= (mouseY - pmouseY) * 0.01;
	}

	/**
	 * Respostas de comandos do usuário
	 */
	public void keyPressed() {

		if (key == ' ') { 
			goToNextSymbol();
		}

		if ((key == 'y') || (key == 'Y')) {
			this.playSigns();
		}

		if ((key == 'x') || (key == 'X')) {
			this.loadLocalXML();
		}

		if ((key == 'z') || (key == 'Z')) {
			this.loadLocalSerialized();
		}

		// controle de câmera
		if (key == 'a')
			rotY += -0.1;
		if (key == 'd')
			rotY += 0.1;
		if (key == 'w')
			rotX += -0.1;
		if (key == 's')
			rotX += 0.1;
		if (key == 'q')
			rotZ += -0.1;
		if (key == 'e')
			rotZ += 0.1;
	}

	/**
	 * Carrega sinais diretamente dos objetos passados. Sinais anteriormente
	 * carregados são esquecidos.
	 * 
	 * @param list
	 */
	public void loadSignsFromObject(List<Sign> list) {
		this.signs.clear();
		this.signs.addAll(list);
	}

	/**
	 * Carrega sequência de sinais a partir de arquivo XML
	 * 
	 * @param link
	 *            link do arquivo de entrada em SML
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void loadSignsFromXML(String link) throws IOException, JAXBException {
		URL url = new URL(link);
		Reader reader = new InputStreamReader(url.openStream());
		this.signs = XMLParser.parseXML(reader);
		printSigns();
	}

	/**
	 * Carrega sinais a partir de arquivo contendo serialização de um array de
	 * sinais
	 * 
	 * @param link
	 *            link apontando para o aruivo.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadSignsFromSerialized(String link) throws IOException,
			ClassNotFoundException {
		System.out.println("Carregando " + link);
		URL url = new URL(link);
		ObjectInputStream objr = new ObjectInputStream(url.openStream());
		Sign[] retrieved = (Sign[]) objr.readObject();
		objr.close();
		this.signs = Arrays.asList(retrieved);
		printSigns();
	}

	/**
	 * Carrega sequência de sinais do arquivo "xml/signs.xml"
	 */
	protected void loadLocalXML() {
		try {
			String file = "resources/xml/signs.xml";
			this.signs = XMLParser.parseXMLFile(file);
			printSigns();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carrega sinais do arquivo serial/signs.txt
	 */
	protected void loadLocalSerialized() {
		try {
			String file = "resources/serial/signs.txt";
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream objr = new ObjectInputStream(fis);
			Sign[] retrieved = (Sign[]) objr.readObject();
			objr.close();
			fis.close();
			this.signs = Arrays.asList(retrieved);
			printSigns();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Plays the animation of the signs
	 */
	public void playSigns() {
		this.reset();
		goToNextSymbol();
		playing = true;
	}

	private void printSigns() {
		System.out.println("Sinais carregados:");
		if (this.signs != null) {
			for (Sign s : this.signs) {
				System.out.println(s.getName());
			}
		}
	}

	protected void reset() {
		signIndex = 0;
		symbolIndex = 0;
	}

	protected void setSignName(String name) {
		this.signName = name;
	}

	protected List<Sign> getSigns() {
		return Collections.unmodifiableList(this.signs);
	}

	protected void goToNextSymbol() {
		if (this.signs.size() > 0) {
			Sign s = this.signs.get(signIndex);
			signName = s.getName();
			List<Symbol> symbols = s.getSymbols();
			Symbol symbol = symbols.get(symbolIndex);
			this.symbolGraph.nextSymbol(symbol);

			if (symbolIndex < symbols.size() - 1) {
				symbolIndex++;
			} else {
				symbolIndex = 0;
				if (signIndex < signs.size() - 1) {
					signIndex++;
				} else {
					reset();
					playing = false;
				}
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "br.usp.libras.jonah.VirtualJonah" });
	}
}
