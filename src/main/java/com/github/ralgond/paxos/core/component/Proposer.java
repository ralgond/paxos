package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public class Proposer {
    public interface StateMachine {
        public void onTimeout();
        public void onRecvPrepareResponse(PaxosPrepareResponse resp);
        public void onRecvAcceptResponse(PaxosAcceptResponse resp);
    }

    public PaxosEnvironment env;
    public StateMachine state_machine;

    public void start(PaxosEnvironment env) {
        this.env = env;
        this.env.persistent.getAcceptedMaxVersion();
    }
}
