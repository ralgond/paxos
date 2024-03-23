package com.github.ralgond.paxos.core.env;

import java.util.TreeMap;

public class PaxosConfigTest implements PaxosConfig {

    TreeMap<Integer, ServerConfig> servers;

    public PaxosConfigTest() {
        servers = new TreeMap<>();
        servers.put(1, new ServerConfig(1, "192.168.0.101", "1"));
        servers.put(2, new ServerConfig(2, "192.168.0.102", "2"));
        servers.put(3, new ServerConfig(3, "192.168.0.103", "3"));
    }

    @Override
    public Integer getServerId() {
        return 1;
    }

    @Override
    public TreeMap<Integer, ServerConfig> getServers() {
        return servers;
    }
}
