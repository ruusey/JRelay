![alt text](https://i.imgur.com/K7EkJkY.png)
# **JRelay**  1.0.0 - RotMG 17.0.0

A modular Java man in the middle proxy for the flash game Realm of the Mad God



## Overview
**JRelay** is a man in the middle proxy for the popular flash browser game Realm of Mad God. **JRelay's** intended use is to allow users to create plugins that intercept and modify the games data which is transmitted in objects called Packets. JRelay is written in Java meaning it is platform independent so long as you have a compatible JRE installed on your Operating System. **JRelay requires Java 1.8+ to run.** 

There will be a guide for creating your own plugins as well as how to use the information made available to you through JRelay.

## Project Structure
**JRelay** is structured to allow the user acess to their plugins, game XML, packet structures and fields all contained within the install directory of **JRelay**. **JRelay** is built using basic Java socket IO to communicate with the Remote RotMG servers and your Local Client. The JRelayGUI can be run by simply **double clicking JRelay.jar.** For windows users, it is wise to create a batch program to execute JRelay. 

**JRelay** makes use of a modular plugin system to allow third parties to write their own programs to manipulate the game's data. Plugins in **JRelay** operate slightly different from plugins for `KRelay` which most of you are used to. Plugins for **JRelay** are not attached to individual user instances until they connect

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
I highly recommend **Spring Tool Suite** and **Eclipse** as the tutorial I provide will be a one-to-one translation in terms of the steps taken to create a plugin.

### Steps
**1)** Create a new Java Project by navigating to `New > Project` in Eclipse or STS and selecting the Java Project creation wizard.

![alt text](https://i.imgur.com/Mw7MG5T.png)

**2)** Create a new `Java Class` file that will represent your plugin by right clicking on your projects `src` folder and selecting `New > Class` You can name it whatever you like however, it's best to use proper naming conventions.

![alt text](https://i.imgur.com/ArdGJy4.png)

**3)** Add a reference to your project that allows you to access the important methods and functions of  `JRelayLib.jar` by right clicking on your plugin project and navigating to `Properties > Java Build Path > Libraries > Add External Jars` 
![alt text](https://i.imgur.com/SSXHzgO.png)

**4)** Locate `JRelayLib.jar` on your file system and add it to your projects referenced external libraries. This will allow you to incorporate methods for intercepting and manipulating game data. You should now see `JRelayLib.jar` under the list of libraries included in your project.

![alt text](https://i.imgur.com/xwmSGa6.png)

**5)** Set up your plugin class to extend the functionality of **JRelay's** included `JPlugin` type. A type extending `JPlugin` requires the folowwing structure in order to work with **JRelay's** plugin system. If you are using an IDE, the compiler will complain that you have unimplemented methods and unimported libraries but will auto include them for you if you wish. However, if you don't plan on using an IDE for developing **JRelay** plugins please observe the following **__required__** structure:
> Please note that **ALL** overidden methods must return a **NON-NULL** value. It can be empty but not null.

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
public interface PluginData {
	public void attach();
	public String getAuthor();
	public String getName();
	public String getDescription();
	public String[] getCommands();
	public String[] getPackets();
}
```

**6)** Hooking packets, commands and adding your own code to your **JRelay** plugin.
All plugin related hooking into packets and user commands handled by **JRelay** is done within your plugin's `attach()` method.
Within the attach method you have the option to bind user commands or have the ingestion of a specified `PacketType` trigger events.
These two means of proxy data manipulation are available to the plugin creater through the methods

```Java
	hookPacket(PacketType type, Class<? extends JPlugin> location, String callback);
	hookCommand(String command, Class<? extends JPlugin> location, String callback);
```

**7)** To make use of **JRelay's** ability to hook packets and commands simply:
- **For Packets**
	```Java	
	@param(type)     //enum value of type PacketType.
	@param(location) //Your plugin's Class (MyPlugin.class)
	@param(callback) //The method to invoke when PacketType is captured by JRelay. 
			 //This method will receive an instance of the packet captured.
	hookPacket(PacketType type, Class<? extends JPlugin> location, String callback);	  
	```
	
- **For Commands**
	```Java
	@param(command)  //User command to trigger your plugin
	@param(location) //Your plugin's Class (MyPlugin.class)
	@param(callback) //The method to invoke when your command is invoked. 
			 //This method will receive a copy of the command.
			 //As well as any arguments passed by the user	
	hookCommand(String command, Class<? extends JPlugin> location, String callback);  
	```
**8)** Implementing custom packet and command handlers for proxy events. Since we have introduced the means by which you can intercept packets and create command based functionality with **JRelay**, we will not cover how to implement these methods into useful plugins for manipulating the game.


# JRelay Usage

## **Example Packet Hook**
We will now take a look at an example how you can hook a callback function to the game's `UpdatePacket`:
> Example from `JRelay.Glow` plugin included with release.
```Java
@Override
public void attach() {
	user.hookPacket(PacketType.UPDATE, Glow.class, "onUpdatePacket");		
}
```
As you see we have hooked `PacketType.UPDATE` to trigger the method `onUpdatePacket()` within our plugin titled **Glow** as seen by referencing `Glow.class` as the second argument for the `hookPacket` method. It is **__VERY__** important to ensure your class location matches the name of the compilation unit for your plugin otherwise JRelay will not be able to detect your packet and command hooks.

After we have hooked our packet to its callback method we must then include the method itself within our plugin. This is accomplished by simply including a `public void` method that shares the same name ("onUpdatePacket") as the callback you specified in your hook. Your callback must take an argument for type `com.models.Packet` which the only parameter passed into the callback function is the packet that was captured. Continuing the example above, it would look like so:

```Java
public void onUpdatePacket(Packet p){
	UpdatePacket update = (UpdatePacket)p;

         for (int i = 0; i < update.newObjs.length; i++)
         {
             if (update.newObjs[i].status.objectId == user.playerData.ownerObjectId)
             {
                 for (int j = 0; j < update.newObjs[i].status.data.length; j++)
                 {
                     if (update.newObjs[i].status.data[j].id == 59)
                         update.newObjs[i].status.data[j].intValue = 100;
                 }
             }
         }
}
```
While Packet is a generic `superclass` of all packets sent and received by the server, Since your packet hook specified the `PacketType` to be triggered on, you may freely cast the received packet parameter to the type expected as seen on line 2.
`UpdatePacket update = (UpdatePacket)p;` This is the case with all packet hook routines.

## **Example Command Hook**
We will now take a look at an example how you can hook a user inputted command to a callback method:
> Example from `JRelay.Core` plugin included with release.
```Java
@Override
public void attach() {
	user.hookCommand("hi", Core.class, "onHiCommand");		
}
```
In this case, we have hooked the command `"hi"` to the callback function `onHiCommand()` within our plugin class `Core`. This means that when the user types `/hi` in chat in-game, the code within your plugin will be executed. Again it is important to ensure your class location matches the name of the compilation unit for your plugin. 

Like packet hooks, after we have hooked our command to its callback method we must then include the method itself within our plugin. This is accomplished by simply including a `public void` method that shares the same name ("onHiCommand") as the callback you specified in your hook. Unlike packet hook callbacks, command hook callbacks will be passed two parameters: A `String` containing the command invoked and a `String[]` containing the command and all arguments included with the command. It looks as follows:
> Example from `JRelay.Glow` plugin included with release.
>**Arguments are space delimited**
```Java
public void onHiCommand(String command, String[] args){
	if(args.length<2){
		TextPacket packet = EventUtils.createText("hi", "Too few argumenets /hi [on/off]");
		sendToClient(packet);
	}else if(args[1].equals("on")){
		TextPacket packet = EventUtils.createText("hi", "Hello! heres some player data: name="+user.playerData.name+" 				fame="+user.playerData.characterFame);
		sendToClient(packet);
	}else if(args[1].equals("off")){
		TextPacket packet = EventUtils.createText("hi", "Hello! no player data requested");
		sendToClient(packet);
	}
}
```
## Packet Manipulation Explained
Once you have set up your very own plugin to run with **JRelay** it is important to know some of the important ins and outs of the packet capture and manipulation process.

#### Capturing Packets
Packets are containers for data exchanged between your RotMG client and Deca's servers. Some types of packets are only sent from the client to the server while some are only sent from the server and received by the client. The respective packets and their transmission source can be viewed under the `Packets` of the JRelayGUI. If you chose to capture packets and modify their data or stop their transmission there are a few important things to note:
1. Because packet hook callbacks receive a generic untyped packet, The generic packet must be cast to the desired packet type. If you chose to capture **AND MODIFY** a packet through the means mentioned above you **MUST** explicitly send the modified packet to your required destination using the included `sendToClient(Packet packet)` and `sendToServer(Packet packet)` superclass methods of `JPlugin`
2. If you wish to capture a specific packet and prevent its transmission simply change the Packet's `boolean send;` field to `false`
3. Any packet can be created using  `Packet.create(byte id)` or `Packet.create(PacketType type)`
