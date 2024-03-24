package com.github.ralgond.paxos.core.component;

import java.util.TreeMap;

import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.env.PaxosTimerManager;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public class Proposer {
    public static class PaxosStateMachine implements PaxosTimerManager.Timer {
        final PaxosValue paxosValue;

        boolean stopped;

        boolean preparing;

        Long paxosId;

        Long proposalId;

        PaxosValue proposalValue;

        TreeMap<Integer, PaxosPrepareResponse> prepareRespMap;

        TreeMap<Integer, PaxosPrepareResponse> notpromisedPrepareRespMap;

        TreeMap<Integer, PaxosAcceptResponse> acceptRespMap;

        TreeMap<Integer, PaxosAcceptResponse> notacceptedAcceptRespMap;

        public PaxosStateMachine(PaxosValue paxosValue) {
            this.paxosValue = paxosValue;
            this.stopped = false;
            this.preparing = true;
            this.prepareRespMap = new TreeMap<>();
            this.acceptRespMap = new TreeMap<>();

            this.notpromisedPrepareRespMap = new TreeMap<>();
            this.notacceptedAcceptRespMap = new TreeMap<>();
        }

        @Override
        public Long id() {
            return this.paxosValue.value_sn;
        }

        public void start(PaxosEnvironment env) {
            /*
             * Choose a new proposal number n
             */
            this.paxosId = env.persistent.getPaxosId();
            this.proposalId =  env.persistent.getProposalIdOnProposer(paxosId) + 1;
            this.proposalValue = this.paxosValue;

            this.prepareRespMap.clear();
            this.acceptRespMap.clear();
            this.notpromisedPrepareRespMap.clear();
            this.notacceptedAcceptRespMap.clear();

            /*
             * Broadcast Prepare(n) to all servers
             */
            for (var serverId : env.config.getServers().keySet()) {
                PaxosPrepareRequest req = new PaxosPrepareRequest(
                        serverId,
                        paxosId,
                        proposalId
                );
                env.sender.sendPrepareRequest(req);
            }

            this.preparing = true;
            env.timerManager.removeTimer(this);
            env.timerManager.addTimer(this, 3000L);
        }

        public void stop(PaxosEnvironment env) {
            assert (this.proposalValue.equals(this.paxosValue));
            this.stopped = true;
            env.timerManager.removeTimer(this);

            this.paxosId = -1L;
            this.proposalId = -1L;

            this.proposalValue = new PaxosValue();

            this.prepareRespMap.clear();
            this.acceptRespMap.clear();

            this.notpromisedPrepareRespMap.clear();
            this.notacceptedAcceptRespMap.clear();
        }

        /**
         * For test
         * @return
         */
        public boolean nothingChangeAfterStopped() {
            return proposalId == -1L && proposalValue.isNone() &&
                    prepareRespMap.isEmpty() &&
                    acceptRespMap.isEmpty() &&
                    notpromisedPrepareRespMap.isEmpty() &&
                    notacceptedAcceptRespMap.isEmpty();
        }

        boolean isStopped() {
            return stopped;
        }

        boolean isPreparing() {
            return preparing;
        }

        public void onTimeout(PaxosEnvironment env) {
            this.start(env);
        }

        public void onRecvPrepareResponse(PaxosPrepareResponse resp, PaxosEnvironment env) {
            if (this.isStopped())
                return;

            if (!this.isPreparing())
                return;

            if (!env.config.getServers().containsKey(resp.serverId))
                return;

            if (!resp.paxosId.equals(this.paxosId))
                return;

            if (!resp.proposalId.equals(this.proposalId))
                return;

            if (!resp.promised) {
                notpromisedPrepareRespMap.put(resp.serverId, resp);
                if (notpromisedPrepareRespMap.size() >= env.config.getMajoritySize()) {
                    var maxProposalId = proposalId;
                    for (var r : notpromisedPrepareRespMap.values()) {
                        if (r.accepted.promisedId > maxProposalId) {
                            maxProposalId = r.accepted.promisedId;
                        }
                    }
                    env.persistent.saveProposalIdOnProposer(paxosId, maxProposalId);
                    // TODO: congestion control
                    start(env);
                }

                return;
            }
            /*
             * When responses received from majority:
             * If any acceptedValue returned, replace value with acceptedValue for
             * highest acceptedProposal
             */
            prepareRespMap.put(resp.serverId, resp);
            if (prepareRespMap.size() >= env.config.getMajoritySize()) {
                var maxAcceptedProposal = -1L;
                var acceptedValue = new PaxosValue();

                for (var r : prepareRespMap.values()) {
                    if (r.accepted.acceptedId > maxAcceptedProposal) {
                        maxAcceptedProposal = r.accepted.acceptedId;
                        acceptedValue = r.accepted.acceptedValue;
                    }
                }

                if (acceptedValue.isNone()) {
                    this.proposalValue = paxosValue;
                } else {
                    this.proposalValue = acceptedValue;
                }

                /*
                 * Broadcast Accept(n, value) to all servers
                 */
                for (var serverId : env.config.getServers().keySet()) {
                    var req = new PaxosAcceptRequest(serverId, paxosId, proposalId, proposalValue);
                    env.sender.sendAcceptRequest(req);
                }

                this.preparing = false;
            }
        }
        public void onRecvAcceptResponse(PaxosAcceptResponse resp, PaxosEnvironment env) {
            if (this.isStopped())
                return;

            if (this.isPreparing())
                return;

            if (!env.config.getServers().containsKey(resp.serverId))
                return;

            if (!resp.paxosId.equals(this.paxosId))
                return;

            if (!resp.proposalId.equals(this.proposalId))
                return;

            if (!resp.accepted) {
                notacceptedAcceptRespMap.put(resp.serverId, resp);
                if (notacceptedAcceptRespMap.size() >= env.config.getMajoritySize()) {
                    var maxProposalId = proposalId;
                    for (var r : notacceptedAcceptRespMap.values()) {
                        if (r.promisedProposalId > maxProposalId) {
                            maxProposalId = r.promisedProposalId;
                        }
                    }
                    env.persistent.saveProposalIdOnProposer(paxosId, maxProposalId);
                    // TODO: congestion control
                    start(env);
                }
                return;
            }
            /*
             * When response received from majority:
             * Any rejections (result > n)? goto (1)
             * Otherwise, value is chosen.
             */
            acceptRespMap.put(resp.serverId, resp);
            if (acceptRespMap.size() >= env.config.getMajoritySize()) {
                env.persistent.saveProposalIdOnProposer(paxosId, proposalId);
                env.persistent.savePaxosId(paxosId + 1);
                if (proposalValue.equals(paxosValue)) {
                    stop(env);
                } else {
                    start(env);
                }
            }
        }
    }


    public PaxosEnvironment env;
    public PaxosStateMachine state_machine;

    public void start(PaxosEnvironment env, String value) {
        this.env = env;
        var paxos_value = new PaxosValue(env.config.getServerId(),
                env.persistent.incrSerialNumber(),
                value);

        this.state_machine = new PaxosStateMachine(paxos_value);
        this.state_machine.start(env);
    }
}
