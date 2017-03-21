package main.process.convert;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

public class Converter {

	/**
	 * 
	 * @param jsonData
	 * @return
	 */
	public String JsonToXml(JSON jsonData)
	{		
		XMLSerializer serializer = new XMLSerializer(); 
        String xml = serializer.write(jsonData);
        
		return xml;
	}
}
