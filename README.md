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
![alt text](https://i.imgur.com/K7EkJkY.png)

2. Create a `Java Class` file that will represent your plugin by right clikiking on your projects `src` folder and selecting `New > Class` You can name it whatever you like however, it's best to use proper naming conventions.
![alt text](https://i.imgur.com/ArdGJy4.png)

3. Add a reference to your project that allows you to access the important methods and functions of  `JRelayLib.jar'

