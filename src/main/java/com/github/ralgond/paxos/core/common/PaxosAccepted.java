package com.github.ralgond.paxos.core.common;

import java.util.Objects;

public class PaxosAccepted {
    public Long proposal;

    public PaxosValue value;

    public PaxosAccepted() {
        this.proposal = -1L;
        this.value = new PaxosValue();
    }

    public PaxosAccepted(Long proposal, PaxosValue value) {
        this.proposal = proposal;
        this.value = value;
    }

    public boolean isNone() {
        return this.proposal < 0L;
    }

    @Override
    public String toString() {
        return "PaxosAccepted{" +
                "proposal=" + proposal +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosAccepted that = (PaxosAccepted) o;
        return Objects.equals(proposal, that.proposal) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposal, value);
    }
}
