package com.github.ralgond.paxos.core.env;

public class PaxosEnvironmentTest extends PaxosEnvironment {

    public PaxosEnvironmentTest(PaxosConfig config, PaxosPacketSenderTest sender,
                                PaxosPersistent persistent, PaxosTimerManager timer_manager) {
        super(config, sender, persistent, timer_manager);
    }
}
