package domain;

public class IdMapeamento {
	private String nome;
	private String coluna;

	public IdMapeamento() {
	}

	public IdMapeamento(String nome, String coluna) {
		this.nome = nome;
		this.coluna = coluna;
	}

	public String getColuna() {
		return coluna == null ? null : coluna.toLowerCase();
	}

	public String getNome() {
		return nome.toLowerCase();
	}

	public void setColuna(String coluna) {
		this.coluna = coluna;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
