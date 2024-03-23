package com.github.ralgond.paxos.core.env;

public class PaxosEnvironment {

    public PaxosConfig config;

    public PaxosPacketSender sender;

    public PaxosPersistent persistent;

    public PaxosTimerManager timer_manager;

    public PaxosEnvironment(PaxosConfig config, PaxosPacketSender sender, PaxosPersistent persistent, PaxosTimerManager timer_manager) {
        this.config = config;
        this.sender = sender;
        this.persistent = persistent;
        this.timer_manager = timer_manager;
    }
}
