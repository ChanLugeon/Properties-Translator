package io.github.chanlugeon.proptrans.translator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.google.gson.Gson;

import io.github.chanlugeon.proptrans.Util;

final class Kakao extends Translator {
	private static final String HOST = "translate.kakao.com",
			ORIGIN = "https://" + HOST,
			REFERER = ORIGIN + "/",
			REQUEST = ORIGIN + "/translator/translate.json";
	
	private static final Map<String, String> HEADER = new HashMap<>();
	private static final Map<String, String> KOREAN_CACHE = new HashMap<>();
	
	Kakao(Language lang) {
		super(lang);
		
		if (HEADER.isEmpty()) {
			HEADER.put("Accept", "*/*");
			HEADER.put("Accept-Encoding", "gzip, deflate, br");
			HEADER.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			HEADER.put("Connection", "keep-alive");
			HEADER.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			HEADER.put("DNT", "1");
			HEADER.put("Host", HOST);
			HEADER.put("Origin", ORIGIN);
			HEADER.put("Referer", REFERER);
			HEADER.put("X-Requested-With", "XMLHttpRequest");
		}
	}

	@Override
	public String translate(String content) throws IOException {
		if (!KOREAN_CACHE.containsKey(content)) {
			KOREAN_CACHE.put(content, translate(content, Language.ENGLISH, Language.KOREAN));
		}
		
		String koreanContent = KOREAN_CACHE.get(content);
		if (lang == Language.KOREAN) {
			return koreanContent;
		}
		
		return translate(koreanContent, Language.KOREAN, lang);
	}
	
	private String translate(String content, Language fromLang, Language toLang) throws IOException {
		String fromLangTag = kakaoLangCode(fromLang);
		String toLangTag = kakaoLangCode(toLang);
		
		Map<String, String> formData = new HashMap<>();
		formData.put("lang", fromLangTag + toLangTag);
		formData.put("q", content);
		
		Response res = Jsoup.connect(REQUEST)
				.timeout(Util.WAIT_TIME)
				.headers(HEADER)
				.header("Content-Length", Integer.toString(Util.contentLength(formData)))
				.cookie("hideMiniTooltip", "Y")
				.data(formData)
				.userAgent(Util.USER_AGENT)
				.ignoreContentType(true)
				.method(Method.POST)
				.execute();
		Json json = new Gson().fromJson(res.body(), Json.class);
		
		// System.out.println(formData.get("lang"));
		return json.result.translated;
	}
	
	private String kakaoLangCode(Language lang) {
		switch (lang) {
		case KOREAN: return "kr";
		case JAPANESE: return "jp";
		case CHINESE: return "cn";
		
		default: return lang.languageTag();
		}
	}
	
	private static class Json {
		Result result = new Result();
		
		@Override
		public String toString() {
			return String.format("{\"result\":{\"query\":\"%s\",\"translated:\":\"%s\",\"language\":\"%s\"}}",
					result.query, result.translated, result.language);
		}
		
		private static class Result {
			String query, translated, language;
		}
	}
}
