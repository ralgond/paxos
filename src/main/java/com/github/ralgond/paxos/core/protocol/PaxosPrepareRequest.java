package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

import java.util.Objects;

public class PaxosPrepareRequest {
    public Integer serverId;

    public Long paxosId;

    public Long proposalId;

    public PaxosPrepareRequest(Integer serverId, Long paxosId, Long proposalId) {
        this.serverId = serverId;
        this.paxosId = paxosId;
        this.proposalId = proposalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosPrepareRequest request = (PaxosPrepareRequest) o;
        return Objects.equals(serverId, request.serverId) && Objects.equals(paxosId, request.paxosId) && Objects.equals(proposalId, request.proposalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, paxosId, proposalId);
    }

    @Override
    public String toString() {
        return "PaxosPrepareRequest{" +
                "serverId=" + serverId +
                ", paxosId=" + paxosId +
                ", proposalId=" + proposalId +
                '}';
    }
}
