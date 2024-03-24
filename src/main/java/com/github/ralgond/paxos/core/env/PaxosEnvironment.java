package com.github.ralgond.paxos.core.env;

public class PaxosEnvironment {

    public PaxosConfig config;

    public PaxosPacketSender sender;

    public PaxosPersistent persistent;

    public PaxosTimerManager timerManager;

    public PaxosEnvironment(PaxosConfig config, PaxosPacketSender sender, PaxosPersistent persistent, PaxosTimerManager timerManager) {
        this.config = config;
        this.sender = sender;
        this.persistent = persistent;
        this.timerManager = timerManager;
    }
}
