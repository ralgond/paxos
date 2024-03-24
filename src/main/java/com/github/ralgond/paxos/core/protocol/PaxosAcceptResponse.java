package com.github.ralgond.paxos.core.protocol;

import java.util.Objects;

public class PaxosAcceptResponse {
    public Integer serverId;

    public Long paxosId;

    public Long proposalId;

    public Long promisedProposalId;

    public boolean accepted;

    public PaxosAcceptResponse(Integer serverId, Long paxosId, Long proposalId, Long promisedProposalId, boolean accepted) {
        this.serverId = serverId;
        this.paxosId = paxosId;
        this.proposalId = proposalId;
        this.promisedProposalId = promisedProposalId;
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosAcceptResponse that = (PaxosAcceptResponse) o;
        return accepted == that.accepted && Objects.equals(serverId, that.serverId) && Objects.equals(paxosId, that.paxosId) && Objects.equals(proposalId, that.proposalId) && Objects.equals(promisedProposalId, that.promisedProposalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, paxosId, proposalId, promisedProposalId, accepted);
    }

    @Override
    public String toString() {
        return "PaxosAcceptResponse{" +
                "serverId=" + serverId +
                ", paxosId=" + paxosId +
                ", proposalId=" + proposalId +
                ", promisedProposalId=" + promisedProposalId +
                ", accepted=" + accepted +
                '}';
    }
}
