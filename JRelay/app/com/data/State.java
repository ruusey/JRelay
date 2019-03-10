package com.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.packets.client.HelloPacket;
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
    public HelloPacket lastHello = null;
    public ReconnectPacket lastReconnect = null;
    public BiMap<String, Object> state;

    public State(User user, String id)
    {
        this.user=user;
        GUID = id;
        state = HashBiMap.create();
    }

    public void setState(String stateName,Object o){
    	state.forcePut(stateName,  o);
    }
    public Object getState(String stateName){
    	return state.getOrDefault(stateName, null);
    }
    
}
