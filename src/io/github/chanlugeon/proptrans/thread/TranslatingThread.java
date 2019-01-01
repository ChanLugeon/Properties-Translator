package io.github.chanlugeon.proptrans.thread;

import io.github.chanlugeon.proptrans.generator.PropertiesManager;

@Deprecated
public final class TranslatingThread extends Thread {
	private final PropertiesManager manager;
	
	public TranslatingThread(PropertiesManager pm) {
		manager = pm;
	}
	
	@Override
	public void run() {
		
	}
}
