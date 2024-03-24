package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosValue;

import java.util.Objects;

public class PaxosAcceptRequest {
    public Integer serverId;

    public long paxosId;

    public Long proposalId;

    public PaxosValue proposalValue;

    public PaxosAcceptRequest(Integer serverId, long paxosId, Long proposalId, PaxosValue proposalValue) {
        this.serverId = serverId;
        this.paxosId = paxosId;
        this.proposalId = proposalId;
        this.proposalValue = proposalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosAcceptRequest that = (PaxosAcceptRequest) o;
        return paxosId == that.paxosId && Objects.equals(serverId, that.serverId) && Objects.equals(proposalId, that.proposalId) && Objects.equals(proposalValue, that.proposalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, paxosId, proposalId, proposalValue);
    }

    @Override
    public String toString() {
        return "PaxosAcceptRequest{" +
                "serverId=" + serverId +
                ", paxosId=" + paxosId +
                ", proposalId=" + proposalId +
                ", proposalValue=" + proposalValue +
                '}';
    }
}
