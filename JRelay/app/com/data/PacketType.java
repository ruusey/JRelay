package com.data;

public enum PacketType {
	FAILURE(0),
	CREATESUCCESS(22),
	CREATE(74),
	PLAYERSHOOT(52),
	MOVE(11),
	PLAYERTEXT(37),
	TEXT(88),
	SERVERPLAYERSHOOT(63),
	DAMAGE(21),
	UPDATE(27),
	UPDATEACK(26),
	NOTIFICATION(56),
	NEWTICK(100),
	INVSWAP(82),
	USEITEM(30),
	SHOWEFFECT(34),
	HELLO(10),
	GOTO(58),
	INVDROP(60),
	INVRESULT(85),
	RECONNECT(44),
	PING(77),
	PONG(14),
	MAPINFO(59),
	LOAD(3),
	PIC(84),
	SETCONDITION(12),
	TELEPORT(24),
	USEPORTAL(40),
	DEATH(35),
	BUY(81),
	BUYRESULT(45),
	AOE(79),
	GROUNDDAMAGE(93),
	PLAYERHIT(49),
	ENEMYHIT(69),
	AOEACK(83),
	SHOOTACK(48),
	OTHERHIT(16),
	SQUAREHIT(101),
	GOTOACK(6),
	EDITACCOUNTLIST(78),
	ACCOUNTLIST(80),
	QUESTOBJID(89),
	CHOOSENAME(95),
	NAMERESULT(98),
	CREATEGUILD(104),
	GUILDRESULT(55),
	GUILDREMOVE(8),
	GUILDINVITE(75),
	ALLYSHOOT(62),
	ENEMYSHOOT(94),
	REQUESTTRADE(46),
	TRADEREQUESTED(91),
	TRADESTART(31),
	CHANGETRADE(87),
	TRADECHANGED(19),
	ACCEPTTRADE(42),
	CANCELTRADE(15),
	TRADEDONE(9),
	TRADEACCEPTED(5),
	CLIENTSTAT(96),
	CHECKCREDITS(68),
	ESCAPE(53),
	FILE(65),
	INVITEDTOGUILD(28),
	JOINGUILD(99),
	CHANGEGUILDRANK(102),
	PLAYSOUND(51),
	GLOBALNOTIFICATION(18),
	RESKIN(36),
	PETUPGRADEREQUEST(47),
	ACTIVEPETUPDATEREQUEST(50),
	ACTIVEPETUPDATE(13),
	NEWABILITY(103),
	PETYARDUPDATE(20),
	EVOLVEPET(25),
	DELETEPET(38),
	HATCHPET(92),
	ENTERARENA(76),
	IMMINENTARENAWAVE(17),
	ARENADEATH(61),
	ACCEPTARENADEATH(1),
	VERIFYEMAIL(57),
	RESKINUNLOCK(97),
	PASSWORDPROMPT(64),
	QUESTFETCHASK(23),
	QUESTREDEEM(39),
	QUESTFETCHRESPONSE(90),
	QUESTREDEEMRESPONSE(4),
	PETCHANGEFORMMSG(7),
	KEYINFOREQUEST(66),
	KEYINFORESPONSE(41),
	CLAIMLOGINREWARDMSG(33),
	LOGINREWARDMSG(86),
	QUESTROOMMSG(67);
	public int id;
	PacketType(int id){
		this.id=id;
	}
	
}
