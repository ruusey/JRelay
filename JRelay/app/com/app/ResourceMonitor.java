package com.app;

public class ResourceMonitor implements Runnable {

	public ResourceMonitor() {
	}
	@Override
	public void run() {
		while(true) {
			try {
				System.gc();
				Thread.sleep(10000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	

}
