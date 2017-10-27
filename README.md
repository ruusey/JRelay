![alt text](https://i.imgur.com/K7EkJkY.png)
# **JRelay**  1.0.0 - RotMG 17.0.0

A modular Java man in the middle proxy for the flash game Realm of the Mad God



## Overview
**JRelay** is a man in the middle proxy for the popular flash browser game Realm of Mad God. **JRelay's** intended use is to allow users to create plugins that intercept and modify the games data which is transmitted in objects called Packets. JRelay is written in Java meaning it is platform independent so long as you have a compatible JRE installed on your Operating System. **JRelay requires Java 1.8+ to run.** 

There will be a guide for creating your own plugins as well as how to use the information made available to you through JRelay.

## Project Structure
**JRelay** is structured to allow the user acess to their plugins, game XML, packet structures and fields all contained within the install directory of **JRelay**. **JRelay** is built using basic Java socket IO to communicate with the Remote RotMG servers and your Local Client. The JRelayGUI can be run by simply **double clicking JRelay.jar.** For windows users, it is wise to create a batch program to execute JRelay. 

## Windows Users:
Please feel free to use the following pre-written batch script as well as add your own custom JVM arguments:
```
java -jar /path/to/JRelay.jar
pause 
```
For Windows Users the batch script can be executed from any location so long as the location of `JRelay.jar` is correct in terms of its absolute or relative path.

## Mac/Solaris/UNIX/Linux Users:
After confirming you have **Java 1.8+** installed on your machine you should have no issues simply executing `JRelay.jar`, you may however create shorcuts or other execution scripts for you specific environment depending on your needs.

## JRelay Plugins
As mentioned, **JRelay** supports the implementation of User created plugins. The support for users to create thier own plugins will be made avaialable through acessing the distributed `JRelayLib.jar` library. `JRelayLib` contains all essential data and methods neccesarry to create your very own **JRelay** plugins.

## Plugin Creation
Plugin creation has been made a streamlined and easy as possible even for novice developers. JRelay plugins can be writtin in any IDE such as **Eclipse,** **Net Beans,** **Spring Tool Suite (STS)** or even a simple **Text Editor.** 
I highly recommend Spring Tool Suite and Eclipse as the tutorial I provide will be a one-to-one translation in terms of the steps taken to create a plugin.
### Steps
1. Create a Java Project by navigating to `New > Project` within Eclipse or STS and selecting the Java Project creation wizard.

![alt text]((https://i.imgur.com/Mw7MG5T.png))

2. Create a `Java Class` file that will represent your plugin by right clikiking on your projects `src` folder and selecting `New > Class` You can name it whatever you like however, it's best to use proper naming conventions.

![alt text](https://i.imgur.com/ArdGJy4.png)

3. Add a reference to your project that allows you to access the important methods and functions of  `JRelayLib.jar` by right clicking on your plugin project and navigating to `Properties > Java Build Path > Libraries > Add External Jars`

![alt text](https://i.imgur.com/SSXHzgO.png)

4. Locate `JRelayLib.jar` on your file system and add it to your projects referenced external libraries. This will allow you to incorporate methods for intercepting and manipulating game data.

![alt text](https://i.imgur.com/xwmSGa6.png)

5. Set up your plugin class to extend the functionality of **JRelay's** included `JPlugin` type. A type extending `JPlugin` requires the folowwing structure in order to work with **JRelay's** plugin system. If you are using an IDE, the compiler will complain that you have unimplemented methods and unimported libraries but will auto generate them for you if you wish. However, if you dont plan on using an IDE for developing **JRelay** plugins please observe the following **__required__** structure:

```Java
import com.relay.User;

public class MyPlugin extends JPlugin{

	public MyPlugin(User user) {
		super(user);
	}
  
	@Override
	public void attach() {
		//Attach packet and command hooks here
	}
  
	@Override
	public String getAuthor() {
		return "";
	}
  
	@Override
	public String[] getCommands() {
		return new String[]{};
	}
  
	@Override
	public String getDescription() {
		return "";
	}
  
	@Override
	public String getName() {
		return "";
	}

	@Override
	public String[] getPackets() {
		return String[]{};
	}
}
```

Notes:
A plugin built using the superclass `JPlugin` requires the following methods as determined by its class heiarchy:

```Java
	public void attach();
	public String getAuthor();
	public String getName();
	public String getDescription();
	public String[] getCommands();
	public String[] getPackets();
```

6. Hooking packets, commands and adding your own code to your **JRelay** plugin.
All plugin related hooking into packets and user commands handled by **JRelay** is done within your plugin's `attach()` method.
Within the attach method you have the option to bind user commands or have the ingestion of a specified `PacketType` trigger events.
These two means of proxy data manipulation are available to the plugin creater through the methods

```Java
hookPacket(PacketType type, Class<? extends JPlugin> location, String callback);
hookCommand(String command, Class<? extends JPlugin> location, String callback);
```



