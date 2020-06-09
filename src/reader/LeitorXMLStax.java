package reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import domain.IdMapeamento;
import domain.ManyToOne;
import domain.Mapeamento;

public class LeitorXMLStax {

	public Mapeamento lerXml(File file) throws IOException {
		Mapeamento map = new Mapeamento();
		// Aqui você informa o nome do arquivo XML.
		map.setFileName(file.getName());
		map.setCaminho(file.getPath());

		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));
            String packageName = null;
            Class<?> classeMapeada = null;
            String idName = null;
            String idColumn = null;
            
            while(xmlEventReader.hasNext()){
            	 XMLEvent xmlEvent = xmlEventReader.nextEvent();
                 if (xmlEvent.isStartElement()){
                	 StartElement startElement = xmlEvent.asStartElement();
                	 if (startElement.getName().getLocalPart().equals("hibernate-mapping")) {
                		 Attribute packageAttribute = startElement.getAttributeByName(new QName("package"));
                		 if (packageAttribute != null) {
                			 packageName = getValue(packageAttribute);
                		 }
                	 } else if (startElement.getName().getLocalPart().equals("class")) {
                		 Attribute name = startElement.getAttributeByName(new QName("name"));
                		 map.setClasse(getValue(name));
                		 Attribute table = startElement.getAttributeByName(new QName("table"));
                		 map.setTabela(getValue(table));
                		 try {
                			 classeMapeada = Class.forName(map.getClasse());
                		 } catch (ClassNotFoundException e) {
                			 map.setClasse(packageName+"."+map.getClasse());
                			 try {
                				 classeMapeada = Class.forName(map.getClasse());
                			 } catch (ClassNotFoundException e1) {
                				 e1.printStackTrace();
                			 }
                		 }
                	 } else if (startElement.getName().getLocalPart().equals("id")) {
                		 Attribute columnAttribute = startElement.getAttributeByName(new QName("column"));
                		 Attribute nameAttribute = startElement.getAttributeByName(new QName("name"));
                		 idName = getValue(nameAttribute);
                		 idColumn = getValue(columnAttribute);
                	 } else if (startElement.getName().getLocalPart().equals("column") && idName != null) {
                		 Attribute nameAttribute = startElement.getAttributeByName(new QName("name"));
                		 idColumn = getValue(nameAttribute);
                	 } else if (startElement.getName().getLocalPart().equals("many-to-one")) {
                		 Attribute columnAttribute = startElement.getAttributeByName(new QName("column"));
                		 Attribute nameAttribute = startElement.getAttributeByName(new QName("name"));
                		 Attribute classAttribute = startElement.getAttributeByName(new QName("class"));
                		 
                		 ManyToOne manyToOne = new ManyToOne(getValue(nameAttribute), getValue(columnAttribute),
 								getValue(classAttribute));
 						if (manyToOne.getClasse() == null && classeMapeada != null) {
							try {
								Field field = classeMapeada.getDeclaredField(manyToOne.getName());
								manyToOne.setClasse(field.getType().getSimpleName());
							} catch (Exception e) {
								System.out.println(map.getClasse() + ": Missing field - " + manyToOne.getName());
								e.printStackTrace();
							}
						}
						map.addReferencia(manyToOne);
                	 }
                 } else if (xmlEvent.isEndElement()) {
                	 EndElement endElement = xmlEvent.asEndElement();
                	 if (endElement.getName().getLocalPart().equals("id")) {
                		 map.setIdMap(new IdMapeamento(idName, idColumn));
                		 idName = null;
                		 idColumn = null;
                	 }
                 }
            }
        } catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {}
        
		return map;
	}

	private String getValue(Attribute attribute) {
		if (attribute == null) return null;
		return attribute.getValue();
	}

	// /home/joaosantos/eclipse/J2EE_Servidor/Hibernate/mappings/ValorFluxo.hbm.xml

}