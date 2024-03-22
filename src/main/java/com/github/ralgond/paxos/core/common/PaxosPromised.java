package com.github.ralgond.paxos.core.common;

public class PaxosPromised {
    public Long paxos_id;

    public Long proposal_id;

    public PaxosValue value;

    public PaxosPromised() {
        this.paxos_id = -1L;
        this.proposal_id = -1L;
        this.value = new PaxosValue();
    }

    public PaxosPromised(Long paxos_id, Long proposal_id, PaxosValue value) {
        this.paxos_id = paxos_id;
        this.proposal_id = proposal_id;
        this.value = value;
    }
}
