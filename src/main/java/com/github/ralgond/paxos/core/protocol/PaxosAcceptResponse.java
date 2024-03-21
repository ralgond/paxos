package com.github.ralgond.paxos.core.protocol;

public class PaxosAcceptResponse {
    public Integer server_id;

    public Long proposal_id;

    public Long promised_id;

    public PaxosAcceptResponse(Integer server_id, Long proposal_id, Long promised_id) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.promised_id = promised_id;
    }
}
