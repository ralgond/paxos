package com.github.ralgond.paxos.core.component;

import java.util.HashMap;

import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.PaxosEnvironment;
import com.github.ralgond.paxos.core.env.PaxosTimerManager;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public class Proposer {
    public static class PaxosStateMachine implements PaxosTimerManager.Timer {
        final PaxosValue paxos_value;

        boolean stopped;

        boolean preparing;

        Long paxos_id;


        Long proposal_id;

        PaxosValue proposal_value;

        HashMap<Integer, PaxosPrepareResponse> prepare_resp_map;

        HashMap<Integer, PaxosAcceptResponse> accept_resp_map;

        public PaxosStateMachine(PaxosValue paxos_value) {
            this.paxos_value = paxos_value;
            this.stopped = false;
            this.preparing = true;
            this.prepare_resp_map = new HashMap<>();
            this.accept_resp_map = new HashMap<>();
        }

        @Override
        public Long id() {
            return this.paxos_value.value_sn;
        }

        public void start(PaxosEnvironment env) {
            this.proposal_id =  env.persistent.getProposalId() + 1;
            this.paxos_id = env.persistent.getMaxPaxosId() + 1;
            this.proposal_value = this.paxos_value;

            for (var server_id : env.config.getServers().keySet()) {
                PaxosPrepareRequest req = new PaxosPrepareRequest(
                        server_id,
                        this.proposal_id,
                        this.paxos_id,
                        this.proposal_value
                );
                env.sender.sendPrepareRequest(req);
            }

            this.preparing = true;
            env.timer_manager.removeTimer(this);
            env.timer_manager.addTimer(this, 3000L);
        }

        public void stop(PaxosEnvironment env) {
            assert (this.proposal_value.equals(this.paxos_value));
            this.stopped = true;
            env.timer_manager.removeTimer(this);
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
            if (!this.isPreparing()) { return; }

            if (!resp.isPromised())
                return;

            this.prepare_resp_map.put(resp.server_id, resp);

            if (this.prepare_resp_map.size() >= env.config.getServers().size() / 2 + 1) {
                Long max_promised_id = -1L;
                PaxosValue max_promised_value = new PaxosValue();

                for (var prepare_resp : this.prepare_resp_map.values()) {
                    if (prepare_resp.promised.proposal_id > max_promised_id) {
                        max_promised_id = prepare_resp.promised.proposal_id;
                        max_promised_value = prepare_resp.promised.value;
                    }
                }

                env.persistent.saveProposalId(max_promised_id);
                this.proposal_value = max_promised_value;

                for (var prepare_resp : this.prepare_resp_map.values()) {
                    var req = new PaxosAcceptRequest(prepare_resp.server_id,
                            this.paxos_id, max_promised_id, max_promised_value);
                    env.sender.sendAcceptRequest(req);
                }
            }
        }
        public void onRecvAcceptResponse(PaxosAcceptResponse resp, PaxosEnvironment env) {
            if (this.isPreparing()) { return; }

            if (!resp.isAccepted())
                return;

            this.accept_resp_map.put(resp.server_id, resp);

            if (this.accept_resp_map.size() >= env.config.getServers().size() / 2 + 1) {
                env.persistent.saveMaxPaxosId(this.paxos_id);
                if (this.proposal_value.equals(this.paxos_value)) {
                    this.stop(env);
                } else {
                    this.start(env);
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
