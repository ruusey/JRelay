package com.data;

public enum PacketType {
	FAILURE(0),
	CREATESUCCESS(44),
	CREATE(52),
	PLAYERSHOOT(8),
	MOVE(74),
	PLAYERTEXT(59),
	TEXT(20),
	SERVERPLAYERSHOOT(5),
	DAMAGE(58),
	UPDATE(31),
	UPDATEACK(80),
	NOTIFICATION(47),
	NEWTICK(36),
	INVSWAP(27),
	USEITEM(39),
	SHOWEFFECT(28),
	HELLO(100),
	GOTO(76),
	INVDROP(46),
	INVRESULT(82),
	RECONNECT(12),
	PING(4),
	PONG(86),
	MAPINFO(85),
	LOAD(62),
	PIC(50),
	SETCONDITION(19),
	TELEPORT(99),
	USEPORTAL(91),
	DEATH(104),
	BUY(68),
	BUYRESULT(87),
	AOE(18),
	GROUNDDAMAGE(84),
	PLAYERHIT(67),
	ENEMYHIT(96),
	AOEACK(102),
	SHOOTACK(3),
	OTHERHIT(25),
	SQUAREHIT(95),
	GOTOACK(81),
	EDITACCOUNTLIST(21),
	ACCOUNTLIST(1),
	QUESTOBJID(7),
	CHOOSENAME(9),
	NAMERESULT(13),
	CREATEGUILD(40),
	GUILDRESULT(65),
	GUILDREMOVE(101),
	GUILDINVITE(75),
	ALLYSHOOT(63),
	ENEMYSHOOT(92),
	REQUESTTRADE(6),
	TRADEREQUESTED(14),
	TRADESTART(15),
	CHANGETRADE(26),
	TRADECHANGED(93),
	ACCEPTTRADE(17),
	CANCELTRADE(55),
	TRADEDONE(77),
	TRADEACCEPTED(57),
	CLIENTSTAT(66),
	CHECKCREDITS(35),
	ESCAPE(41),
	FILE(42),
	INVITEDTOGUILD(83),
	JOINGUILD(94),
	CHANGEGUILDRANK(51),
	PLAYSOUND(61),
	GLOBALNOTIFICATION(90),
	RESKIN(69),
	PETUPGRADEREQUEST(11),
	ACTIVEPETUPDATEREQUEST(53),
	ACTIVEPETUPDATE(56),
	NEWABILITY(60),
	PETYARDUPDATE(38),
	EVOLVEPET(97),
	DELETEPET(78),
	HATCHPET(22),
	ENTERARENA(89),
	IMMINENTARENAWAVE(23),
	ARENADEATH(34),
	ACCEPTARENADEATH(10),
	VERIFYEMAIL(49),
	RESKINUNLOCK(98),
	PASSWORDPROMPT(48),
	QUESTFETCHASK(16),
	QUESTREDEEM(24),
	QUESTFETCHRESPONSE(79),
	QUESTREDEEMRESPONSE(30),
	PETCHANGEFORMMSG(64),
	KEYINFOREQUEST(45),
	KEYINFORESPONSE(33),
	CLAIMLOGINREWARDMSG(103),
	LOGINREWARDMSG(37),
	QUESTROOMMSG(88),
	PETCHANGESKINMSG(105),
	REALMHEROLEFTMSG(111),
	RESETDAILYQUESTS(112);

	public int id;
	PacketType(int id){
		this.id=id;
	}
	
}
