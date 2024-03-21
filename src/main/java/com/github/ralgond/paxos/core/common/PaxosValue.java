package com.github.ralgond.paxos.core.common;

import java.util.Objects;

public class PaxosValue {
    public Integer server_id;
    public Long value_sn;
    public String value;

    public PaxosValue(Integer server_id, Long value_sn, String value) {
        this.server_id = server_id;
        this.value_sn = value_sn;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PaxosValue) {
            var pe = (PaxosValue)obj;
            return this.server_id.equals(pe.server_id) && this.value_sn.equals(pe.value_sn);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(server_id, value_sn);
    }
}
