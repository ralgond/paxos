package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareRequest {
    public Integer server_id;

    public Long proposal_id;

    public Long value_version;

    public PaxosValue proposal_value;

    public PaxosPrepareRequest(Integer server_id, Long proposal_id, Long value_version, PaxosValue proposal_value) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.value_version = value_version;
        this.proposal_value = proposal_value;
    }
}
