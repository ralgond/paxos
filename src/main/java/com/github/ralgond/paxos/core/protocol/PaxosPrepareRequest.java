package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareRequest {
    public Integer server_id;

    public Long proposal_id;

    public Long paxos_id;

    public PaxosValue proposal_value;

    public PaxosPrepareRequest(Integer server_id, Long proposal_id, Long paxos_id, PaxosValue proposal_value) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.paxos_id = paxos_id;
        this.proposal_value = proposal_value;
    }
}
