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
				System.out.print("Command Hooks: "+JRelay.instance.commandHooks.size()+" | ");
				System.out.print("Connections: "+JRelay.instance.connections.size()+" | ");
				System.out.print("Packet Hooks: "+JRelay.instance.packetHooks.size()+" | ");
				System.out.print("Required Packet Hooks: "+JRelay.instance.requiredPacketHooks.size()+" | ");
				System.out.print("Required Plugins: "+JRelay.instance.requiredPlugins.size()+" | ");
				System.out.print("User Plugins: "+JRelay.instance.userPlugins.size()+" | ");
				System.out.print("Users: "+JRelay.instance.users.size()+" | ");
				System.out.print("User States: "+JRelay.instance.userStates.size()+" | ");
				Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	

}
