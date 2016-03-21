package br.usp.libras.jonah.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.usp.libras.sign.Sign;
import br.usp.libras.sign.symbol.Hand;
import br.usp.libras.sign.symbol.HandShape;
import br.usp.libras.sign.symbol.HandSide;
import br.usp.libras.sign.symbol.Symbol;

public class ShapesForTest {

	private static final String FILE_NAME = "resources/input/shapes.txt";

	public List<Sign> getSignsWithListedShapes() {
		List<Sign> signs = new ArrayList<Sign>();
		List<String> confNames = getConfNames();
		for (String confName: confNames) {
			Sign sign = new Sign();
			sign.setName(confName.toUpperCase());
			Symbol symbol = new Symbol();
			Hand rightHand = new Hand();
			rightHand.setSide(HandSide.RIGHT);
			rightHand.setShape(HandShape.valueOf(confName.toUpperCase()));
			Hand leftHand = new Hand();
			leftHand.setSide(HandSide.LEFT);
			leftHand.setShape(HandShape.valueOf(confName.toUpperCase()));
			symbol.setRightHand(rightHand);
			symbol.setLeftHand(leftHand);
			sign.addSymbol(symbol);
			signs.add(sign);
		}
		return signs;
	}

	private List<String> getConfNames() {
		URL uri = this.getClass().getClassLoader().getResource(FILE_NAME);
		File file = new File(uri.getFile());
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.ready()) {
				String line = reader.readLine(); 
				if (!line.startsWith("#") && !line.isEmpty())
					lines.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(FILE_NAME + " not found! Should never happen!");
		} catch (IOException e) {
			System.out.println("Could not properly read from" + FILE_NAME);
		}
		return lines;
	}
}
