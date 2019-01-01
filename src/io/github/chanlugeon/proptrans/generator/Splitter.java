package io.github.chanlugeon.proptrans.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Splitter {
	private static final Pattern PATH = Pattern.compile("^(?<dir>.+)/(?<name>[^/]+)$"),
			ORIGINAL_NAME = Pattern.compile("^(?<name>\\S+)_\\S+(?<tag>)(?<extension>\\.[^.]*)$"); // \w
	
	public final String ORIGIN, DIR, FILE_NAME, BUNDLE_NAME, LANG_TAG, EXTENSION;
	
	public Splitter(String path) {
		ORIGIN = path;
		
		Matcher pathMatcher = PATH.matcher(path);
		pathMatcher.find();
		DIR = pathMatcher.group("dir");
		FILE_NAME = pathMatcher.group("name");
		
		Matcher fileNameMatcher = ORIGINAL_NAME.matcher(FILE_NAME);
		fileNameMatcher.find();
		BUNDLE_NAME = fileNameMatcher.group(1);
		LANG_TAG = fileNameMatcher.group("tag");
		EXTENSION = fileNameMatcher.group("extension");
	}
}
