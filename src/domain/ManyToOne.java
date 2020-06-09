package domain;

public class ManyToOne {

	private String name;
	private String classe;
	private String coluna;

	public ManyToOne() {}
	
	public ManyToOne(String name, String coluna, String classe) {
		this.name = name;
		this.coluna = coluna;
		this.classe = classe;
	}
	
	public String getName() {
		return name;
	}

	public String getClasse() {
		return classe;
	}

	public String getColuna() {
		return coluna;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setColuna(String coluna) {
		this.coluna = coluna;
	}

}
