package com.github.ralgond.paxos.core.protocol;

public class PaxosAcceptRequest {
    public Integer server_id;

    public Long proposal_id;

    public PaxosAcceptRequest(Integer server_id, Long proposal_id) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
    }
}
