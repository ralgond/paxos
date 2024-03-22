package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
import com.github.ralgond.paxos.core.common.PaxosPromised;
import com.github.ralgond.paxos.core.common.PaxosValue;

public class PaxosPrepareResponse {
    public Integer server_id;

    public Long proposal_id;

    public Long proposal_id_on_acceptor;

    public PaxosAccepted accepted;

    public PaxosPrepareResponse(Integer server_id, Long proposal_id, Long proposal_id_on_acceptor, PaxosAccepted accepted) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
        this.proposal_id_on_acceptor = proposal_id_on_acceptor;
        this.accepted = accepted;
    }

    public boolean isPromised() {
        return (proposal_id > proposal_id_on_acceptor);
    }
}
