package assignment1;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ReadXML{	
	public static NodeList ToNodeList(String filename) {	
		NodeList sublist = null;
		try {	
			File XmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);	
			doc.getDocumentElement().normalize(); // normalize CIM XML file
			sublist = doc.getElementsByTagName("*");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sublist;
	}
}
