import java.sql.DatabaseMetaData;
import java.util.Map;

import data.Conexao;
import domain.Mapeamento;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import reader.BuscaReferencias;
import reader.MappingReader;

public class Main {

	public static void main(String[] args) throws Exception {
		long startedAt = System.currentTimeMillis();

		ArgumentParser parser = configureArgumentParser();
		msg();
		try {
			Namespace namespace = parser.parseArgs(args);
			boolean lowercase = namespace.getBoolean("lowercase") == null ? false : namespace.getBoolean("lowercase");
			
			Conexao bd = new Conexao(namespace.getString("urldb"), namespace.getString("userdb"),
					namespace.getString("passdb"), namespace.getString("driverdb"));
			bd.connect();
			
			BuscaReferencias buscar = new BuscaReferencias(lowercase);
			Map<String, Mapeamento> mapeamentos = MappingReader.create().readMappings(namespace.getString("path"));

			try {
				DatabaseMetaData dbMetaData = bd.getC().getMetaData();
				buscar.buscarChaves(dbMetaData, mapeamentos);
			} finally {
				bd.disconnect();
			}
		} catch (ArgumentParserException e) {
			parser.handleError(e);
		}

		long finishedAt = System.currentTimeMillis();
		System.out.println(String.format("Tempo total consumido: %s min", (finishedAt - startedAt) / 1000d / 60d));
	}

	private static ArgumentParser configureArgumentParser() {
		ArgumentParser parser = ArgumentParsers.newFor("MappingReader").build().description(
				"Ferramenta que compara o mapeamento Hibernate com as tabelas do BD para identificar FKs faltantes");
		parser.addArgument("path").metavar("Path");
		parser.addArgument("-l", "--lowercase").help("Caso o metadata do banco esteja em lowercase, ative essa opcao")
				.action(Arguments.storeTrue()).dest("lowercase");
		parser.addArgument("-db", "--urldb").help("Digite o endereco do banco de dados").action(Arguments.store())
				.dest("urldb");
		parser.addArgument("-usr", "--userdb").help("Usuario").action(Arguments.store()).dest("userdb");
		parser.addArgument("-ps", "--passdb").help("Senha").action(Arguments.store()).dest("passdb");
		parser.addArgument("-dv", "--driverdb").help("Driver").action(Arguments.store()).dest("driverdb");
		return parser;
	}
	
	private static void msg() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("M\\              //M"							);
		System.out.println("M \\            // M"							);
		System.out.println("M  \\          //  M"							);
		System.out.println("M   \\        //   M"							);
		System.out.println("M    \\      //    M"							);
		System.out.println("M     \\    //     M"							);
		System.out.println("M      \\  //      M"							);
		System.out.println("M       \\//       MAPPING READER"			    );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	}
}
