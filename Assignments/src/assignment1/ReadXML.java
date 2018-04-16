package assignment1;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ReadXML{	
	//*** EXTRACT PROFILE NODES INTO ARRAY LISTS FOR EASIER MANIPULATION ***
	public static NodeList ToNodeList(String filename) {	
		NodeList sublist = null;
		try {	
			File XmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);	
			doc.getDocumentElement().normalize(); //Normalize CIM XML file.
			sublist = doc.getElementsByTagName("*"); //Get all tags.
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sublist;
	}
}
