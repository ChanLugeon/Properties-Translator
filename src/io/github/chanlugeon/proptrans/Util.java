package io.github.chanlugeon.proptrans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class Util {
	public static final int WAIT_TIME = 10000;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";
	
	public Util() {
		throw new AssertionError();
	}
	
	@Deprecated
	public static String getLengthByString(Map<String, String> query) {
			return Integer.toString(query.toString().length() - query.size() - 1); // {}, & 
	}
	
	public static int contentLength(Map<String, String> query) {
		// String content = query.toString();
		int len = 0;
		
		for (Map.Entry<String, String> s : query.entrySet()) {
			len += s.getKey().length();
			try {
				len += URLEncoder.encode(s.getValue(), "UTF-8").replaceAll("%0A", "%0D%0A").length();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			len += 2; // =, &
		}
		
		return len - 1; // &
	}
}
