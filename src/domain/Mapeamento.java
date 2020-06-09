package domain;

import java.util.ArrayList;
import java.util.List;

public class Mapeamento {

	private String fileName;
	private String classe;
	private String tabela;
	private String caminho;
	private IdMapeamento idMap;
	private List<ManyToOne> referencias;

	public Mapeamento() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getTabela() {
		return tabela.toLowerCase();
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public IdMapeamento getIdMap() {
		return idMap;
	}

	public void setIdMap(IdMapeamento idMap) {
		this.idMap = idMap;
	}
	
	@Override
	public String toString() {
		return	"\n_______________________________\n"
			+	"Arquivo: " + fileName 			+ "\n"
			+	"ID: " 		+ idMap.getNome().toLowerCase()	+ "\n"
			+	"Coluna ID: "+idMap.getColuna()	+ "\n"
			+	"Classe: " 	+ classe			+ "\n"
			+	"Tabela: " 	+ tabela.toLowerCase()			+ "\n"
			+	"Caminho: "	+ caminho			+ "\n"
			+	"_____________________________\n";
	}

	public void addReferencia(ManyToOne manyToOne) {
		getReferencias().add(manyToOne);
	}

	public List<ManyToOne> getReferencias() {
		if (referencias == null) {
			referencias = new ArrayList<>();
		}
		return referencias;
	}
	
}
