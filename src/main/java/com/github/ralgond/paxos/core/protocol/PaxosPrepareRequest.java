package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

import java.util.Objects;

public class PaxosPrepareRequest {
    public Integer server_id;

    public Long proposal_id;

    public PaxosPrepareRequest(Integer server_id, Long proposal_id) {
        this.server_id = server_id;
        this.proposal_id = proposal_id;
    }

    @Override
    public String toString() {
        return "PaxosPrepareRequest{" +
                "server_id=" + server_id +
                ", proposal_id=" + proposal_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosPrepareRequest request = (PaxosPrepareRequest) o;
        return Objects.equals(server_id, request.server_id) && Objects.equals(proposal_id, request.proposal_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server_id, proposal_id);
    }
}
