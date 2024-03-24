package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccepted;

import java.util.TreeMap;

public class PaxosPersistentTest implements PaxosPersistent {

    public Long serialNumber;

    public Long paxosId;

    public TreeMap<Long, Long> proposalIdOnProposerMap;

    public TreeMap<Long, PaxosAccepted> acceptedMap;

    public PaxosPersistentTest() {
        serialNumber = 0L;
        paxosId = 0L;
        proposalIdOnProposerMap = new TreeMap<>();
        acceptedMap = new TreeMap<>();
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
    public Long getPaxosId() {
        return paxosId;
    }

    @Override
    public void savePaxosId(Long paxosId) {
        this.paxosId = paxosId;
    }

    @Override
    public void saveProposalIdOnProposer(Long paxosId, Long proposalId) {
        proposalIdOnProposerMap.put(paxosId, proposalId);
    }

    @Override
    public Long getProposalIdOnProposer(Long paxosId) {
        return proposalIdOnProposerMap.getOrDefault(paxosId, -1L);
    }

    @Override
    public PaxosAccepted getAccepted(Long paxosId) {
        return acceptedMap.get(paxosId);
    }

    @Override
    public void saveAccepted(Long paxosId, PaxosAccepted accepted) {
        acceptedMap.put(paxosId, accepted);
    }
}
