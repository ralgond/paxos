package com.github.ralgond.paxos.core.component;

import java.util.HashMap;

import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public class Proposer {
    public interface StateMachine {
        public StateMachine start(PaxosEnvironment env, Long paxos_id, PaxosValue paxos_value);
        public boolean isStopped();
        public StateMachine onTimeout(PaxosEnvironment env);
        public StateMachine onRecvPrepareResponse(PaxosPrepareResponse resp, PaxosEnvironment env);
        public StateMachine onRecvAcceptResponse(PaxosAcceptResponse resp, PaxosEnvironment env);
    }

    public class StateMachinePrepare implements StateMachine {

        public final Long paxos_id;

        public final PaxosValue paxos_value;

        public Long proposal_id;

        public PaxosValue proposal_value;

        public HashMap<Integer, PaxosPrepareResponse> resp_map;

        public StateMachinePrepare(Long paxos_id, PaxosValue paxos_value) {
            this.paxos_id = paxos_id;
            this.paxos_value = paxos_value;
            this.proposal_value = paxos_value;
        }

        @Override
        public StateMachine start(PaxosEnvironment env, Long paxos_id, PaxosValue paxos_value) {
            this.proposal_id =  env.persistent.getProposalId() + 1;

            for (var server_id : env.config.getServers().keySet()) {
                PaxosPrepareRequest req = new PaxosPrepareRequest(
                        server_id,
                        this.proposal_id,
                        paxos_id,
                        paxos_value
                );
                env.sender.sendPrepareRequest(req);
            }
            return new StateMachinePrepare(paxos_id, paxos_value);
        }

        @Override
        public boolean isStopped() {
            return false;
        }

        @Override
        public StateMachine onTimeout(PaxosEnvironment env) {
            return start(env, this.paxos_id, this.paxos_value);
        }

        @Override
        public StateMachine onRecvPrepareResponse(PaxosPrepareResponse resp, PaxosEnvironment env) {
            if (resp.isPromised()) {
                resp_map.put(resp.server_id, resp);
                if (resp_map.size() >= env.config.getServers().size() / 2 + 1) {
                    Long max_promised_proposal_id = -1L;
                    var max_promised_proposal_value = new PaxosValue();

                    for (var prepare_resp : resp_map.values()) {
                        if (prepare_resp.promised.proposal_id > max_promised_proposal_id) {
                            max_promised_proposal_id = prepare_resp.promised.proposal_id;
                            max_promised_proposal_value = prepare_resp.promised.value;
                        }
                    }

                    for (var server : resp_map.values()) {
                        var req = new PaxosAcceptRequest(server.server_id, this.paxos_id, max_promised_proposal_id, max_promised_proposal_value);
                        env.sender.sendAcceptRequest(req);
                    }
                    return new StateMachineAccept(this.paxos_id, this.paxos_value, max_promised_proposal_value);
                } else {
                    return this;
                }
            } else {
                if (resp.promised.proposal_id > env.persistent.getProposalId()) {
                    env.persistent.saveProposalId(resp.promised.proposal_id);
                    return start(env, this.paxos_id, this.paxos_value);
                } else {
                    return this;
                }
            }
        }

        @Override
        public StateMachine onRecvAcceptResponse(PaxosAcceptResponse resp, PaxosEnvironment env) {
            return this;
        }
    }

    public class StateMachineAccept implements StateMachine {

        public boolean stopped;
        public final Long paxos_id;

        public final PaxosValue paxos_value;

        public PaxosValue proposal_value;

        public HashMap<Integer, PaxosAcceptResponse> resp_map;

        public StateMachineAccept(Long paxos_id, PaxosValue paxos_value, PaxosValue proposal_value) {
            this.stopped = false;
            this.paxos_id = paxos_id;
            this.paxos_value = paxos_value;
            this.proposal_value = proposal_value;
            this.resp_map = new HashMap<>();
        }
        @Override
        public StateMachine start(PaxosEnvironment env, Long paxos_id, PaxosValue paxos_value) {
            for (var server_id : env.config.getServers().keySet()) {
                PaxosPrepareRequest req = new PaxosPrepareRequest(
                        server_id,
                        env.persistent.getProposalId() + 1,
                        paxos_id,
                        paxos_value
                );
                env.sender.sendPrepareRequest(req);
            }
            return new StateMachinePrepare(paxos_id, paxos_value);
        }

        @Override
        public boolean isStopped() {
            return stopped;
        }

        @Override
        public StateMachine onTimeout(PaxosEnvironment env) {
            return start(env, this.paxos_id, this.paxos_value);
        }

        @Override
        public StateMachine onRecvPrepareResponse(PaxosPrepareResponse resp, PaxosEnvironment env) {
            return this;
        }

        @Override
        public StateMachine onRecvAcceptResponse(PaxosAcceptResponse resp, PaxosEnvironment env) {
            if (resp.isAccepted()) {
                resp_map.put(resp.server_id, resp);
                if (resp_map.size() >= env.config.getServers().size()/2 + 1) {
                    env.persistent.saveResult(this.paxos_id, this.proposal_value);
                    if (this.proposal_value.equals(this.paxos_value)) {
                        this.stopped = true;
                        return this;
                    } else {
                        return this.start(env, env.persistent.getMaxPaxosId(), this.paxos_value);
                    }
                } else {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public PaxosEnvironment env;
    public StateMachine state_machine;

    public void start(PaxosEnvironment env) {
        this.env = env;
    }
}
