package reader;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class LeitorTxt {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Digite o caminho do arquivo base para ser comparado");
		String path1 = scan.next();
		File txt1 = new File(path1);
		System.out.println("Digite o caminho do arquivo a ser comparado");
		String path2 = scan.next();
		File txt2 = new File(path2);
		scan.close();

		try {
			List<String> list1 = Files.readAllLines(txt1.toPath());
			List<String> list2 = Files.readAllLines(txt2.toPath());
			
//			for (Iterator<String> it = list1.iterator(); it.hasNext();) {
//				String key = it.next();
//				System.out.println(list1.size());
//				if (list2.contains(key)) {
//					it.remove();
//				}
//			}
			list1.removeAll(list2);
			list1.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
// txt2: /home/joaosantos/result_SAX [ mais resultados ]
