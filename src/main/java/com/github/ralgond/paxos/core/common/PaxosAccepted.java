package com.github.ralgond.paxos.core.common;

import java.util.Objects;

public class PaxosAccepted {
    public Long promisedId;

    public Long acceptedId;

    public PaxosValue acceptedValue;

    public PaxosAccepted() {
        this.promisedId = -1L;
        this.acceptedId = -1L;
        this.acceptedValue = new PaxosValue();
    }

    public PaxosAccepted(Long promisedId, Long acceptedId, PaxosValue acceptedValue) {
        this.promisedId = promisedId;
        this.acceptedId = acceptedId;
        this.acceptedValue = acceptedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosAccepted accepted = (PaxosAccepted) o;
        return Objects.equals(promisedId, accepted.promisedId) && Objects.equals(acceptedId, accepted.acceptedId) && Objects.equals(acceptedValue, accepted.acceptedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promisedId, acceptedId, acceptedValue);
    }

    @Override
    public String toString() {
        return "PaxosAccepted{" +
                "promisedId=" + promisedId +
                ", acceptedId=" + acceptedId +
                ", acceptedValue=" + acceptedValue +
                '}';
    }

    public boolean isNone() {
        return this.acceptedId < 0L || acceptedValue.isNone();
    }

}
