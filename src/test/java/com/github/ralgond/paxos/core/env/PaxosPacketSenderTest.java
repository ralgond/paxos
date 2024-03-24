package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

import java.util.ArrayList;

public class PaxosPacketSenderTest implements PaxosPacketSender {

    public ArrayList<PaxosPrepareRequest> prepareReqList;
    public ArrayList<PaxosPrepareResponse> prepareRespList;
    public ArrayList<PaxosAcceptRequest> acceptReqList;
    public ArrayList<PaxosAcceptResponse> acceptRespList;

    public PaxosPacketSenderTest() {
        prepareReqList = new ArrayList<>();
        prepareRespList = new ArrayList<>();
        acceptReqList = new ArrayList<>();
        acceptRespList = new ArrayList<>();
    }

    @Override
    public void sendPrepareRequest(PaxosPrepareRequest req) {
        prepareReqList.add(req);
    }

    @Override
    public void sendPrepareResponse(PaxosPrepareResponse resp) {
        prepareRespList.add(resp);
    }

    @Override
    public void sendAcceptRequest(PaxosAcceptRequest req) {
        acceptReqList.add(req);
    }

    @Override
    public void sendAcceptResponse(PaxosAcceptResponse resp) {
        acceptRespList.add(resp);
    }
}
