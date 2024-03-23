package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccepted;

import java.util.TreeMap;

public class PaxosPersistentTest implements PaxosPersistent {

    public Long serialNumber;

    public Long proposalIdOnProposer;

    public Long proposalIdOnAcceptor;

    public TreeMap<Long, PaxosAccepted> acceptedMap;

    public PaxosPersistentTest() {
        this.serialNumber = 0L;
        this.proposalIdOnProposer = -1L;
        this.proposalIdOnAcceptor = -1L;
        this.acceptedMap = new TreeMap<>();
    }

    @Override
    public void open(String path) {

    }

    @Override
    public void close() {

    }

    @Override
    public Long incrSerialNumber() {
        serialNumber += 1;
        return serialNumber;
    }

    @Override
    public void saveProposalIdOnProposer(Long proposalId) {
        this.proposalIdOnProposer = proposalId;
    }

    @Override
    public Long getProposalIdOnProposer() {
        return this.proposalIdOnProposer;
    }

    @Override
    public Long getProposalIdOnAcceptor() {
        return this.proposalIdOnAcceptor;
    }

    @Override
    public void saveProposalIdOnAcceptor(Long proposalId) {
        this.proposalIdOnAcceptor = proposalId;
    }

    @Override
    public PaxosAccepted getMaxProposalIdAccepted() {
        if (this.acceptedMap.isEmpty())
            return null;

        var entry = this.acceptedMap.lastEntry();
        return entry.getValue();
    }

    @Override
    public void saveAccepted(PaxosAccepted accepted) {
        this.acceptedMap.put(accepted.proposal, accepted);
    }
}
