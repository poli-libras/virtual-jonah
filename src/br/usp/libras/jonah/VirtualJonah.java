package br.usp.libras.jonah;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import br.usp.libras.jonah.input.Shapes;
import br.usp.libras.jonah.input.Signs;
import br.usp.libras.sign.Sign;
import br.usp.libras.sign.symbol.Hand;
// import br.usp.libras.sign.symbol.HandOrientation;
// import br.usp.libras.sign.symbol.HandPlane;
import br.usp.libras.sign.symbol.HandShape;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Symbol;
import br.usp.libras.xml.SignXMLParser;
import processing.core.PApplet;
import processing.core.PFont;

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

	private boolean pressN = false;
	private boolean playingSign = false;
	private boolean playing = false;
	private PFont font;
	private String signName = "";
	private boolean showSignName = true;

	private boolean initialized = false;
	private boolean initializing = false;

	/**
	 * Esse método é chamado pelo processing no começo da execução.
	 */
	public void setup() {
		size(1120, 700, P3D);
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
		background(255, 238, 116);
		// beginShape(LINES);
		// stroke(0, 255, 0); // verde - 0x
		// vertex(-200, 0, 0);
		// vertex(200, 0, 0);
		// stroke(0, 0, 255); // azul - 0y
		// vertex(0, -200, 0);
		// vertex(0, 200, 0);
		// endShape(LINES);
		noStroke();
	}

	private void drawWhenInitialized() {
		lights();
		pushMatrix();
		translate(width / 2, 0.6f * height, -200);
		rotateModel();
		drawAxis();
		if (playing && this.symbolGraph.hasTransitionEnded()) {
			goToNextSymbol();
		}
		// Caso payingSign seja true e sign atual tenha symbols restantes, deve
		// chamar goToNextSymbol automaticamnte
		// if (this.signs.size() > 0) {
		// Sign signAtual = this.signs.get(signIndex);
		if (playingSign && this.symbolGraph.hasTransitionEnded()) {
			// if (symbolIndex < signAtual.getSymbols().size() - 1) {
			goToNextSymbol();
			// }
			// else {
			// playingSign = false;
			// }
		}
		// }

		// renderiza sinal (composto de modelos obj)
		this.symbolGraph.draw();
		popMatrix();
		// Desenha na tela
		fill(255);
		if (showSignName) {
			text(signName, width / 2, height - 50);
		}
	}

	/**
	 * Loads all models. May take a while.
	 */
	private void loadModels() {

		// carrega objs
		ModelsLoader.loadModels(this);
		LocationsLoader.loadLocations();

		// mão inicial deve ser sempre esta:
		Hand leftHand = new Hand();
		leftHand.setShape(HandShape.MAO_2);
		leftHand.setSide(HandSide.LEFT);
		Hand rightHand = new Hand();
		rightHand.setShape(HandShape.MAO_2);
		rightHand.setSide(HandSide.RIGHT);
		Symbol initialSymbol = new Symbol();
		initialSymbol.setLeftHand(leftHand);
		initialSymbol.setRightHand(rightHand);

		this.reset();
		this.symbolGraph = new SymbolGraph(this, initialSymbol);
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

		if (key == 'n') {
			goToNextSign();
		}

		if (key == 'l') {
			goToPreviousSymbol();
		}

		if (key == ' ') {
			goToNextSymbol();
		}

		if ((key == 'y') || (key == 'Y')) {
			this.playSigns();
		}

		if ((key == 'x') || (key == 'X')) {
			this.loadLocalXML();
		}

		if ((key == 'c') || (key == 'C')) {
			this.loadHandShapesFromFile();
		}
		
		if ((key == 't') || (key == 'T')) {
			this.loadTestSuit();
		}
		
		if (key == 's') {
			this.loadSignByName();
		}

		if (key == 'S') {
			this.loadHandSpock();
		}

		if (key == 'r' || key == 'R') {
			this.resetHands();
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

	protected void resetHands() {
		// mão inicial deve ser sempre esta:
		Symbol symbol = new Symbol();
		if (this.signs.size() > 0) {
			Sign s = this.signs.get(signIndex);
			List<Symbol> symbols = s.getSymbols();
			Symbol nextSymbol = symbols.get(symbolIndex);
			if (nextSymbol.getLeftHand() != null) {
				Hand leftHand = new Hand();
				leftHand.setShape(HandShape.MAO_A);
				leftHand.setSide(HandSide.LEFT);
				leftHand.setRotY((float) -1.570796);
				symbol.setLeftHand(leftHand);
			}
		}
		Hand rightHand = new Hand();
		rightHand.setShape(HandShape.MAO_A);
		rightHand.setSide(HandSide.RIGHT);
		rightHand.setRotY((float) 1.570796);
		symbol.setRightHand(rightHand);
		symbolGraph.nextSymbol(symbol);
	}

	private void loadHandSpock() {
		this.symbolGraph.spock();
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
		this.signs = SignXMLParser.parseXML(reader);
		printSigns();
	}

	/**
	 * Carrega sequência de sinais a partir de string XML
	 * 
	 * @param xml
	 * @throws JAXBException
	 */
	public void loadSignsFromXMLString(String xml) throws JAXBException {
		Reader reader = new StringReader(xml);
		this.signs = SignXMLParser.parseXML(reader);
		printSigns();
	}

	private void loadLocalXML() {
		this.signs = Signs.getSigns();
		printSigns();
	}

	private void loadTestSuit() {
		this.signs = Signs.getTestSuitSigns();
		printSigns();
	}
	
	private void loadHandShapesFromFile() {
		Shapes shapes = new Shapes();
		this.signs = shapes.getSignsWithListedShapes();
		printSigns();
	}

	/**
	 * Usuário deve escrever nome do sinal no console. Para facilitar os testes.
	 */
	private void loadSignByName() {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Digite o nome do sinal: ");
		String signName = keyboard.next();
		Sign sign = Signs.getSignByName(signName);
		if (sign == null) {
			System.out.println("Sinal não encontrado.");
		} else {
			this.signs = Collections.singletonList(sign);
			System.out.println("Sinal carregado.");
		}
		keyboard.close();
	}

	/**
	 * Plays the animation of the signs
	 */
	public void playSigns() {
		this.reset();
		playing = true;
		goToNextSymbol();
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

			if (pressN == true) {
				playingSign = true;
			}

			if (symbolIndex < symbols.size() - 1) {
				symbolIndex++;
			} else {
				playingSign = false;
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

	protected void goToPreviousSymbol() {
		if (this.signs.size() > 0) {
			if (symbolIndex > 0) {
				symbolIndex--;
			} else {
				if (signIndex > 0) {
					signIndex--;
					symbolIndex = this.signs.get(signIndex).getSymbols().size() - 1;
				}
			}
			Sign s = this.signs.get(signIndex);
			signName = s.getName();
			List<Symbol> symbols = s.getSymbols();
			Symbol symbol = symbols.get(symbolIndex);
			this.symbolGraph.nextSymbol(symbol);

		}
	}

	public void goToNextSign() {
		pressN = true;
		goToNextSymbol();
		pressN = false;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "br.usp.libras.jonah.VirtualJonah" });
	}
}
