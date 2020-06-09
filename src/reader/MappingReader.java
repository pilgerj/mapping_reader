package reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import domain.Mapeamento;

public class MappingReader {
	
	private int count = 0;
	
	public Map<String, Mapeamento> readMappings(String path) throws IOException {
		LeitorXMLJdom leitor = new LeitorXMLJdom();
		Map<String, Mapeamento> mapeamentos = new HashMap<>();
		Files.newDirectoryStream(new File(path).toPath(), "*.hbm.xml").forEach(p -> {
			try {
				count++;
				Mapeamento mapeamento = leitor.lerXml(p.toFile());
				mapeamentos.put(mapeamento.getClasse(), mapeamento);
				if (count % 10 == 0) {
					System.out.println(String.format("%s arquivos lidos", String.valueOf(count)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return mapeamentos;
	}

	public static MappingReader create() {
		return new MappingReader();
	}

}
