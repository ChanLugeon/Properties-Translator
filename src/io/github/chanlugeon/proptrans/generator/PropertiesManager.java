package io.github.chanlugeon.proptrans.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import io.github.chanlugeon.proptrans.translator.Language;
import io.github.chanlugeon.proptrans.translator.Translator;

public class PropertiesManager {
	private final Splitter PATH;
	private final List<String> properties;
	private final ProtectedWord protWord;
	
	public PropertiesManager(Splitter path, ProtectedWord protWord) throws IOException {
		PATH = path;
		this.protWord = protWord;
		
		properties = new LinkedList<String>();
		BufferedReader br = new BufferedReader(new FileReader(path.ORIGIN));
		
		String line = "", readLine;
		while ((readLine = br.readLine()) != null) {
			String add = null;
			
			// To support \ to combine lines
			if (readLine.isEmpty()) {
				add = (line.isEmpty() ? readLine : line);
			} else if (readLine.charAt(readLine.length() - 1) == '\\') { // delete \
				line += readLine;
				continue;
			} else {
				add = (line.isEmpty() ? readLine : line);	
			}
			
			properties.add(protWord.protect(add));
		}
		
		System.out.println(path.ORIGIN + properties);
	}
	
	public PropertiesManager generate(Language lang) throws IOException {
		String translated = protWord.unprotect(translate(lang));
		
		try (
				BufferedWriter bw = new BufferedWriter(Files.newBufferedWriter(Paths.get(PATH.DIR + "/" + PATH.BUNDLE_NAME
						+ "_" + lang.languageTag() + PATH.EXTENSION), StandardCharsets.UTF_8))
		) {
			String[] properties = translated.split("\n"); // return list?
			for (int i = 0, len = properties.length; i < len; i++) {
				bw.write(properties[i]);
				if (i < len - 1) bw.newLine();
			}
		}
		
		return this;
	}
	
	// TODO private
	private String translate(Language lang) throws IOException {
		Translator translator = Translator.create(lang);
		System.out.println(lang + "-" + lang.languageTag());
		
		// TODO StringBuilder
		String result = "";
		for (String property: properties) {
			
			result += translator.translateProperty(property) + "\n";
		}
		
		return result.substring(0, result.length() - 1);
	}
}
