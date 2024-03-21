package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareResponse {
    public Integer server_id;

    public Long proposal_id;

    public Long promised_proposal_id;

    public PaxosValue promised_value;

    public PaxosPrepareResponse(Integer server_id, Long proposal_id, Long promised_proposal_id, PaxosValue promised_value) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.promised_proposal_id = promised_proposal_id;
        this.promised_value = promised_value;
    }
}
