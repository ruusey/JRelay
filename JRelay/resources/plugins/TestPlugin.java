package plugins;

import com.event.EventUtils;
import com.event.JPlugin;
import com.packets.server.TextPacket;
import com.relay.User;

public class TestPlugin extends JPlugin {

	public TestPlugin(User user) {
		super(user);

	}

	@Override
	public void attach() {
		user.hookCommand("testp", TestPlugin.class, "onHiCommand");

	}

	public void onHiCommand(String command, String[] args) {

		TextPacket packet = EventUtils.createText("test", "This is a test plugin created with JRelayLib");
		sendToClient(packet);

	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Ruusey";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TestPlugin";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "A plugin Test";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] { "/testp - testplugi", "/jr - prints JRelay version" };
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { "" };
	}

}
