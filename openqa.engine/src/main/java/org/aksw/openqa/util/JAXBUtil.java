package org.aksw.openqa.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class JAXBUtil {

	public static <E> E deserialize(Class<E> clazz, File file) throws JAXBException, FileNotFoundException, UnsupportedEncodingException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		@SuppressWarnings("unchecked")
		E instance = (E) jaxbUnmarshaller.unmarshal(file);
		return instance;
	}
	
	public static <E> void serialize(Class<E> clazz, E instance, OutputStream out, String cdataElements) throws Exception {
		// Generating context uses resources - do it once and reuse across threads
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

		// Marshaller is not thread-safe
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// Create an empty DOM document
		// DocumentBuilderFactory is not thread-safe
		DocumentBuilderFactory docBuilderFactory = 
		    DocumentBuilderFactory.newInstance();
		Document document = 
		    docBuilderFactory.newDocumentBuilder().newDocument();

		// Marshall the feed object into the empty document.
		jaxbMarshaller.marshal(instance, document);

		// Transform the DOM to the output stream
		// TransformerFactory is not thread-safe
		TransformerFactory transformerFactory = 
		    TransformerFactory.newInstance();
		Transformer nullTransformer = transformerFactory.newTransformer();
		nullTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
		nullTransformer.setOutputProperty(
		    OutputKeys.CDATA_SECTION_ELEMENTS,
		    cdataElements);
		nullTransformer.transform(new DOMSource(document),
		     new StreamResult(out));
	 }

}
