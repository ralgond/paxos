package com.github.ralgond.paxos.core.env;

import java.util.HashMap;

public interface PaxosConfig {
    public Integer getServerId();

    public HashMap<Integer, ServerConfig> getServers();
}
