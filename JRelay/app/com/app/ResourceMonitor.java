package com.app;

import com.relay.JRelay;

public class ResourceMonitor implements Runnable {

	public ResourceMonitor() {
		// TODO Auto-generated constructor stub
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
