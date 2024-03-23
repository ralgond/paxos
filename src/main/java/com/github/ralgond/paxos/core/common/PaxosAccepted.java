package com.github.ralgond.paxos.core.common;

public class PaxosAccepted {
    public Long proposal;

    public PaxosValue value;

    public PaxosAccepted() {
        this.proposal = -1L;
        this.value = new PaxosValue();
    }

    public PaxosAccepted(Long proposal, PaxosValue value) {
        this.proposal = proposal;
        this.value = value;
    }

    public boolean isNone() {
        return this.proposal < 0L;
    }
}
