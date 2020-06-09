package reader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import domain.IdMapeamento;
import domain.ManyToOne;
import domain.Mapeamento;

public class LeitorXMLJdom {

	public Mapeamento lerXml(File file) throws JDOMException, IOException {
		Mapeamento map = new Mapeamento();
		// Aqui você informa o nome do arquivo XML.
		map.setFileName(file.getName());
		map.setCaminho(file.getPath());

		// Criamos uma classe SAXBuilder que vai processar o XML4
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = sb.build(file);
		
		// Recuperamos o elemento root
		Element mural = d.getRootElement();
		for (Element element : mural.getChildren()) {
			if ("class".equals(element.getName())) {
				map.setClasse(element.getAttributeValue("name"));
				map.setTabela(element.getAttributeValue("table"));
				Class<?> classeMapeada = null;
				try {
					classeMapeada = Class.forName(map.getClasse());
				} catch (ClassNotFoundException e) {
					map.setClasse(buscaColunaPackage(d)+"."+map.getClasse());
					try {
						classeMapeada = Class.forName(map.getClasse());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				for (Element child : element.getChildren()) {
					String columnName = child.getAttributeValue("column");
					if ("id".equals(child.getName())) {
						if (columnName == null) {
							columnName = buscaColunaNodoFilho(child);
						}
						map.setIdMap(new IdMapeamento(child.getAttributeValue("name"), columnName));
					}
					if ("many-to-one".equals(child.getName())) {
						ManyToOne manyToOne = new ManyToOne(child.getAttributeValue("name"), columnName,
								child.getAttributeValue("class"));

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
			}
		}
		return map;
	}

	private String buscaColunaNodoFilho(Element child) {
		Element column = child.getChild("column");
		if (column != null) {
			return column.getAttributeValue("name");
		}
		return null;
	}

	private String buscaColunaPackage(Document d) {
		Element mural = d.getRootElement();
		return mural.getAttributeValue("package");
	}

	// /home/joaosantos/eclipse/J2EE_Servidor/Hibernate/mappings/ValorFluxo.hbm.xml

}