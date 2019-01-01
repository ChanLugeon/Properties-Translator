package io.github.chanlugeon.proptrans.cui;

import java.io.IOException;
import java.util.Scanner;

import io.github.chanlugeon.proptrans.generator.PropertiesManager;
import io.github.chanlugeon.proptrans.generator.ProtectedWord;
import io.github.chanlugeon.proptrans.generator.ProtectedWord.Type;
import io.github.chanlugeon.proptrans.generator.Splitter;
import io.github.chanlugeon.proptrans.translator.Language;

public abstract class CuiHandler {
	public static void run() throws IOException {
		Scanner scan = new Scanner(System.in);
		Language[] langs = Language.values();
		
		while (true) {
			System.out.println("Input an absolute path to a properties file ends with \"_en\". (e.g. C:/dir/file_en.properties)");
			Splitter splitter = new Splitter(scan.nextLine());
			ProtectedWord pw = new ProtectedWord();
			pw.put("Apple", Type.STRING);
			
			PropertiesManager pm = new PropertiesManager(splitter, pw);
			Thread[] thread = new Thread[langs.length];
			System.out.println("Please wait...");
			
			for (int i = 1; i < langs.length; i++) {
				Language lang = langs[i];
				thread[i] = new Thread(() -> {
					try {
						pm.generate(lang);
						System.out.println(lang + " FINISHED");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				thread[i].start();
				
			}
			for (int i = 1; i < langs.length; i++) {
				try {
					thread[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Finished.");
		}
	}
}
