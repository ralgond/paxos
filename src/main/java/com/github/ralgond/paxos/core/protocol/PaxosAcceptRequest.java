package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosAcceptRequest {
    public Integer server_id;

    public Long paxos_id;

    public Long proposal_id;

    public PaxosValue proposal_value;

    public PaxosAcceptRequest(Integer server_id, Long paxos_id, Long proposal_id, PaxosValue proposal_value) {
        this.server_id = server_id;
        this.paxos_id = paxos_id;
        this.proposal_id = proposal_id;
        this.proposal_value = proposal_value;
    }
}
