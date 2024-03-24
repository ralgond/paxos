package com.github.ralgond.paxos.core.env;

import java.util.TreeMap;

public interface PaxosConfig {
    Integer getServerId();

    TreeMap<Integer, ServerConfig> getServers();

    public default int getMajoritySize() {
        return this.getServers().size() / 2 + 1;
    }
}
