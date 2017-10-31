![alt text](https://i.imgur.com/K7EkJkY.png)
# **JRelay**  1.0.0 - RotMG X18.0.0

A modular Java man in the middle proxy for the flash game **Realm of the Mad God**
![alt text](https://i.imgur.com/8CJnRVb.png)

# Overview
**JRelay** is a man in the middle proxy for the popular flash browser game Realm of Mad God. **JRelay's** intended use is for users to create plugins that intercept and modify the games data which is transmitted in objects called Packets. JRelay is written in Java meaning it is platform independent so long as you have a compatible JRE installed on your Operating System. **JRelay requires Java 1.8+ to run.** 

## Project Structure
**JRelay** is built using basic `Java Socket IO` to bridge the gap between your game client and Deca's servers. **JRelay** incorporates aspects of existing RotMG proxies bundled with the platform independence of Java. **JRelay** makes use of a modular plugin system to allow third parties to write their own programs to capture and manipulate the game's data. 

## Windows Users:
Please feel free to use the following pre-written batch script as well as add your own custom JVM arguments:
```
java -jar /path/to/JRelay.jar
pause 
```
For Windows Users the batch script can be executed from any location so long as the location of `JRelay.jar` is correct in terms of its absolute or relative path.

## Mac/Solaris/UNIX/Linux Users:
After confirming you have **Java 1.8+** installed on your machine you should have no issues simply executing `JRelay.jar`, you may however create shorcuts or other execution scripts for you specific environment depending on your needs.

# Plugin Creation
As mentioned, **JRelay** supports the implementation of User created plugins. The support for users to create thier own plugins will be made avaialable through acessing the distributed `JRelayLib.jar` library. `JRelayLib` contains all essential data and methods neccesarry to create your very own **JRelay** plugins.

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

**9)** Deployment. To incorporate your plugin into **JRelay** simply place its .class or .java file in the **JRelay** `plugins` folder.


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

## Capturing Packets
Packets are containers for data exchanged between your RotMG client and Deca's servers. Some packets are only sent from the client to the server while some are only sent from the server to the client. The respective packets and their transmission source can be viewed under the `Packets` tab of the JRelayGUI. If you chose to capture packets and modify their data or stop their transmission there are a few important things to note:
1. Because packet hook callbacks are passed a generic untyped packet, The generic packet must be cast to the desired packet type. 
2. If you chose to capture **AND MODIFY** a packet you **MUST** explicitly send the modified packet to its required destination using  `sendToClient(Packet packet)` and `sendToServer(Packet packet)` which are superclass methods of `JPlugin`.
3. If you wish to capture a specific packet and **STOP** its transmission change the Packet's `boolean send;` field to `false`
4. Any packet can be created at any time using  `Packet.create(byte id)` or `Packet.create(PacketType type)` or by simply constructing a new packet object.
> `HelloPacket helloPacket = (HelloPacket) Packet.create(PacketType.HELLO);`
> `HelloPacket helloPacket = (HelloPacket) Packet.create(30);`
5. Sending the wrong packet to the wrong place or with unexpected data will disconnect you.
6. Spamming packets will get you **BANNED BY DECA***

# Useful Data and Fields
There are a number of extremely useful data collections in **JRelay** to help you write plugins. This section will detail the data you can access and the means by which to do so.
>All enumerations also contain a static hashmap of their values if you prefer to access the data that way

## GameData
The `GameData` class contains useful mappings of all of RotMG's out of the box xml data. These data sets are stored in Java `HashMap` objects. A `HashMap` is a one-to-one map of keys to values. When **JRelay** runs it will create object models of all entities within the game's XML. The XML of the entitity will be serialized into raw data and constructed into a model representing the entity. A map of the entity's ID(byte/int) **AND** name(String) to the Java model will be created. Once you have started **JRelay** and loaded all game assets, you can access any of this data statically within your plugins.
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

## Packet Type
`PacketType` is an enumeration of RotMG's server and client packets referenced by packet ID.
```Java
FAILURE(0),
CREATESUCCESS(87),
CREATE(7),
PLAYERSHOOT(82),
MOVE(51),
PLAYERTEXT(84),
TEXT(23),
SERVERPLAYERSHOOT(39),
DAMAGE(36),
UPDATE(79),
UPDATEACK(80),
NOTIFICATION(26),
NEWTICK(85),
INVSWAP(59),
USEITEM(49),
SHOWEFFECT(21),
HELLO(30),
GOTO(78),
INVDROP(102),
INVRESULT(9),
RECONNECT(1),
PING(97),
PONG(103),
MAPINFO(83),
LOAD(3),
PIC(64),
SETCONDITION(5),
TELEPORT(31),
USEPORTAL(48),
DEATH(91),
BUY(56),
BUYRESULT(67),
AOE(40),
GROUNDDAMAGE(98),
PLAYERHIT(35),
ENEMYHIT(76),
AOEACK(8),
SHOOTACK(27),
OTHERHIT(89),
SQUAREHIT(66),
GOTOACK(99),
EDITACCOUNTLIST(100),
ACCOUNTLIST(93),
QUESTOBJID(44),
CHOOSENAME(10),
NAMERESULT(88),
CREATEGUILD(57),
GUILDRESULT(13),
GUILDREMOVE(77),
GUILDINVITE(33),
ALLYSHOOT(41),
ENEMYSHOOT(75),
REQUESTTRADE(60),
TRADEREQUESTED(63),
TRADESTART(46),
CHANGETRADE(47),
TRADECHANGED(68),
ACCEPTTRADE(20),
CANCELTRADE(25),
TRADEDONE(17),
TRADEACCEPTED(94),
CLIENTSTAT(6),
CHECKCREDITS(62),
ESCAPE(37),
FILE(96),
INVITEDTOGUILD(101),
JOINGUILD(61),
CHANGEGUILDRANK(53),
PLAYSOUND(19),
GLOBALNOTIFICATION(22),
RESKIN(69),
PETUPGRADEREQUEST(55),
ACTIVEPETUPDATEREQUEST(4),
ACTIVEPETUPDATE(38),
NEWABILITY(12),
PETYARDUPDATE(24),
EVOLVEPET(50),
DELETEPET(15),
HATCHPET(14),
ENTERARENA(86),
IMMINENTARENAWAVE(92),
ARENADEATH(74),
ACCEPTARENADEATH(18),
VERIFYEMAIL(34),
RESKINUNLOCK(95),
PASSWORDPROMPT(81),
QUESTFETCHASK(104),
QUESTREDEEM(52),
QUESTFETCHRESPONSE(28),
QUESTREDEEMRESPONSE(65),
PETCHANGEFORMMSG(16),
KEYINFOREQUEST(90),
KEYINFORESPONSE(11),
CLAIMLOGINREWARDMSG(45),
LOGINREWARDMSG(42),
QUESTROOMMSG(58);
```

## ConditionEffects & ConditionEffectIndex
`ConditionEffect` and `ConditionEffectIndex` are enumerations containing RotMG's status effects. They are both very similar but should be used in different cases. `ConditionEffect` values should be used in conjunction with `PlayerData`s `hasConditionEffect(ConditionEffect condition)` method. `ConditionEffectIndex` should be used to compare any packets `effect` field.
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

## EffectTypes
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

## PetAbility
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

## StatsType
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
This is very useful for keeping track of certain aspects of the player anytime a packet containing a `Status` field is intercepted (UpdatePacket, NewTickPacket). The `Status` type contains a `StatData[]`. The `id` field of `StatData` can be compared with `StatsType` to cherry-pick the data you are looking for.

## BagType
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

## PlayerData
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

# Create Custom Tile & Object Maps
JRelay allows the user to define custom tile and object mappings allowing users to give the game their own personal look. JRelay object maps are written in XML and employ a custom syntax for managing mapping of multiple object to multiple other objects. All JRelay object maps are defined in `xml/maps.xml`.

Example Map:
```XML
<Map id="myMap">
	<TileMap/>
	<Entry start="Light Sand:Dark Sand" end="Castle Stone Floor Tile#Wood Panel Floor"/>
</Map>
```
>This map replaces all tiles that are Light & Dark Sand with Castle Stone Floor OR Wood Panel Floor.

## Creating Your Own Object Map Definition
1. To define your own map, within the `<Maps></Maps>`tag found in `xml/maps.xml`, create an entry of type `<Map></Map>`. If you wish to name your map specify its `id` using `<Map id="myCustomMap"></Map>`.
2. Choose weather your map will be an `ObjectMap` or a `TileMap`. Tile Maps are used to replace game tiles with other tiles while Object Maps are used to replace game object with other objects. Ex. You cant change Sand to be Medusas, the game wont like you very much. Add your corresponding map type entry to your `<Map>` as `<TileMap/>` or `<ObjectMap/>`.
3. Underneath your map type declaration add a tag type `<Entry/>`. The entry tag has two attributes: `start` and `end`. Both tags are required and should be filled with corresponding tiles and object mapping syntax. You can have as many entries per map as you like.

## Object Map Syntax
`JRelay` object maps use a custom syntax to make your life easier. There are three main parts to object map syntax. 
- The `:` operator denotes an "AND" operation. Restricted to `start` tag of your entry. Replace multiple objects at once.
- The `#` operator denotes an "OR" operation. Restricted to `end` tag or your entry. Define multiple replacing objects. Each object separated has `1/n` chance of replacing the object defined in `start` where `n = # of objects`.
- The `*` operator denotes a "Wildcard" operator. Meaning you can replace ALL tiles or ALL objects with specified object(s),



