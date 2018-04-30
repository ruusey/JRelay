package com.data;

import java.util.HashMap;

import com.packets.server.ReconnectPacket;
import com.relay.JRelay;
import com.relay.User;

public class State
{
    public User user;
    public String GUID;
    public String accId;
    public String locationName;
    public byte[] conRealKey = new byte[0];
    public String conTargetAddress = JRelay.instance.remoteHost;
    public int conTargetPort = 2050;

    public ReconnectPacket lastRealm = null;
    public ReconnectPacket lastDungeon = null;

    public HashMap<String, Object> states;

    public State(User user, String id)
    {
        this.user=user;
        GUID = id;
        states = new HashMap<String, Object>();
    }

    public void setState(String stateName,Object o){
    	states.put(stateName, o);
    }
    public Object getState(String stateName){
    	return states.get(stateName);
    }
    
}
