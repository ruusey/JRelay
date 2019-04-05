package com.app;

public class ResourceMonitor implements Runnable {
	boolean shutdown = false;

	public ResourceMonitor() {
	}

	@Override
	public void run() {
		while (!shutdown) {
			try {
				System.gc();
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void stop() {

		this.shutdown = true;
	}

}
