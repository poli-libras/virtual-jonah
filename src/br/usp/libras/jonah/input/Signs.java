package br.usp.libras.jonah.input;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import br.usp.libras.sign.Sign;
import br.usp.libras.xml.SignXMLParser;

public class Signs {

	private static final String XML_FILE_PATH = "resources/input/signs.xml";

	public static Sign getSignByName(String name) {
		return getSignsByName().get(name);
	}

	public static Map<String, Sign> getSignsByName() {
		List<Sign> signs = getSigns();
		Map<String, Sign> signsByName = signs.stream().collect(Collectors.toMap(Sign::getName, s -> s));
		return signsByName;
	}

	public static List<Sign> getSigns() {
		try {
			URL uri = Signs.class.getClassLoader().getResource(XML_FILE_PATH);
			File file = new File(uri.getFile());
			FileReader reader = new FileReader(file);
			return SignXMLParser.parseXML(reader);
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static List<Sign> getTestSuitSigns() {
		Map<String, Sign> signsByName = getSignsByName();
		List<Sign> signs = new ArrayList<>();
		
		// rotY, duas mãos
		signs.add(signsByName.get("BOLA"));
		
		// soletração, expressão facial
		signs.add(signsByName.get("2016"));

		// mão na diagonal
		signs.add(signsByName.get("CASA"));

		// rotY, rotX, locação, expressão facial
		signs.add(signsByName.get("JUIZ"));
		
		// rotY, rotZ, dois símbolos
		signs.add(signsByName.get("ABERTURA"));

		// movimento para frente
		signs.add(signsByName.get("OLHAR"));

		// movimento para cima
		signs.add(signsByName.get("FESTA"));

		// mãos fazem coisas diferentes
		signs.add(signsByName.get("LIMITE"));
		
		// movimento com vários segmentos, movimento na diagonal, movimento rápido, magnitude curta
		signs.add(signsByName.get("TORCIDA"));
		
		return signs;
	}
}
