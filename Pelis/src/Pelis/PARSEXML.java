package Pelis;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PARSEXML {

	Visual visual = new Visual();

	private String id;

	/**
	 * 
	 * @param doc
	 * @return
	 */
	// Gets the online data to see it.
	public Visual getDataToSee(Document doc) {
		try {
			NodeList movieList = doc.getElementsByTagName("movie");
			Node p = movieList.item(0);

			if (p.getNodeType() == Node.ELEMENT_NODE) {
				Element movie = (Element) p;

				visual.setTitle(movie.getAttribute("title"));
				visual.setType(movie.getAttribute("type"));
				visual.setDate(movie.getAttribute("year"));
				visual.setLenght(movie.getAttribute("runtime"));
				visual.setGenre(movie.getAttribute("genre"));
				visual.setSynopsis(movie.getAttribute("plot"));
				visual.setLanguage(movie.getAttribute("language"));
				visual.setDirector(movie.getAttribute("director"));
				visual.setActors(movie.getAttribute("actors"));
			}

		} catch (NullPointerException e) {
			System.out.println("Error 404 Not Found.");
		}

		return visual;
	}

	/**
	 * 
	 * @param doc
	 * @param type
	 */
	// Gets the online data to write it.
	public void getDataToWrite(Document doc, String type, String name) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String loc = null;
		try {
			//2 Documents: doc to get the data online, and docu where we will write the data.
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document docu = builder.newDocument();
			Element root = docu.createElement(name.toLowerCase());
			docu.appendChild(root);

			NodeList movieList = doc.getElementsByTagName("movie");
			Node p = movieList.item(0);

			
			
			if (p.getNodeType() == Node.ELEMENT_NODE) {
				Element movie = (Element) p;
				root.setAttribute("title", movie.getAttribute("title"));
				root.setAttribute("type", movie.getAttribute("type"));
				root.setAttribute("year", movie.getAttribute("year"));
				root.setAttribute("runtime", movie.getAttribute("runtime"));
				root.setAttribute("genre", movie.getAttribute("genre"));
				root.setAttribute("plot", movie.getAttribute("plot"));
				root.setAttribute("language", movie.getAttribute("language"));
				root.setAttribute("actors", movie.getAttribute("actors"));
				root.setAttribute("director", movie.getAttribute("director"));
			}

			DOMSource source = new DOMSource(docu);

			if (type.equals("series")) {
				loc = "resources/series.xml";
			} else if (type.equals("movie")) {
				loc = "resources/movie.xml";
			}
			
			Result result = new StreamResult(loc);

			TransformerFactory transf = TransformerFactory.newInstance();
			Transformer transformer = transf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

			System.out.println("Archivo escrito.");

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param type
	 */
	// Reads the written data.
	public void readWrittenData(String type, String name) {
		String id;
		Document doc = null;
		// Defines a factory API that enables applications to obtain a parser
		// that produces DOM object trees from XML documents.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// Defines the API to obtain DOM Document instances from an XML
			// document. Using this class, an application programmer can obtain
			// a Document from XML.
			DocumentBuilder builder = factory.newDocumentBuilder();
			// It is the root of the document tree, and provides the primary
			// access to the document's data.
			if (type.equals("series")) {
				doc = builder.parse("resources/series.xml");
			} else if (type.equals("movie")) {
				doc = builder.parse("resources/movie.xml");
			}

			NodeList personList = doc.getElementsByTagName(name);
			for (int i = 0; i < personList.getLength(); i++) {
				Node p = personList.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element person = (Element) p;
					id = person.getAttribute("title");
					System.out.print("Title:  " + id + "");
					id = person.getAttribute("type");
					System.out.println(" (" + id + ").");
					id = person.getAttribute("year");
					System.out.println("Release date: " + id);
					id = person.getAttribute("runtime");
					System.out.println("Lenght: " + id);
					id = person.getAttribute("genre");
					System.out.println("Genre: " + id);
					id = person.getAttribute("plot");
					System.out.println("Synopsis: " + id);
					id = person.getAttribute("language");
					System.out.println("Language: " + id);
					id = person.getAttribute("actors");
					System.out.println("Actors: " + id);
					id = person.getAttribute("director");
					System.out.println("Director: " + id);

					System.out.println("Archivo leido.");
				}

			}

		} catch (ParserConfigurationException e) {
			System.out.println("Error DocumentBuilder.");
		} catch (SAXException e) {
			System.out.println("Error S.A.X.");
		} catch (IOException e) {
			System.out.println("Error I/O.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//Controllers.
		PARSEXML read = new PARSEXML();
		RESTAPI api = new RESTAPI();
		
		System.out.println("Look for online the series/ movie you want: ");
		Visual visual = read.getDataToSee(api.transformXML());
		System.out.println(visual.toString());
		
		
		System.out.println("Want to write it?");
		read.getDataToWrite(api.getDocu(), api.Type(), api.Name());
		
		
		
		System.out.println("Select if it´s a series/ movie and the id: ");
		read.readWrittenData(api.Type(), api.Name());
	}

}
