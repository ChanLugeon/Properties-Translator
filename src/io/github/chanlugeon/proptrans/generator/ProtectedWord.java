package io.github.chanlugeon.proptrans.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO number equals string
public class ProtectedWord {
	public enum Type {
		NUMBER, STRING
	}
	
	private static final int FORM = 0, FORM_REPLACED = 1;
	
	private static final String[][] FORM_CHARS = {
			{"%c", "G98121UI"},
			{"%d", "98122"},
			{"%i", "98123"},
			{"%f", "98124"},
			{"%s", "G98125UI"},
			{"%o", "98126"},
			{"%u", "98127"},
			{"%x", "98128"},
			{"%X", "98129"},
			{"%e", "98130"},
			{"%E", "98131"},
			{"%g", "98132"},
			{"%G", "98133"},
			{"%%", "98134"},
			//{"AB", "98134"} // TEST
	};
	
	private static final int BEGIN_CODE_NUM = 98180;
	private static final String START_STR_WRAPPER = "G";
	private static final String END_STR_WRAPPER = "UI";
	public final Map<String, String> words = new HashMap<>(); // word, encodedWord
	
	public ProtectedWord put(String word, Type type) {
		int codeNum = BEGIN_CODE_NUM + words.size();
		String encodedWord = null;
		
		switch (type) {
		case NUMBER: case STRING:
			encodedWord = Integer.toString(codeNum);
			break;
		
		/*case STRING:
			StringBuilder b = new StringBuilder((int)(Math.log10(codeNum) + 1) + 3);
			b.append(START_STR_WRAPPER);
			b.append(codeNum);
			b.append(END_STR_WRAPPER);
			encodedWord = b.toString();
			break;*/
		
		default:
			throw new IllegalArgumentException(type.name());
		}
		
		words.put(word, encodedWord);
		return this;
	}
	
	public String protect(String query) {
		// Default protected words
		for (String[] formChar: FORM_CHARS) {
			query = query.replace(formChar[FORM], formChar[FORM_REPLACED]);
		}
		
		for (Iterator<String> iter = words.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			query = query.replace(key, words.get(key));
		}
		
		return query;
	}
	
	public String unprotect(String query) {
		// Default protected words
		for (String[] formChar: FORM_CHARS) {
			query = query.replace(formChar[FORM_REPLACED], formChar[FORM]);
		}

		for (Iterator<String> iter = words.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			query = query.replace(words.get(key), key);
		}

		return query;
	}
	
	@Deprecated
	private static class WordData {
		String word;
		Type type;
		
		@Override
		public int hashCode() {
			int result = word.hashCode();
			result = 31 * result + type.hashCode();
			return result;
		}
	}
}
