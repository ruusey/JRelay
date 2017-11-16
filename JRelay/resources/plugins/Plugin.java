package plugins;

import com.event.EventUtils;
import com.event.JPlugin;
import com.packets.server.TextPacket;
import com.relay.User;

public class Plugin extends JPlugin {

	public Plugin(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attach() {
		user.hookCommand("t", Plugin.class, "testCommand");

	}
	public void testCommand(String command, String[] args){
	
			TextPacket packet = EventUtils.createText("TEST", "This is a test");
			sendToClient(packet);
		
	}
	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Test Author";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Test Plugin";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Test Description";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] {"/test - test command"};
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] {""};
	}

}
