package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosAccept;
import com.github.ralgond.paxos.core.common.PaxosPrepare;
import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

import java.util.Optional;

public class Acceptor {

    public PaxosEnvironment env;

    public Long expected_version;

    public Long promised_proposal_id;

    public PaxosValue promised_value;

    public void start(PaxosEnvironment env) {
        this.env = env;

        Optional<Long> expected_version_optional = env.persistent.getAcceptedMaxVersion();
        this.expected_version = 0L;
        expected_version_optional.ifPresent(aLong -> this.expected_version = aLong + 1);
    }

    public void onRecvPrepareRequest(PaxosPrepareRequest req) {
        if (!req.server_id.equals(env.config.getServerId()))
            return;

        if (!req.value_version.equals(this.expected_version)) {
            var resp = new PaxosPrepareResponse(this.env.config.getServerId(),
                    req.proposal_id, -1L, this.expected_version, new PaxosValue());
            this.env.sender.sendPrepareResponse(resp);
            return;
        }

        var max_promised_id = this.env.persistent.getMaxPromisedId();
        var pr = new PaxosPrepare();
        if (max_promised_id.isPresent()) {
            if (req.proposal_id > max_promised_id.get()) {
                pr = new PaxosPrepare(req.proposal_id, req.proposal_value);
                this.env.persistent.savePrepare(pr);
            } else {
                pr = new PaxosPrepare(max_promised_id.get(),
                        this.env.persistent.getPrepare(max_promised_id.get()).get().value);
            }
        } else {
            pr = new PaxosPrepare(req.proposal_id, req.proposal_value);
        }

        var resp = new PaxosPrepareResponse(this.env.config.getServerId(),
                req.proposal_id, pr.promised_proposal_id, this.expected_version, pr.value);
        this.env.sender.sendPrepareResponse(resp);
    }

    public void onRecvAcceptRequest(PaxosAcceptRequest req) {
        if (!req.server_id.equals(env.config.getServerId()))
            return;

        var max_promised_id = this.env.persistent.getMaxPromisedId();
        if (max_promised_id.isPresent()) {
            if (req.proposal_id.equals(max_promised_id.get())) {
                var pa = new PaxosAccept(this.expected_version, this.promised_value);
                this.env.persistent.saveAccept(pa);
            } else {
                var resp = new PaxosAcceptResponse(req.server_id, req.proposal_id, this.promised_proposal_id);
                this.env.sender.sendAcceptResponse(resp);
            }
        } else {
            var resp = new PaxosAcceptResponse(req.server_id, req.proposal_id, -1L);
            this.env.sender.sendAcceptResponse(resp);
        }
    }
}
