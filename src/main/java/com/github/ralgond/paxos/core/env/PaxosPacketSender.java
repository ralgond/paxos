package com.github.ralgond.paxos.core.env;

import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;

public interface PaxosPacketSender {
    public void sendPrepareRequest(PaxosPrepareRequest req);

    public void sendPrepareResponse(PaxosPrepareResponse resp);

    public void sendAcceptRequest(PaxosAcceptRequest req);

    public void sendAcceptResponse(PaxosAcceptResponse resp);
}
