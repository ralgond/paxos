package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
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
        if (!req.serverId.equals(env.config.getServerId()))
            return;

        /*
         * n > minProposal the minProposal = n
         * Return (acceptedProposal, acceptedValue)
         */

        PaxosAccepted accepted = env.persistent.getAccepted(req.paxosId);
        if (accepted == null || accepted.promisedId < req.proposalId) {
            // promise
            Long promisedIdOnAcceptor = req.proposalId;
            Long acceptedIdOnAcceptor = (accepted == null ? -1 : accepted.acceptedId);
            var pa = new PaxosAccepted(promisedIdOnAcceptor, acceptedIdOnAcceptor, new PaxosValue());
            env.persistent.saveAccepted(req.paxosId, pa);
            var resp = new PaxosPrepareResponse(req.serverId, req.paxosId, req.proposalId, pa, true);
            env.sender.sendPrepareResponse(resp);
        } else {
            // reject
            Long promisedIdOnAcceptor = accepted.promisedId;
            Long acceptedIdOnAcceptor = accepted.acceptedId;
            var resp = new PaxosPrepareResponse(req.serverId, req.paxosId, req.proposalId,
                    new PaxosAccepted(promisedIdOnAcceptor, acceptedIdOnAcceptor, new PaxosValue()), false);
            env.sender.sendPrepareResponse(resp);
        }
    }

    public void onRecvAcceptRequest(PaxosAcceptRequest req) {
        if (!req.serverId.equals(env.config.getServerId()))
            return;

        /*
         * if n >= minProposal then acceptedProposal = minProposal = n, acceptedValue = value
         * return (minProposal)
         */

        PaxosAccepted accepted = env.persistent.getAccepted(req.paxosId);
        if (accepted == null || accepted.promisedId <= req.proposalId) {
            var promisedId = req.proposalId;
            var acceptedProposalId = req.proposalId;
            var acceptedValue = req.proposalValue;
            env.persistent.saveAccepted(req.paxosId, new PaxosAccepted(promisedId, acceptedProposalId, acceptedValue));
            var resp = new PaxosAcceptResponse(req.serverId, req.paxosId, req.proposalId, promisedId, true);
            env.sender.sendAcceptResponse(resp);
        } else {
            // reject
            Long promisedIdOnAcceptor = accepted.promisedId;
            var resp = new PaxosAcceptResponse(req.serverId, req.paxosId, req.proposalId, promisedIdOnAcceptor, false);
            env.sender.sendAcceptResponse(resp);
        }
    }
}
