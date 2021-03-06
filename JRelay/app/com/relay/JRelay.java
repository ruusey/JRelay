package com.relay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.JRelayGUI;
import com.app.ResourceMonitor;
import com.crypto.RSA;
import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.event.JPlugin;
import com.event.PluginMetaData;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.models.ObjectMapper;
import com.models.Packet;
import com.net.ListenSocket;
import com.owlike.genson.Genson;

import javafx.scene.control.TextField;
import plugins.ClientUpdater;
import plugins.ReconnectHandler;

public final class JRelay implements Runnable {
	public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCKFctVrhfF3m2Kes0FBL/JFeOcmNg9eJz8k/hQy1kadD+XFUpluRqa//Uxp2s9W2qE0EoUCu59ugcf/p7lGuL99UoSGmQEynkBvZct+/M40L0E0rZ4BVgzLOJmIbXMp0J4PnPcb6VLZvxazGcmSfjauC7F3yWYqUbZd/HCBtawwIDAQAB\n";
	public static int TUTORIAL_GAMEID = -1;
	public static int NEXUS_GAMEID = -2;
	public static int RANDOM_REALM_GAMEID = -3;
	public static int MAPTEST_GAMEID = -6;
	public static final String GAME_VERSION = "X31.8.3";
	public static final String JRELAY_VERSION = "1.7.0";
	
	public static final boolean DEBUG = true;
	public static final boolean PROD = false;
	public static boolean PARSE_MAPS = true;
	public static String DEFAULT_SERVER = "";
	public static float AUTONEXUS_PERCENT;
	public static int FILTER_LEVEL;
	
	public static String APP_LOC = "";
	public static String RES_LOC = "";
	public static String APP_PKG = "";
	public static String RES_PKG = "";
	public static final JRelay instance = new JRelay();
//	public static ResourceMonitor monitor =new ResourceMonitor();
//	public static Thread td = new Thread(monitor);
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
	public final HashMap<Integer, InetSocketAddress> gameIdSocketAddressMap = new HashMap<Integer, InetSocketAddress>();
	public HashMap<User, Thread> userThreads = new HashMap<User, Thread>();
	public BiMap<String, State> userStates = HashBiMap.create();
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
	public static RSA rsa = new RSA();
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
			TextField anx = new TextField(p.getProperty("anx"));
			TextField filter = new TextField(p.getProperty("filter"));
			TextField server = new TextField(p.getProperty("server"));
			
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
			setting.add(anx);
			setting.add(filter);
			setting.add(server);

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
			JRelay.AUTONEXUS_PERCENT=Float.parseFloat(p.getProperty("anx"));
			JRelay.FILTER_LEVEL=Integer.parseInt(p.getProperty("filter"));
			JRelay.DEFAULT_SERVER = (p.getProperty("server"));
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
			p.setProperty("anx", String.valueOf(JRelay.AUTONEXUS_PERCENT));
			p.setProperty("filter", String.valueOf(JRelay.FILTER_LEVEL));
			p.setProperty("server", String.valueOf(JRelay.DEFAULT_SERVER));
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
			Float.valueOf(settings.get(9).getText());
			Integer.valueOf(settings.get(10).getText());
			p.setProperty("listenHost", settings.get(0).getText());
			p.setProperty("listenPort", settings.get(1).getText());
			p.setProperty("useExternalProxy", settings.get(2).getText());
			p.setProperty("externalProxyHost", settings.get(3).getText());
			p.setProperty("externalProxyPort", settings.get(4).getText());
			p.setProperty("remoteHost", settings.get(5).getText());
			p.setProperty("remotePort", settings.get(6).getText());
			p.setProperty("key0", settings.get(7).getText());
			p.setProperty("key1", settings.get(8).getText());
			p.setProperty("anx",  settings.get(9).getText());
			p.setProperty("filter",  settings.get(10).getText());
			p.setProperty("server",  settings.get(11).getText());
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
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JRelay.instance.connections = null;
		GameData.destroy();
		listenSocket.stop();
		JRelayGUI.log("Proxy Shutdown");
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
			ObjectMapper.buildMap();
			Packet.init();
			loadUserPlugins();
			
		} catch (Exception e) {
			JRelay.error(e.getMessage());
		}
		//td.start();
		if (JRelay.instance.listenSocket.start()) {
			JRelayGUI.updateStatus("Running", "green");
			while (!JRelay.instance.listenSocket.isClosed()) {
				while (!JRelay.instance.newUsers.isEmpty()) {
					User user = JRelay.instance.newUsers.remove(0);
					Thread userThread = new Thread(user);
					userThread.start();
					JRelayGUI.log("Client recieved");
				}
			}
			Iterator<User> i = JRelay.instance.users.iterator();
			while (i.hasNext()) {
				
				User user = i.next();
				JRelayGUI.log("Client disconnected");
				user.kick();
			}
		} else {
			JRelayGUI.error(
					"Failure starting the local listener. Make sure there is not an instance of JRelay running already.");
		}
	}

//	public void removeInactiveUsers(List<User> toRemove) {
//		JRelay.instance.users.removeAll(toRemove);
//	}

	/**
	 * Load user plugins from the plugins directory.
	 */
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

//	public void reloadPlugins() {
//		JRelay.userPluginClasses = new ArrayList<String>();
//		loadUserPlugins();
//	}

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
				JRelayGUI.log("Attached plugin " + obj.getClass().getName() + " to user");
				JRelay.instance.pluginData.add(new PluginMetaData(obj.getAuthor(), obj.getName(), obj.getDescription(),
						obj.getCommands(), obj.getPackets()));

				JRelay.instance.userPlugins.put(obj.getClass().getSimpleName(), obj);
			} catch (Exception e) {
				JRelayGUI.error("Unable to load plugin "+clazz+"... make sure your plugin is structured correctly");
			}
		}

		for (JPlugin pe : JRelay.instance.userPlugins.values()) {

			pe.attach();
		}
	}

	public State getState(User client, byte[] key) {
		String keyString = "";
		try {
			keyString = new String(key, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String guid = key.length == 0 ? "n/a" : keyString;

		State newState = new State(client, guid);
		if (!guid.equals("n/a")) {
			State lastState = userStates.get(guid);

			newState.conTargetAddress = lastState.conTargetAddress;
			newState.conTargetPort = lastState.conTargetPort;
			newState.conRealKey = lastState.conRealKey;

		}else {
			newState = new State(client, UUID.randomUUID().toString().replace("-", ""));
			userStates.forcePut(newState.GUID, newState);
		}

		return newState;
	}

}
