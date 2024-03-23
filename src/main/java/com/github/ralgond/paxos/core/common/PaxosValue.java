package com.github.ralgond.paxos.core.common;

import java.util.Objects;

public class PaxosValue {
    public Integer server_id;
    public Long value_sn;
    public String value;

    public PaxosValue() {
        this.server_id = -1;
        this.value_sn = -1L;
        this.value = "";
    }

    public PaxosValue(Integer server_id, Long value_sn, String value) {
        this.server_id = server_id;
        this.value_sn = value_sn;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaxosValue that = (PaxosValue) o;
        return Objects.equals(server_id, that.server_id) && Objects.equals(value_sn, that.value_sn) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server_id, value_sn, value);
    }

    public boolean isNone() {
        return this.server_id == -1 && this.value_sn == -1L;
    }

    @Override
    public String toString() {
        return "PaxosValue{" +
                "server_id=" + server_id +
                ", value_sn=" + value_sn +
                ", value='" + value + '\'' +
                '}';
    }
}
