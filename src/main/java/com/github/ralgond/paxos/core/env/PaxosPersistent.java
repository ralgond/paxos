package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccepted;

import java.util.Optional;

public interface PaxosPersistent {
    public void open(String path);

    public void close();

    public Long incrSerialNumber();

    public Long getPaxosId();

    public void savePaxosId(Long paxosId);

    public void saveProposalIdOnProposer(Long paxosId, Long proposalId);
    /**
     *
     * @return -1 means not any proposal id saved.
     */
    public Long getProposalIdOnProposer(Long paxosId);

    /**
     *
     * @return new PaxosAccepted() when there is not any accepted value.
     */
    public PaxosAccepted getAccepted(Long paxosId);

    public void saveAccepted(Long paxosId, PaxosAccepted accepted);
}
