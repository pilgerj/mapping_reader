package reader;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.ForeignKeyMetadata;
import domain.ManyToOne;
import domain.Mapeamento;

public class BuscaReferencias {
	private boolean lowercase;
	
	public BuscaReferencias(boolean lowercase) {
		this.lowercase = lowercase;
	}

	public void buscarChaves(DatabaseMetaData dbMetaData, Map<String, Mapeamento> mapeamentos)
			throws Exception {
		for (Mapeamento mapeamento : mapeamentos.values()) {
			String tableOwner = lowercase ? mapeamento.getTabela().toLowerCase() : mapeamento.getTabela().toUpperCase();
			List<ForeignKeyMetadata> colunasFk = new ArrayList<>();

			try (ResultSet rs = dbMetaData.getImportedKeys(null, null, tableOwner)) {
				while (rs.next()) {
					colunasFk.add(new ForeignKeyMetadata(rs.getString("FKTABLE_NAME"), rs.getString("FKCOLUMN_NAME"),
							rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME")));
				}
			} 
			for (ManyToOne manyToOne : mapeamento.getReferencias()) {
				try {
					Mapeamento mapeamentoReferencia = mapeamentos.get(manyToOne.getClasse());
					String columnOwner = manyToOne.getColuna();

					if (mapeamentoReferencia != null) {
						String targetTable = mapeamentoReferencia.getTabela();
						String targetColumn = mapeamentoReferencia.getIdMap().getColuna();
						boolean encontrouReferencia = false;
						for (ForeignKeyMetadata fk : colunasFk) {
							if (fk.isSame(tableOwner, columnOwner, targetTable, targetColumn)) {
								encontrouReferencia = true;
							}
						}
						if (!encontrouReferencia) {
							String constraintName = tableOwner.substring(0, 4) + "_" + targetTable.substring(0, 4) + "_FK";
							System.out.println(
									String.format("alter table %s add constraint %s foreign key (%s) references %s (%s)",
											tableOwner, constraintName, columnOwner, targetTable, targetColumn));
						}
					}
				} catch (Exception e) {
					System.out.println(String.format("Erro ocorrido durante execucao da tabela %s no many-to-one da propriedade %s ", tableOwner, manyToOne.getColuna()));
					e.printStackTrace();
				}
			}
		}

	}
}
