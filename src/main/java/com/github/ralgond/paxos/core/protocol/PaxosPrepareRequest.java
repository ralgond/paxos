package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareRequest {
    public Integer server_id;

    public Long proposal_id;

    public PaxosPrepareRequest(Integer server_id, Long proposal_id) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
    }

    @Override
    public String toString() {
        return "PaxosPrepareRequest{" +
                "server_id=" + server_id +
                ", proposal_id=" + proposal_id +
                '}';
    }
}
