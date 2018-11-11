package com.relay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.JRelayGUI;
import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.event.JPlugin;
import com.event.PluginMetaData;
import com.models.ObjectMapper;
import com.models.Packet;
import com.net.ListenSocket;
import com.owlike.genson.Genson;

import javafx.scene.control.TextField;
import plugins.ClientUpdater;
import plugins.ReconnectHandler;

public final class JRelay implements Runnable {
	public static final String GAME_VERSION = "X31.1.0";
	public static final String JRELAY_VERSION = "1.2.0";
	public static final boolean PROD = true;
	//
	public static String APP_LOC = "";
	public static String RES_LOC = "";
	public static String APP_PKG = "";
	public static String RES_PKG = "";
	public static final JRelay instance = new JRelay();

	public String listenHost = "";
	public int listenPort = -1;

	// ************************************
	// USE A VPS PROXY INSTEAD OF LOCALHOST
	// ************************************

	public boolean useExternalProxy = false;
	public String externalProxyHost = "";
	public int externalProxyPort = -1;

	public String remoteHost = "";
	public int remotePort = -1;

	public String key0 = "";
	public String key1 = "";

	public final ListenSocket listenSocket;
	public final List<User> users = new ArrayList<User>();
	public final List<User> newUsers = new Vector<User>();
	public final Map<Integer, InetSocketAddress> gameIdSocketAddressMap = new Hashtable<Integer, InetSocketAddress>();
	public static HashMap<User, Thread> userThreads = new HashMap<User, Thread>();
	public final Map<String, State> userStates = new Hashtable<String, State>();
	public static Logger log = Logger.getLogger(GameData.class.getName());
	public static Genson gen = new Genson();

	public HashMap<String, JPlugin> userPlugins = new HashMap<String, JPlugin>();
	public ArrayList<PluginMetaData> pluginData = new ArrayList<PluginMetaData>();

	public HashMap<String, JPlugin> requiredPlugins = new HashMap<String, JPlugin>();
	public HashMap<PacketType, ArrayList<Method>> requiredPacketHooks = new HashMap<PacketType, ArrayList<Method>>();
	public HashMap<PacketType, ArrayList<Method>> packetHooks = new HashMap<PacketType, ArrayList<Method>>();
	public HashMap<String, Method> commandHooks = new HashMap<String, Method>();

	public static ArrayList<String> userPluginClasses = new ArrayList<String>();
	public ClientUpdater updater = null;
	public ReconnectHandler reconHandler = null;

	public ArrayList<Thread> connections = new ArrayList<Thread>();

	public JRelay() {
		if(!JRelay.PROD) {
			JRelay.APP_LOC="app\\";
			JRelay.RES_LOC="resources\\";
			JRelay.APP_PKG="app\\";
			JRelay.RES_PKG="resources\\";
		}
		loadSettings();
		
		this.listenSocket = new ListenSocket(this.listenHost, this.listenPort) {

			@Override
			public void socketAccepted(Socket localSocket) {
				try {
					User user = new User(localSocket);
					initUser(user);
					attachUserPlugins(user);
					JRelay.instance.newUsers.add(user);
				} catch (Exception e) {
					e.printStackTrace();
					try {
						localSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
	}

	public void initUser(User user) {
		updater = new ClientUpdater(user);
		reconHandler = new ReconnectHandler(user);
		updater.attach();
		reconHandler.attach();

		requiredPlugins.put(updater.getClass().getSimpleName(), updater);
		requiredPlugins.put(reconHandler.getClass().getSimpleName(), reconHandler);
	}

	public ArrayList<TextField> getSettings() {
		File file = new File(RES_LOC + "settings.properties");
		Properties p = new Properties();
		try {
			InputStream in = new FileInputStream(file);
			p.load(in);
			in.close();
			TextField listenHost = new TextField(p.getProperty("listenHost"));
			TextField listenPort = new TextField(p.getProperty("listenPort"));
			TextField useExternalProxy = new TextField(p.getProperty("useExternalProxy"));
			TextField externalProxyHost = new TextField(p.getProperty("externalProxyHost"));
			TextField externalProxyPort = new TextField(p.getProperty("externalProxyPort"));
			TextField remoteHost = new TextField(p.getProperty("remoteHost"));
			TextField remotePort = new TextField(p.getProperty("remotePort"));
			TextField key0 = new TextField(p.getProperty("key0"));
			TextField key1 = new TextField(p.getProperty("key1"));

			ArrayList<TextField> setting = new ArrayList<TextField>();
			setting.add(listenHost);
			setting.add(listenPort);
			setting.add(useExternalProxy);
			setting.add(externalProxyHost);
			setting.add(externalProxyPort);
			setting.add(remoteHost);
			setting.add(remotePort);
			setting.add(key0);
			setting.add(key1);

			return setting;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void loadSettings() {
		File file = new File(RES_LOC + "settings.properties");

		Properties p = new Properties();
		try {
			InputStream in = new FileInputStream(file);
			p.load(in);
			in.close();
			this.listenHost = p.getProperty("listenHost");
			this.listenPort = Integer.parseInt(p.getProperty("listenPort"));
			this.useExternalProxy = Boolean.parseBoolean(p.getProperty("useExternalProxy"));
			this.externalProxyHost = p.getProperty("externalProxyHost");
			this.externalProxyPort = Integer.parseInt(p.getProperty("externalProxyPort"));
			this.remoteHost = p.getProperty("remoteHost");
			this.remotePort = Integer.parseInt(p.getProperty("remotePort"));
			this.key0 = p.getProperty("key0");
			this.key1 = p.getProperty("key1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		Properties p = new Properties();
		try {
			p.setProperty("listenHost", this.listenHost);
			p.setProperty("listenPort", String.valueOf(this.listenPort));
			p.setProperty("useExternalProxy", String.valueOf(this.useExternalProxy));
			p.setProperty("externalProxyHost", this.externalProxyHost);
			p.setProperty("externalProxyPort", String.valueOf(this.externalProxyPort));
			p.setProperty("remoteHost", this.remoteHost);
			p.setProperty("remotePort", String.valueOf(this.remotePort));
			p.setProperty("key0", this.key0);
			p.setProperty("key1", this.key1);
			File file = new File(RES_LOC + "settings.properties");
			if (!file.isFile()) {
				try {
					OutputStream out = new FileOutputStream(file);
					p.store(out, "JRelay Settings");
					out.close();
					JRelayGUI.createPopup("Settings Saved", "Ok");
				} catch (Exception e) {
					JRelayGUI.createPopup("Save Settings Failed \n" + e.getMessage(), "Close");
				}
			}
		} catch (Exception e) {
			JRelayGUI.createPopup("Save Settings Failed \n" + e.getMessage(), "Close");
		}

	}

	public void saveSettings(ArrayList<TextField> settings) {
		Properties p = new Properties();
		try {
			// CHECK VALUES
			Integer.valueOf(settings.get(1).getText());
			Boolean.valueOf(settings.get(2).getText());
			Integer.valueOf(settings.get(4).getText());
			Integer.valueOf(settings.get(6).getText());
			p.setProperty("listenHost", settings.get(0).getText());
			p.setProperty("listenPort", settings.get(1).getText());
			p.setProperty("useExternalProxy", settings.get(2).getText());
			p.setProperty("externalProxyHost", settings.get(3).getText());
			p.setProperty("externalProxyPort", settings.get(4).getText());
			p.setProperty("remoteHost", settings.get(5).getText());
			p.setProperty("remotePort", settings.get(6).getText());
			p.setProperty("key0", settings.get(7).getText());
			p.setProperty("key1", settings.get(8).getText());
			File file = new File(RES_LOC + "settings.properties");

			try {
				OutputStream out = new FileOutputStream(file);
				p.store(out, "JRelay Settings");
				out.close();
				JRelayGUI.createPopup("Settings Saved", "Ok");
			} catch (Exception e) {
				JRelayGUI.createPopup("Save Settings Failed \n" + e.getMessage(), "Close");
			}
		} catch (Exception e) {
			JRelayGUI.createPopup("Save Settings Failed \n" + e.getMessage(), "Close");
		}
	}

	public void shutdown() {
		JRelay.instance.connections = null;
		GameData.destroy();
		listenSocket.stop();
		JRelay.info("Proxy Shutdown");
		System.gc();
	}

	public static void error(String message) {
		JRelayGUI.error(message);
		log.log(Level.SEVERE, message);
	}

	public static void info(String message) {
		JRelayGUI.log(message);
		log.log(Level.INFO, message);
	}

	public InetSocketAddress getSocketAddress(int gameId) {
		InetSocketAddress socketAddress = this.gameIdSocketAddressMap.get(gameId);
		if (socketAddress == null) {
			return new InetSocketAddress(this.remoteHost, this.remotePort);
		}
		return socketAddress;
	}

	public void setSocketAddress(int gameId, String host, int port) {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		this.gameIdSocketAddressMap.put(gameId, socketAddress);
	}

	@Override
	public void run() {
		try {
			JRelayGUI.updateStatus("Loading assets...", "orange");
			if (!GameData.loadData()) {
				JRelay.info("GameData unable to load. Exiting...");
				System.exit(0);
			}
			ObjectMapper.buildMap();
			Packet.init();
			loadUserPlugins();

		} catch (Exception e) {
			JRelay.error(e.getMessage());
		}
		if (JRelay.instance.listenSocket.start()) {
			JRelayGUI.updateStatus("Running", "green");
			while (!JRelay.instance.listenSocket.isClosed()) {
				while (!JRelay.instance.newUsers.isEmpty()) {
					User user = JRelay.instance.newUsers.remove(0);
					Thread userThread = new Thread(user);
					connections.add(userThread);
					userThread.start();
					JRelay.info("Client recieved...");
				}
			}
			Iterator<User> i = JRelay.instance.users.iterator();
			while (i.hasNext()) {
				
				User user = i.next();
				user.kick();
			}
		} else {
			JRelay.info(
					"Failure starting the local listener. Make sure there is not an instance of JRelay running already.");
		}
	}

	public void removeInactiveUsers(List<User> toRemove) {
		JRelay.instance.users.removeAll(toRemove);
	}

	public static void loadUserPlugins() {
		Path p = Paths.get(RES_LOC + "plugins\\");
		File f = p.toFile();
		String[] files = f.getAbsoluteFile().list();
		for (String plugin : files) {
			String clazz = plugin.substring(0, plugin.indexOf("."));
			JRelay.userPluginClasses.add(clazz);
			JRelayGUI.log("Loaded user plugin " + clazz);
		}
	}

	public void reloadPlugins() {
		JRelay.userPluginClasses = new ArrayList<String>();
		loadUserPlugins();
	}

	public void attachUserPlugins(User user) {
		// ATTACH PLUGIN EVENTS TO SPECIFIC USERS
		String pluginDir = null;
		File plugins = null;
		File[] pluginsToLoad = null;
		ArrayList<String> classesToLoad = new ArrayList<String>();
		try {
			pluginDir = JRelayGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			pluginDir = pluginDir.substring(0,pluginDir.lastIndexOf("/"));
			pluginDir+="/plugins/";
			plugins = new File(pluginDir);
			pluginsToLoad = plugins.listFiles();
			for(File f: pluginsToLoad) {
				classesToLoad.add(f.getName());
			}
			System.out.println("Located Plugin Directory "+pluginDir);
			
			for(String s : classesToLoad) {
				System.out.println("Located Plugin "+s);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		JRelay.instance.pluginData = new ArrayList<PluginMetaData>();
		for (String clazz : classesToLoad) {
			try {
				System.out.println("trying to load class "+clazz);
				URL pluginUrl = plugins.toURI().toURL();
				URL[] urls = { pluginUrl };
				URLClassLoader cl = URLClassLoader.newInstance(urls);
				clazz = clazz.substring(0, clazz.lastIndexOf("."));
				JPlugin obj = (JPlugin) cl.loadClass("plugins."+clazz)
						.getDeclaredConstructor(User.class).newInstance(user);
				JRelay.info("Attached plugin " + obj.getClass().getName() + " to user");
				JRelay.instance.pluginData.add(new PluginMetaData(obj.getAuthor(), obj.getName(), obj.getDescription(),
						obj.getCommands(), obj.getPackets()));

				JRelay.instance.userPlugins.put(obj.getClass().getSimpleName(), obj);
			} catch (Exception e) {
				JRelay.error("Unable to load plugin "+clazz+"... make sure your plugin is structured correctly");
			}
		}

		for (JPlugin pe : JRelay.instance.userPlugins.values()) {

			pe.attach();
		}
	}

	public State getState(User client, byte[] key) {
		String keyString = null;
		try {
			keyString = new String(key, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String guid = key.length == 0 ? "n/a" : keyString;

		State newState = new State(client, UUID.randomUUID().toString().replace("-", ""));
		userStates.put(newState.GUID, newState);
		if (!guid.equals("n/a")) {
			State lastState = userStates.get(guid);

			newState.conTargetAddress = lastState.conTargetAddress;
			newState.conTargetPort = lastState.conTargetPort;
			newState.conRealKey = lastState.conRealKey;

		}

		return newState;
	}

}
