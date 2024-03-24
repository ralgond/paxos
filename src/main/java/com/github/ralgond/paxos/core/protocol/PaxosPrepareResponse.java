package com.github.ralgond.paxos.core.protocol;

import com.github.ralgond.paxos.core.common.PaxosAccepted;

import java.util.Objects;

public class PaxosPrepareResponse {
    public Integer serverId;

    public Long paxosId;

    public Long proposalId;

    public PaxosAccepted accepted;

    public boolean promised;

    public PaxosPrepareResponse(Integer serverId, Long paxosId, Long proposalId, PaxosAccepted accepted, boolean promised) {
        this.serverId = serverId;
        this.paxosId = paxosId;
        this.proposalId = proposalId;
        this.accepted = accepted;
        this.promised = promised;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosPrepareResponse that = (PaxosPrepareResponse) o;
        return promised == that.promised && Objects.equals(serverId, that.serverId) && Objects.equals(paxosId, that.paxosId) && Objects.equals(proposalId, that.proposalId) && Objects.equals(accepted, that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, paxosId, proposalId, accepted, promised);
    }

    @Override
    public String toString() {
        return "PaxosPrepareResponse{" +
                "serverId=" + serverId +
                ", paxosId=" + paxosId +
                ", proposalId=" + proposalId +
                ", accepted=" + accepted +
                ", promised=" + promised +
                '}';
    }
}
