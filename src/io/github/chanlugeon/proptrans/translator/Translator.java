package io.github.chanlugeon.proptrans.translator;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Translator {
	// Language tags
	/*public static final String ENGLISH = "en",
			KOREAN = "ko",
			JAPANESE = "ja",
			CHINESE = "zh",
			VIETNAMESE = "vi",
			INDONESIAN = "id",
			ARABIC = "ar",
			GERMAN = "de",
			FRENCH = "fr",
			RUSSIAN = "ru",
			SPANISH = "es",
			PORTUGUESE = "pt";*/
	
	private static final Pattern PROPERTY = Pattern.compile("^(?<key>.*\\S+\\s*[=:]\\s*)(?<value>.+)$");
	
	final Language lang;
	
	Translator(Language lang) {
		this.lang = lang;
	}
	
	public static Translator create(Language lang) {
		switch (lang) {
		case KOREAN: case JAPANESE: case CHINESE: case VIETNAMESE: case INDONESIAN:
			return new Kakao(lang);
		
		case ARABIC: case GERMAN: case FRENCH: case RUSSIAN: case SPANISH: case PORTUGUESE:
			return new GenieTalk(lang);
		
		default:
			throw new IllegalArgumentException(lang + " is not supported.");
		}
	}
	
	public abstract String translate(String content) throws IOException;
	
	public final String translateProperty(String line) throws IOException {
		Matcher m = PROPERTY.matcher(line);
		if (m.find()) {
			return m.group("key") + translate(m.group("value"));
		}
		
		return line;
	}
}
