package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.common.PaxosAccept;
import com.github.ralgond.paxos.core.common.PaxosPrepare;

import java.util.Optional;

public interface PaxosPersistent {
    public void open(String path);

    public void close();

    public Long getLatestValueSerialNumberAndIncrease();

    public Optional<Long> getMaxPromisedId();

    public void savePrepare(PaxosPrepare prepare);

    public Optional<PaxosPrepare> getPrepare(Long promised_id);


    public void saveAccept(PaxosAccept accept);

    public Optional<PaxosAccept> getAccept(Long version);

    public Optional<Long> getAcceptedMaxVersion();
}
