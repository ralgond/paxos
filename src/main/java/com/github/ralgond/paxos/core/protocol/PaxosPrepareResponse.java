package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosPromised;
import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareResponse {
    public Integer server_id;

    public Long proposal_id;

    public PaxosPromised promised;

    public PaxosPrepareResponse(Integer server_id, Long proposal_id, PaxosPromised promised) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.promised = promised;
    }

    public boolean isPromised() {
        return this.proposal_id.equals(this.promised.proposal_id);
    }
}
