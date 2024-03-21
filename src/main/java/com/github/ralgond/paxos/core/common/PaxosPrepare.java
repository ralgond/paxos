package com.github.ralgond.paxos.core.common;

public class PaxosPrepare {

    public Long promised_proposal_id;

    public PaxosValue value;

    public PaxosPrepare() {
        this.promised_proposal_id = -1L;
        this.value = new PaxosValue();
    }

    public PaxosPrepare(Long promised_proposal_id, PaxosValue value) {
        this.promised_proposal_id = promised_proposal_id;
        this.value = value;
    }
}
