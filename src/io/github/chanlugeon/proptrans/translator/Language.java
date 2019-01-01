package io.github.chanlugeon.proptrans.translator;

public enum Language {
	ENGLISH("en"),
	KOREAN("ko"),
	JAPANESE("ja"),
	CHINESE("zh"),
	VIETNAMESE("vi"),
	INDONESIAN("id"),
	ARABIC("ar"),
	GERMAN("de"),
	FRENCH("fr"),
	RUSSIAN("ru"),
	SPANISH("es"),
	PORTUGUESE("pt");
	
	private final String langTag;
	
	Language(String langTag) {
		this.langTag = langTag;
	}
	
	@Override
	public String toString() {
		String name = name();
		return name.substring(0, 1) + name.substring(1).toLowerCase();
	}
	
	public String languageTag() {
		return langTag;
	}
}
