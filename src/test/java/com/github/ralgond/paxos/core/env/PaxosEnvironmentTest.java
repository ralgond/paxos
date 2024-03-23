package com.github.ralgond.paxos.core.env;

public class PaxosEnvironmentTest extends PaxosEnvironment {

    public PaxosEnvironmentTest(PaxosConfig config, PaxosPacketSenderTest sender, PaxosPersistent persistent) {
        super(config, sender, persistent);
    }
}
