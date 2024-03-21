package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccept;

public interface PaxosPersistent {
    public void open(String path);

    public void close();

    public Long getLatestValueProposalIdAndIncrease();

    public void saveAccept(PaxosAccept accept);

    public PaxosAccept getLastestAccept();
}
