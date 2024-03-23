package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosAccepted;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "PaxosPrepareResponse{" +
                "server_id=" + server_id +
                ", proposal_id=" + proposal_id +
                ", proposal_id_on_acceptor=" + proposal_id_on_acceptor +
                ", accepted=" + accepted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosPrepareResponse that = (PaxosPrepareResponse) o;
        return Objects.equals(server_id, that.server_id) && Objects.equals(proposal_id, that.proposal_id) && Objects.equals(proposal_id_on_acceptor, that.proposal_id_on_acceptor) && Objects.equals(accepted, that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server_id, proposal_id, proposal_id_on_acceptor, accepted);
    }
}
