package domain;

public class ForeignKeyMetadata {
	
	private String tableOwner;
	private String columnOwner;
	private String targetTable;
	private String targetColumn;
	
	public ForeignKeyMetadata(String tableOwner, String columnOwner, String targetTable, String targetColumn) {
		this.tableOwner = tableOwner;
		this.columnOwner = columnOwner;
		this.targetTable = targetTable;
		this.targetColumn = targetColumn;
	}

	public boolean isSame(String tableOwner, String columnOwner, String targetTable, String targetColumn) {
		return this.tableOwner.equalsIgnoreCase(tableOwner) 
				&& this.columnOwner.equalsIgnoreCase(columnOwner) 
				&& this.targetTable.equalsIgnoreCase(targetTable) 
				&& this.targetColumn.equalsIgnoreCase(targetColumn);
	}
	
	@Override
	public String toString() {
		return String.format("Tabela %s, Coluna %s nao possui FK em relacao a Tabela: %s na Coluna: %s", tableOwner, columnOwner, targetTable, targetColumn);
	}

}
