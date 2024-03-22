package com.github.ralgond.paxos.core.protocol;

public class PaxosAcceptResponse {
    public Integer server_id;

    public Long proposal_id;

    public Long promised_proposal_id;

    public PaxosAcceptResponse(Integer server_id, Long proposal_id, Long promised_proposal_id) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.promised_proposal_id = promised_proposal_id;
    }

    /**
     * Try to send prepare request if result is False.
     * @return
     */
    public boolean isAccepted() {
        if (this.promised_proposal_id < 0) {
            return false;
        }
        return this.proposal_id >= this.promised_proposal_id;
    }
}
