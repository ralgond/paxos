package com.github.ralgond.paxos.core.env;

public class ServerConfig {
    public Integer server_id;

    public String server_ip_addr;

    public String rack;

    public ServerConfig(Integer server_id, String server_ip_addr, String rack) {
        this.server_id = server_id;
        this.server_ip_addr = server_ip_addr;
        this.rack = rack;
    }
}
