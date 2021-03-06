# **JRelay** Beta 1.5.3 - RotMG X31.5.0

#### A plugin-based Java man in the middle proxy for the browser flash game **Realm of the Mad God**
![alt text](https://i.imgur.com/iOUNE86.png)

#### Up to date virus scans (for _those_ people).
> [Jotti](https://virusscan.jotti.org/en-US/filescanjob/k7ktwf324o)

> [VirusTotal](https://www.virustotal.com/#/file/aa78c7c09c10d623399dfd34bbcd083f19f7d7711e7f50fd0c3bfc428aa861f8/detection)

# Table of Contents
1. [Overview](https://github.com/ruusey/JRelay/blob/master/README.md#overview)
2. [Plugin Creation](https://github.com/ruusey/JRelay/blob/master/README.md#plugin-creation)
3. [How To Use JRelay](https://github.com/ruusey/JRelay/blob/master/README.md#using-jrelay)
4. [Useful Data and Fields](https://github.com/ruusey/JRelay/blob/master/README.md#useful-data-and-fields)
5. [Event Utils](https://github.com/ruusey/JRelay/blob/master/README.md#event-utils)
6. [Creating Custom Tile & Object Maps](https://github.com/ruusey/JRelay/blob/master/README.md#create-custom-tile--object-maps)

---
# Overview
**JRelay** is a man in the middle proxy for the popular flash browser game Realm of Mad God. **JRelay's** intended use is for users to create plugins that intercept and modify the games data which is transmitted in objects called Packets. JRelay is written in Java meaning it is platform independent so long as you have a compatible JRE installed on your Operating System. **JRelay requires Java 1.8+ to run.** 

### Project Structure
Functionality of **JRelay** is built using `Java Socket IO` to communicate with your RotMG client and Deca's game servers. **JRelay** incorporates aspects of pre-existing RotMG proxies with the added benefit of platofm independence. 

**JRelay** incorporates a modular plugin implementation system allowing third parties to write their own plugins to manipulate the game's data.

**JRelay** distribution consist of two components: the **JRelay** proxy itself as well as **JRelayLib** which is the library meant to be referenced for 3rd party plugin development.

**JRelay** is built using `Maven` for `Spring Tool Suite` and makes use of the following dependencies:
- `Dom4J 1.6.1`
- `Genson 1.4`
- `Jaxen 1.6`
- `Google Guava`
---
# Deploying JRelay
How to get **JRelay** up and running on your system.

### Windows Users:
Please feel free to use the following pre-written batch script as well as add your own custom JVM arguments:
```
java -jar /path/to/JRelay.jar
pause 
```
Otherwise you can simply double click `JRelay.jar` to start **JRelay**

### Mac/Linux Users:
Asuming **Java 1.8+** is installed on your machine, you can simply execute `JRelay.jar` from the download directory. 

---

# Plugin Creation
As mentioned, **JRelay** supports the implementation of User created plugins. The support for users to create thier own plugins will be made avaialable through acessing the distributed `JRelayLib.jar` library. `JRelayLib` contains all essential data and methods neccesarry to create your very own **JRelay** plugins.

Plugin creation has been made a streamlined and easy as possible even for novice developers. **JRelay** plugins can be writtin in any IDE such as **Eclipse,** **Net Beans,** **Spring Tool Suite (STS)** or even a simple **Text Editor.** 
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

**5)** Your project setup should now look as follows.

![alt text](https://i.imgur.com/tulZrrI.png)

**6)** You will now need to create a package within the source folder to contain your plugin class. The reason for this is that **JRelay** loads plugins from the `plugins` directory. With how Java class files are compiled this is a **necessary** step to ensure that **JRelay's** plugin loading system detects your plugin.

Right-Click on the `src` folder of your Java project and navigate to New > Package. It is essential to name this package `plugins`.

![alt text](https://i.imgur.com/vDNrQhw.png)

**7)** Create a new Java Class within the `plugins` package. This class will extend the functionality of **JRelay's** included `JPlugin` type. A type extending `JPlugin` requires the structure shown below in order to work with **JRelay's** plugin system. 

If you are using an IDE, the compiler will complain that you have unimplemented methods and unimported libraries but will auto include them for you if you wish. 

If you don't plan on using an IDE for developing **JRelay** plugins please follow the **__proper__** plugin structure defined below:

> Please note that **ALL** overidden methods must return a **NON-NULL** value.
> In the example below we have created a plugin class called `MyPlugin`.

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

**Note**
>A plugin built using the superclass `JPlugin` requires the following methods as determined by its class heiarchy:

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

**8)** Hooking packets, commands and adding your own code to your **JRelay** plugin.

All plugin related hooking into packets and user commands handled by **JRelay** is done within your plugin's `attach()` method.

Within the `attach()` method you have the option to bind user commands or set up Packets to trigger events.
These two means of proxy data manipulation are available to the plugin creater through the methods

```Java
	hookPacket(PacketType type, Class<? extends JPlugin> location, String callback);
	hookCommand(String command, Class<? extends JPlugin> location, String callback);
```

**9)** Methods for hooking commands and packets and their parameters.
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
**10)** Implement your own custom packet and command handlers for your new plugin. Continue reading to learn more about the powerful tools within **JRelay** and how to make use of them to manipulate the game's data.

**11)** Including your plugin in **JRelay**. 
To add your plugin into **JRelay** you need to compile your plugin class and add it to the `plugins` directory of **JRelay**.

If you used an IDE, export your plugin as a jar and extract the individual class file from it.

Please look up how to compile Java source code into Class files if this is confusing.

---

# Using JRelay 
#### This section will show you how to make use of and implement JRelays command and packet hooking features.

### **Example Packet Hook**
We will now take a look at an example how you can hook a callback function to the game's `UpdatePacket`:
> Example from `JRelay.Glow` plugin included with release.
```Java
@Override
public void attach() {
	user.hookPacket(PacketType.UPDATE, Glow.class, "onUpdatePacket");		
}
```
We have hooked `PacketType.UPDATE` to trigger the method `onUpdatePacket()` within our plugin titled **Glow** as seen by referencing `Glow.class` as the second argument for the `hookPacket` method. 
It is **__VERY__** important to ensure your class location matches the name of the compilation unit for your plugin otherwise **JRelay** will not be able to detect your packet and command hooks.

After we have hooked our packet to its callback method we must then include the method itself within our plugin. 
This is accomplished by simply including a `public void` method that shares the same name ("onUpdatePacket") as the callback you specified in your hook. 

Your callback must take an argument for type `com.models.Packet` which the only parameter passed into the callback function is the packet that was captured. Continuing the example above, it would look like so:

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
While `Packet` is a generic `superclass` of all Packets sent and received by the server you may freely cast the received packet parameter to the type expected as seen on line 2 of the above example.

`UpdatePacket update = (UpdatePacket)p;` 

This is the case with most all packet hoooking routines.

### **Example Command Hook**
We will now look at an example how you can hook a command to a callback method:
> Example from `JRelay.Core` plugin included with release.
```Java
@Override
public void attach() {
	user.hookCommand("hi", Core.class, "onHiCommand");		
}
```
We have hooked the text command `"hi"` to the callback function `onHiCommand()` within our plugin class `Core`. This means that when the user types `/hi` in chat in-game, the code within your plugin will be executed. Again it is important to ensure your class location matches the name of the compilation unit for your plugin. 

Like packet hooks, after we have hooked our command to its callback method we must then include the method itself within our plugin. This is accomplished by simply including a `public void` method that shares the same name ("onHiCommand") as the callback you specified in your hook. 

Unlike packet hook callbacks, command hook callbacks will be passed two parameters: A `String` containing the command invoked and a `String[]` containing the command and all arguments included with the command. It looks as follows:
> Example from `JRelay.Glow` plugin included with release.
>**Command arguments are space delimited**
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
### Understanding Packet Manipulation
Once you have set up your own plugin to run with **JRelay**, it is important to know a little about the process of packet capture and manipulation in order to create effective plugins.
> Assuming no NIO/Networking knowledge. Skip this if you are experienced.

### Capturing Packets
Packets are simple containers for data exchanged between the RotMG client and Deca's servers. Some packets are only sent from the client to the server while some are only sent from the server to the client. 

Each Packet's data fields and transmission source can be viewed under the `Packets` tab of the **JRelayGUI**. When capturing packets and manipulating their data there are a few important things to note:

1. Because packet hook callbacks are passed a generic untyped packet, The generic packet must be cast to the desired packet type. 
2. If you chose to capture **AND MODIFY** a packet you **MUST** explicitly send the modified packet to its required destination using  `sendToClient(Packet packet)` 
	or
 `sendToServer(Packet packet)` 

3. If you wish to capture a specific packet and **STOP** its transmission, change the Packet's `boolean send;` field to `false`.
4. Any packet can be created at any time using  `Packet.create(byte id)` or `Packet.create(PacketType type)` or by simply constructing a new packet object.
> `HelloPacket helloPacket = (HelloPacket) Packet.create(PacketType.HELLO);`

> `HelloPacket helloPacket = (HelloPacket) Packet.create(30);`
5. Sending the wrong packet to the wrong place or with unexpected data will disconnect you.
6. Spamming packets will get you **BANNED BY DECA***

# Useful Data and Fields
There are a number of extremely useful data collections in **JRelay** to help you write plugins. This section will detail the data you can access and the means by which to do so.
>All enumerations also contain a static hashmap of their values if you prefer to access the data that way

### GameData
The `GameData` class contains useful mappings of all of RotMG's out of the box xml data. 

These data sets are stored in Java `HashMap` objects. A `HashMap` is a one-to-one map of keys to values. When **JRelay** runs it will create object models of all entities within the game's XML. The XML of the entitity will be serialized into raw data and constructed into a model representing the entity. A map of the entity's ID(byte/int) **AND** name(String) to the Java model will be created. Once you have started **JRelay** and loaded all game assets, you can access any of this data statically within your plugins.

Be wary however, since you have full access to these maps, it is up to the user to handle cases where finding items by name or id return a null result.
```Java
public static HashMap<Integer, Item> items = new HashMap<Integer,Item>();
public static HashMap<String, Item> nameToItem = new HashMap<String,Item>();
public static HashMap<Integer, Tile> tiles = new HashMap<Integer,Tile>();
public static HashMap<String, Tile> nameToTile = new HashMap<String,Tile>();
public static HashMap<Integer, Object> objects = new HashMap<Integer, Object>();
public static HashMap<String, Object> nameToObject = new HashMap<String, Object>();
public static HashMap<Byte, PacketModel> packets = new HashMap<Byte,PacketModel>();
public static HashMap<String, Server> abbrToServer = new HashMap<String,Server>();
public static HashMap<String, Server> servers = new HashMap<String,Server>();
public static HashMap<Integer, PacketModel> packetIdToName = new HashMap<Integer,PacketModel>();
public static HashMap<String, Integer> packetNameToId = new HashMap<String,Integer>();
```
There are maps for `id -> model` and `name -> model` meaning that you can search for entities using their ID or Name.
This can be extremely useful when manipulating tiles and objects.
Example:
```Java
int sand = GameData.nameToTile.get("Light Sand").id;
int sand2 = GameData.nameToTile.get("Dark Sand").id;
```

### Packet Type
`PacketType` is an enumeration of RotMG's server and client packets referenced by packet ID.
```Java
FAILURE(0),
CREATESUCCESS(85),
CREATE(89),
PLAYERSHOOT(38),
MOVE(1),
PLAYERTEXT(86),
TEXT(4),
SERVERPLAYERSHOOT(75),
DAMAGE(9),
UPDATE(13),
UPDATEACK(15),
NOTIFICATION(52),
NEWTICK(101),
INVSWAP(16),
USEITEM(68),
SHOWEFFECT(74),
HELLO(92),
GOTO(51),
INVDROP(57),
INVRESULT(17),
RECONNECT(35),
PING(95),
PONG(65),
MAPINFO(98),
LOAD(69),
PIC(90),
SETCONDITION(39),
TELEPORT(60),
USEPORTAL(87),
DEATH(31),
BUY(25),
BUYRESULT(76),
AOE(28),
GROUNDDAMAGE(88),
PLAYERHIT(46),
ENEMYHIT(12),
AOEACK(63),
SHOOTACK(77),
OTHERHIT(62),
SQUAREHIT(26),
GOTOACK(96),
EDITACCOUNTLIST(37),
ACCOUNTLIST(20),
QUESTOBJID(93),
CHOOSENAME(41),
NAMERESULT(103),
CREATEGUILD(8),
GUILDRESULT(56),
GUILDREMOVE(45),
GUILDINVITE(84),
ALLYSHOOT(19),
ENEMYSHOOT(64),
REQUESTTRADE(3),
TRADEREQUESTED(55),
TRADESTART(10),
CHANGETRADE(33),
TRADECHANGED(66),
ACCEPTTRADE(24),
CANCELTRADE(97),
TRADEDONE(78),
TRADEACCEPTED(58),
CLIENTSTAT(100),
CHECKCREDITS(83),
ESCAPE(104),
FILE(59),
INVITEDTOGUILD(91),
JOINGUILD(7),
CHANGEGUILDRANK(22),
PLAYSOUND(27),
GLOBALNOTIFICATION(6),
RESKIN(67),
PETUPGRADEREQUEST(21),
ACTIVEPETUPDATEREQUEST(18),
ACTIVEPETUPDATE(80),
NEWABILITY(94),
PETYARDUPDATE(50),
EVOLVEPET(11),
DELETEPET(30),
HATCHPET(53),
ENTERARENA(102),
IMMINENTARENAWAVE(61),
ARENADEATH(34),
ACCEPTARENADEATH(5),
VERIFYEMAIL(36),
RESKINUNLOCK(40),
PASSWORDPROMPT(81),
QUESTFETCHASK(49),
QUESTREDEEM(44),
QUESTFETCHRESPONSE(48),
QUESTREDEEMRESPONSE(14),
PETCHANGEFORMMSG(99),
KEYINFOREQUEST(79),
KEYINFORESPONSE(82),
CLAIMLOGINREWARDMSG(47),
LOGINREWARDMSG(42),
QUESTROOMMSG(23);
```

## ConditionEffects & ConditionEffectIndex
`ConditionEffect` and `ConditionEffectIndex` are enumerations containing RotMG's status effects. 

They are both very similar but should be used in different cases. `ConditionEffect` values should be used in conjunction with `PlayerData`s `hasConditionEffect(ConditionEffect condition)` method. 

`ConditionEffectIndex` should be used to compare any packets `effect` field.
For instance:
```Java
//Check if the player's armor is broken
boolean armorBroken = user.playerData.hasConditionEffect(ConditionEffect.ArmorBroken);

//Simulate AOEPacket capture and check if it will break the players armor
AOEPacket aeo = new AOEPacket();
boolean armorBroken = false;
if (aeo.effect == ConditionEffectIndex.ArmorBroken.index) {
	armorBroken = true;
}
```

### EffectTypes
`EffectType` is an enumeration containing visual effects for RotMG. I've personally never played around with these but I beleive they are used in the server `ShowEffectPacket`s `effect` field.
```Java
Unknown(0), 
Heal(1), 
Teleport(2), 
Stream(3), 
Throw(4), 
Nova(5), 
Poison(6), 
Line(7), 
Burst(8), 
Flow(9), 
Ring(10), 
Lightning(11), 
Collapse(12), 
ConeBlast(13), 
Earthquake(14), 
Flash(15), 
BeachBall(16), 
ElectricBolts(17), 
ElectricFlashing(18), 
RisingFury(19);
```

### PetAbility
`PetAbility` is an enumeration of all avaiable pet abilities.
```Java
AttackClose(402),
AttackMid(404),
AttackFar(405),
Electric(406),
Heal(407),
MagicHeal(408),
Savage(409),
Decoy(410),
RisingFury(411);
```

### StatsType
`StatsType` is an type containing all possible fields that might be updated in an `UPDATE` packet. It also contains useful methods for comparing and checking Stats.
```Java
public static StatsType MaximumHP = new StatsType(0);
public static StatsType HP = new StatsType(1);
public static StatsType Size = new StatsType(2);
public static StatsType MaximumMP = new StatsType(3);
public static StatsType MP = new StatsType(4);
public static StatsType NextLevelExperience = new StatsType(5);
public static StatsType Experience = new StatsType(6);
public static StatsType Level = new StatsType(7);
public static StatsType Inventory0 = new StatsType(8);
public static StatsType Inventory1 = new StatsType(9);
public static StatsType Inventory2 = new StatsType(10);
public static StatsType Inventory3 = new StatsType(11);
public static StatsType Inventory4 = new StatsType(12);
public static StatsType Inventory5 = new StatsType(13);
public static StatsType Inventory6 = new StatsType(14);
public static StatsType Inventory7 = new StatsType(15);
public static StatsType Inventory8 = new StatsType(16);
public static StatsType Inventory9 = new StatsType(17);
public static StatsType Inventory10 = new StatsType(18);
public static StatsType Inventory11 = new StatsType(19);
public static StatsType Attack = new StatsType(20);
public static StatsType Defense = new StatsType(21);
public static StatsType Speed = new StatsType(22);
public static StatsType Vitality = new StatsType(26);
public static StatsType Wisdom = new StatsType(27);
public static StatsType Dexterity = new StatsType(28);
public static StatsType Effects = new StatsType(29);
public static StatsType Stars = new StatsType(30);
public static StatsType Name = new StatsType(31); //Is UTF
public static StatsType Texture1 = new StatsType(32);
public static StatsType Texture2 = new StatsType(33);
public static StatsType MerchandiseType = new StatsType(34);
public static StatsType Credits = new StatsType(35);
public static StatsType MerchandisePrice = new StatsType(36);
public static StatsType PortalUsable = new StatsType(37); 
public static StatsType AccountId = new StatsType(38); //Is UTF
public static StatsType AccountFame = new StatsType(39);
public static StatsType MerchandiseCurrency = new StatsType(40);
public static StatsType ObjectConnection = new StatsType(41);
public static StatsType MerchandiseRemainingCount = new StatsType(42);
public static StatsType MerchandiseRemainingMinutes = new StatsType(43);
public static StatsType MerchandiseDiscount = new StatsType(44);
public static StatsType MerchandiseRankRequirement = new StatsType(45);
public static StatsType HealthBonus = new StatsType(46);
public static StatsType ManaBonus = new StatsType(47);
public static StatsType AttackBonus = new StatsType(48);
public static StatsType DefenseBonus = new StatsType(49);
public static StatsType SpeedBonus = new StatsType(50);
public static StatsType VitalityBonus = new StatsType(51);
public static StatsType WisdomBonus = new StatsType(52);
public static StatsType DexterityBonus = new StatsType(53);
public static StatsType OwnerAccountId = new StatsType(54); //Is UTF
public static StatsType RankRequired = new StatsType(55);
public static StatsType NameChosen = new StatsType(56);
public static StatsType CharacterFame = new StatsType(57);
public static StatsType CharacterFameGoal = new StatsType(58);
public static StatsType Glowing = new StatsType(59);
public static StatsType SinkLevel = new StatsType(60);
public static StatsType AltTextureIndex = new StatsType(61);
public static StatsType GuildName = new StatsType(62); //Is UTF
public static StatsType GuildRank = new StatsType(63);
public static StatsType OxygenBar = new StatsType(64);
public static StatsType XpBoosterActive = new StatsType(65);
public static StatsType XpBoostTime = new StatsType(66);
public static StatsType LootDropBoostTime = new StatsType(67);
public static StatsType LootTierBoostTime = new StatsType(68);
public static StatsType HealthPotionCount = new StatsType(69);
public static StatsType MagicPotionCount = new StatsType(70);
public static StatsType Backpack0 = new StatsType(71);
public static StatsType Backpack1 = new StatsType(72);
public static StatsType Backpack2 = new StatsType(73);
public static StatsType Backpack3 = new StatsType(74);
public static StatsType Backpack4 = new StatsType(75);
public static StatsType Backpack5 = new StatsType(76);
public static StatsType Backpack6 = new StatsType(77);
public static StatsType Backpack7 = new StatsType(78);
public static StatsType HasBackpack = new StatsType(79);
public static StatsType Skin = new StatsType(80);
public static StatsType PetInstanceId = new StatsType(81);
public static StatsType PetName = new StatsType(82); //Is UTF
public static StatsType PetType = new StatsType(83);
public static StatsType PetRarity = new StatsType(84);
public static StatsType PetMaximumLevel = new StatsType(85);
public static StatsType PetFamily = new StatsType(86); 
public static StatsType PetPoints0 = new StatsType(87);
public static StatsType PetPoints1 = new StatsType(88);
public static StatsType PetPoints2 = new StatsType(89);
public static StatsType PetLevel0 = new StatsType(90);
public static StatsType PetLevel1 = new StatsType(91);
public static StatsType PetLevel2 = new StatsType(92);
public static StatsType PetAbilityType0 = new StatsType(93);
public static StatsType PetAbilityType1 = new StatsType(94);
public static StatsType PetAbilityType2 = new StatsType(95);
public static StatsType Effects2 = new StatsType(96); // Other Effects
public static StatsType FortuneTokens = new StatsType(97);
```
This is very useful for keeping track of certain aspects of the player anytime a packet containing a `Status` field is intercepted (UpdatePacket, NewTickPacket). 
The `Status` type contains a `StatData[]`. The `id` field of `StatData` can be compared with `StatsType` to cherry-pick the data you are looking for.

### BagType
`BagType` is an enumeration containing the different types of loot bags in RotMG.
```Java
Normal(0x500),
Purple(0x503),
Pink(0x506),
Cyan(0x509),
Red(0x510),
Blue(0x050B),
Purple2(0x507),
Egg(0x508),
White(0x050C),
White2(0x050E),
White3(0x50F);
```

### PlayerData
An instance of `PlayerData` is created when you connect to **JRelay.** `PlayerData` keeps track of all player related stats. All information in `PlayerData` is automatically kept up to date.
```Java
public int ownerObjectId;
public String mapName;
public boolean teleportAllowed;
public int mapWidth;
public int mapHeight;
public int maxHealth;
public int health;
public int maxMana;
public int mana;
public int xpGoal;
public int xp;
public int level = 1;
public int[] slot = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
public int[] backPack = { -1, -1, -1, -1, -1, -1, -1, -1 };
public int attack;
public int defense;
public int speed;
public int vitality;
public int wisdom;
public int dexterity;
public int effects;
public int stars;
public String name;
public int realmGold;
public int price;
public boolean canEnterPortal;
public String accountId;
public int accountFame;
public int healthBonus;
public int manaBonus;
public int attackBonus;
public int defenseBonus;
public int speedBonus;
public int vitalityBonus;
public int wisdomBonus;
public int dexterityBonus;
public int nameChangeRankRequired;
public boolean nameChosen;
public int characterFame;
public int characterFameGoal;
public boolean glowingEffect;
public String guildName;
public int guildRank;
public int breath;
public int healthPotionCount;
public int magicPotionCount;
public boolean hasBackpack;
public int skin;
public Location pos = new Location();
public CharacterClass cls;
```
# Event Utils
**JRelay** comes with a few helper methods for creating player text and notifications. These are `static` methods contained within the `EventUtils` class. Use event utils to make it easier to send chat and notifications to the client.

```Java
/* 
*@param objectId - game object ID to display the notification on. Commonly user.playerData.ownerObjectId
*@param message - notification text
*/
public static NotificationPacket createNotification(int objectId, String message) {}
/* 
*@param sender - the sender of the message to be displayed in chat.
*@param text - chat text
*/
public static TextPacket createText(String sender, String text) {}

//EXAMPLES
sendToClient(EventUtils.createNotification(user.playerData.ownerObjectId, "Example Notification"));

sendToClient(EventUtils.createText("sender", "Example Chat Message"));		
```

# Create Custom Tile & Object Maps
**JRelay** allows the user to define custom tile and object mappings allowing users to give the game their own personal look. **JRelay** object maps are written in XML and employ a custom syntax for managing mapping of multiple object to multiple other objects. All **JRelay** object maps are defined in `xml/maps.xml`.

Example Map:
```XML
<Maps>
  <Map id="tileMap">
    <TileMap />
    <Entry start="Light Sand:Dark Sand" end="OceanFloor" />
  </Map>
</Maps>
```
>This map replaces all tiles that are Light & Dark Sand with OceanFloor tile from OT.
>Result:
![alt text](https://i.imgur.com/UXaIwtZ.png)

### Creating Your Own Object Map Definition
1. To define your own map, within the `<Maps></Maps>`tag found in `xml/maps.xml`, create an entry of type `<Map></Map>`. If you wish to name your map specify its `id` using `<Map id="myCustomMap"></Map>`.

2. Choose weather your map will be an `ObjectMap` or a `TileMap`. Tile Maps are used to replace game tiles with other tiles while Object Maps are used to replace game object with other objects. Ex. You cant change Sand to be Medusas, the game wont like you very much. Add your corresponding map type entry to your `<Map>` as `<TileMap/>` or `<ObjectMap/>`.

3. Underneath your map type declaration add a tag type `<Entry/>`. The entry tag has two attributes: `start` and `end`. Both tags are required and should be filled with corresponding tiles and object mapping syntax. You can have as many entries per map as you like.

4. If you want to create a TileMap check out the 'xml/tiles.xml' for some neat substitutions for tiles in game

5. For Object maps, anything and everything might go. ObjectMaps carry a high risk of getting disconnected... Especially if you replace all nexus tiles with Medusas

### Object Map Syntax
`JRelay` object maps use a custom syntax to make your life easier. There are three main parts to object map syntax.

- The `:` operator denotes an "AND" operation. Restricted to `start` tag of your entry. Replace multiple objects at once.
- The `#` operator denotes an "OR" operation. Restricted to `end` tag or your entry. Define multiple replacing objects. Each object separated has `1/n` chance of replacing the object defined in `start` where `n = # of objects`.
- The `*` operator denotes a "Wildcard" operator. Meaning you can replace ALL tiles or ALL objects with specified object(s),



