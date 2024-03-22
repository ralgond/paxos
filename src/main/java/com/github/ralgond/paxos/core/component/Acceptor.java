package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosPromised;
import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public class Acceptor {

    public PaxosEnvironment env;

    public void start(PaxosEnvironment env) {
        this.env = env;
    }

    public void onRecvPrepareRequest(PaxosPrepareRequest req) {
        if (!req.server_id.equals(env.config.getServerId()))
            return;

        var resp = new PaxosPrepareResponse(req.server_id, req.proposal_id, new PaxosPromised());

        var promised = this.env.persistent.getPromised(req.paxos_id);
        if (promised.isPresent()) {
            var promised1 = promised.get();
            if (promised1.proposal_id < req.proposal_id) {
                promised1.proposal_id = req.proposal_id;
                promised1.value = req.proposal_value;
                this.env.persistent.savePromised(promised1);
                resp.promised = promised1;
            } else {
                resp.promised = promised1;
            }
        } else {
            var promised2 = new PaxosPromised(req.paxos_id, req.proposal_id, req.proposal_value);
            this.env.persistent.savePromised(promised2);
            resp.promised = promised2;
        }

        this.env.sender.sendPrepareResponse(resp);
    }

    public void onRecvAcceptRequest(PaxosAcceptRequest req) {
        if (!req.server_id.equals(env.config.getServerId()))
            return;

        var resp = new PaxosAcceptResponse(req.server_id, req.proposal_id, -1L);

        var promised0 = this.env.persistent.getPromised(req.paxos_id);

        if (promised0.isPresent()) {
            var promised = promised0.get();

            resp.promised_proposal_id = promised.proposal_id;

            if (req.proposal_id > promised.proposal_id) {
                promised.proposal_id = req.proposal_id;
                this.env.persistent.savePromised(promised);

                resp.promised_proposal_id = req.proposal_id;
            }
        }

        this.env.sender.sendAcceptResponse(resp);
    }
}
