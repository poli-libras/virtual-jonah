package br.usp.libras.jonah.input;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import br.usp.libras.sign.Sign;
import br.usp.libras.xml.XMLParser;

public class SignsForTest {

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
			URL uri = SignsForTest.class.getClassLoader().getResource(XML_FILE_PATH);
			File file = new File(uri.getFile());
			FileReader reader = new FileReader(file);
			return XMLParser.parseXML(reader);
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
