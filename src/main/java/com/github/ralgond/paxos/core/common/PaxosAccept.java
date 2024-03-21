package com.github.ralgond.paxos.core.common;

import java.math.BigInteger;

public class PaxosAccept {
    public Long version;

    public PaxosValue value;

    public PaxosAccept(Long version, PaxosValue value) {
        this.version = version;
        this.value = value;
    }
}
