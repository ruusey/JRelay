# JRelay 1.0.0 - RotMG 17.0.0
A modular Java man in the middle proxy for the flash game Realm of the Mad God

![alt text](https://i.imgur.com/8CJnRVb.png)

## Overview
JRelay is a man in the middle proxy for the popular flash browser game Realm of Mad God. JRelay's intended use is to allow users to create plugins that intercept and modify the games data which is transmitted in objects called Packets. JRelay is written in Java meaning it is platform independent so long as you have a compatible JRE installed on your Operating System. **JRelay requires Java 1.8+ to run.** 

There will be a guide for creating your own plugins as well as how to use the information made available to you through JRelay.

## Project Structure
JRelay is structured to allow the user acess to their plugins, game XML, packet structures and fields all contained within the install directory of JRelay. JRelay is built using basic Java socket IO to communicate with the Remote RotMG servers and your Local Client. The JRelayGUI can be run by simply **double clicking JRelay.jar.** For windows users, it is wise to create a batch program to execute JRelay. 

Please feel free to use the following pre-written batch script as well as add your own custom JVM arguments:
```
`java -jar /path/to/JRelay.jar`
`pause` 
```
For Windows Users the batch script can be executed from any location so long as the location of `JRelay.jar`
