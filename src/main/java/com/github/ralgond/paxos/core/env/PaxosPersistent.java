package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
import com.github.ralgond.paxos.core.common.PaxosPromised;
import com.github.ralgond.paxos.core.common.PaxosValue;

import java.util.Optional;

public interface PaxosPersistent {
    public void open(String path);

    public void close();

    public Long incrSerialNumber();

    public void saveProposalIdOnProposer(Long proposalId);
    /**
     *
     * @return -1 means not any proposal id saved.
     */
    public Long getProposalIdOnProposer();


    public Long getProposalIdOnAcceptor();

    public void saveProposalIdOnAcceptor(Long proposal_id);

    /**
     *
     * @return new PaxosAccepted() when there is not any accepted value.
     */
    public PaxosAccepted getMaxProposalIdAccepted();

    public void saveAccepted(PaxosAccepted accepted);
}
