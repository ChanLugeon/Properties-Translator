package io.github.chanlugeon.proptrans.translator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.google.gson.Gson;

import io.github.chanlugeon.proptrans.Util;

final class GenieTalk extends Translator {
	private static final String HOST = "hnctrans.hancom.com";
	private static final String ORIGIN = "https://" + HOST;
	private static final String REFERER = ORIGIN + "/translate/trans_text.html";
	private static final String REQUEST = ORIGIN + "/translate/trans-ajax.html";
	
	private static final Map<String, String> HEADER = new HashMap<>();
	
	GenieTalk(Language lang) {
		super(lang);
		
		if (HEADER.isEmpty()) {
			HEADER.put("Accept", "application/json, text/javascript, */*; q=0.01");
			HEADER.put("Accept-Encoding", "gzip, deflate, br");
			HEADER.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			HEADER.put("Cache-Control", "no-cache");
			HEADER.put("Connection", "keep-alive");
			HEADER.put("Cache-Length", "");
			HEADER.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			HEADER.put("DNT", "1");
			HEADER.put("Host", HOST);
			HEADER.put("Origin", ORIGIN);
			HEADER.put("Pragma", "no-cache");
			HEADER.put("Referer", REFERER);
			HEADER.put("X-Requested-With", "XMLHttpRequest");
		}
	}

	@Override
	public String translate(String content) throws IOException {
		Map<String, String> formData = new HashMap<>();
		formData.put("type", "document");
		formData.put("source", "en");
		formData.put("target", lang.languageTag());
		formData.put("input", content);
		
		Response res = Jsoup.connect(REQUEST)
				.timeout(Util.WAIT_TIME)
				.headers(HEADER)
				.header("Content-Length", Integer.toString(Util.contentLength(formData)))
				.data(formData)
				.userAgent(Util.USER_AGENT)
				.ignoreContentType(true)
				.method(Method.POST)
				.execute();
		Json json = new Gson().fromJson(res.body(), Json.class);
		json.output.replaceAll("\\\\u", "\\u");
		//System.out.println(res.body());
		
		return json.output;
	}
	
	private static class Json {
		int code;
		String msg, output;
		
		@Override
		public String toString() {
			return String.format("{\"code\":\"%s\",\"msg:\":\"%s\",\"output\":\"%s\"}}",
					code, msg, output);
		}
	}
}
