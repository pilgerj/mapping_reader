package reader;

import java.io.File;
import java.lang.reflect.Field;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import domain.IdMapeamento;
import domain.ManyToOne;
import domain.Mapeamento;

public class LeitorXMLSax {
	
	private SAXParserFactory factory;
	private SAXParser saxParser;


	public LeitorXMLSax() {
		factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mapeamento lerXml(File file) {
		Mapeamento map = new Mapeamento();
		// Aqui você informa o nome do arquivo XML.
		map.setFileName(file.getName());
		map.setCaminho(file.getPath());

		try {
			 saxParser.parse(file, new XMLHandler(map));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {}
		return map;
	}


	class XMLHandler extends DefaultHandler {
		String packageName = null;
		Class<?> classeMapeada = null;
		String idName = null;
		String idColumn = null;
		Mapeamento map;

		public XMLHandler(Mapeamento map) {
			this.map = map;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (qName.equals("hibernate-mapping")) {
				packageName = attributes.getValue("package");
			} else if (qName.equals("class")) {
				map.setClasse(attributes.getValue("name"));
				map.setTabela(attributes.getValue("table"));
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
			} else if (qName.equals("id")) {
				idName = attributes.getValue("name");
				idColumn = attributes.getValue("column");
			} else if (qName.equals("column") && idName != null) {
				idColumn = attributes.getValue("name");
			} else if (qName.equals("many-to-one")) {
				ManyToOne manyToOne = new ManyToOne(attributes.getValue("name"), attributes.getValue("column"),
						attributes.getValue("class"));
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
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("id")) {
				map.setIdMap(new IdMapeamento(idName, idColumn));
				idName = null;
				idColumn = null;
			}
		}
	}

	// /home/joaosantos/eclipse/J2EE_Servidor/Hibernate/mappings/ValorFluxo.hbm.xml

}