package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
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

        Long proposal_id_on_acceptor = env.persistent.getProposalIdOnAcceptor();
        if (req.proposal_id > proposal_id_on_acceptor) {
            env.persistent.saveProposalIdOnAcceptor(req.proposal_id);
            var pa = env.persistent.getMaxProposalIdAccepted();
            var resp = new PaxosPrepareResponse(req.server_id, req.proposal_id, proposal_id_on_acceptor, pa);
            env.sender.sendPrepareResponse(resp);
        } else {
            // Reject the request.
            var resp = new PaxosPrepareResponse(req.server_id, req.proposal_id, proposal_id_on_acceptor, new PaxosAccepted());
            env.sender.sendPrepareResponse(resp);
        }
    }

    public void onRecvAcceptRequest(PaxosAcceptRequest req) {
        if (!req.server_id.equals(env.config.getServerId()))
            return;

        Long proposal_id_on_acceptor = env.persistent.getProposalIdOnAcceptor();
        if (req.proposal_id >= proposal_id_on_acceptor) {
            proposal_id_on_acceptor = req.proposal_id;
            env.persistent.saveProposalIdOnAcceptor(req.proposal_id);
            var pa = new PaxosAccepted(req.proposal_id, req.proposal_value);
            env.persistent.saveAccepted(pa);
            var resp = new PaxosAcceptResponse(req.server_id, req.proposal_id, proposal_id_on_acceptor);
            env.sender.sendAcceptResponse(resp);
        } else {
            // Reject the request
            var resp = new PaxosAcceptResponse(req.server_id, req.proposal_id, proposal_id_on_acceptor);
            env.sender.sendAcceptResponse(resp);
        }
    }
}
