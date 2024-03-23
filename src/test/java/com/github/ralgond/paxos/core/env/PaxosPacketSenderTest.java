package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

import java.util.ArrayList;

public class PaxosPacketSenderTest implements PaxosPacketSender {

    public ArrayList<PaxosPrepareRequest> prepare_req_list;
    public ArrayList<PaxosPrepareResponse> prepare_resp_list;
    public ArrayList<PaxosAcceptRequest> accept_req_list;
    public ArrayList<PaxosAcceptResponse> accept_resp_list;

    public PaxosPacketSenderTest() {
        prepare_req_list = new ArrayList<>();
        prepare_resp_list = new ArrayList<>();
        accept_req_list = new ArrayList<>();
        accept_resp_list = new ArrayList<>();
    }

    @Override
    public void sendPrepareRequest(PaxosPrepareRequest req) {
        prepare_req_list.add(req);
    }

    @Override
    public void sendPrepareResponse(PaxosPrepareResponse resp) {
        prepare_resp_list.add(resp);
    }

    @Override
    public void sendAcceptRequest(PaxosAcceptRequest req) {
        accept_req_list.add(req);
    }

    @Override
    public void sendAcceptResponse(PaxosAcceptResponse resp) {
        accept_resp_list.add(resp);
    }
}
