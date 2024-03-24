package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.*;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptRequest;
import com.github.ralgond.paxos.core.protocol.PaxosAcceptResponse;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareRequest;
import com.github.ralgond.paxos.core.protocol.PaxosPrepareResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptorTest {

    @Test
    public void testPrepare01() {
        var env = new PaxosEnvironmentTest(new PaxosConfigTest(),
                new PaxosPacketSenderTest(),
                new PaxosPersistentTest(),
                new PaxosTimerManagerTest());

        var acceptor = new Acceptor();
        acceptor.start(env);

        var sender = (PaxosPacketSenderTest)env.sender;
        var persistent = (PaxosPersistentTest)env.persistent;

        /*
         * 1. accepted == null
         */
        PaxosPrepareRequest req1 = new PaxosPrepareRequest(1, 0L, 1L);
        acceptor.onRecvPrepareRequest(req1);
        assertEquals(1, sender.prepareRespList.size());
        System.out.println(sender.prepareRespList);
        assertEquals(new PaxosPrepareResponse(1, 0L, 1L,
                new PaxosAccepted(1L, -1L, new PaxosValue()), true), sender.prepareRespList.get(0));
        sender.prepareRespList.clear();

        /*
         * 2. accepted != null && accepted.promisedId < req.proposalId
         */
        PaxosPrepareRequest req2 = new PaxosPrepareRequest(1, 0L, 3L);
        acceptor.onRecvPrepareRequest(req2);
        assertEquals(1, sender.prepareRespList.size());
        System.out.println(sender.prepareRespList);
        assertEquals(new PaxosPrepareResponse(1, 0L, 3L,
                new PaxosAccepted(3L, -1L, new PaxosValue()), true), sender.prepareRespList.get(0));
        sender.prepareRespList.clear();
        System.out.println(persistent.acceptedMap);

        /*
         * 3. accepted != null && accepted.promisedId >= req.proposalId
         */
        PaxosPrepareRequest req3 = new PaxosPrepareRequest(1, 0L, 2L);
        acceptor.onRecvPrepareRequest(req3);
        assertEquals(1, sender.prepareRespList.size());
        System.out.println(sender.prepareRespList);
        assertEquals(new PaxosPrepareResponse(1, 0L, 2L,
                new PaxosAccepted(3L, -1L, new PaxosValue()), false), sender.prepareRespList.get(0));
        sender.prepareRespList.clear();

        /*
         * 4. accepted == null
         */
        PaxosAcceptRequest req4 = new PaxosAcceptRequest(1, 1L, 1L, new PaxosValue(1, 1L, "abc"));
        acceptor.onRecvAcceptRequest(req4);
        assertEquals(1, sender.acceptRespList.size());
        System.out.println(sender.acceptRespList);
        assertEquals(new PaxosAcceptResponse(1, 1L, 1L, 1L, true), sender.acceptRespList.get(0));
        sender.acceptRespList.clear();

        /*
         * 5. accepted != null && accepted.promisedId <= req.proposalId
         */
        PaxosAcceptRequest req5 = new PaxosAcceptRequest(1, 0L, 3L, new PaxosValue(1, 1L, "abc"));
        acceptor.onRecvAcceptRequest(req5);
        assertEquals(1, sender.acceptRespList.size());
        System.out.println(sender.acceptRespList);
        assertEquals(new PaxosAcceptResponse(1, 0L, 3L, 3L, true), sender.acceptRespList.get(0));
        sender.acceptRespList.clear();

        /*
         * 6. accepted == null && accepted.promisedId > req.proposalId
         */
        PaxosAcceptRequest req6 = new PaxosAcceptRequest(1, 0L, 2L, new PaxosValue(1, 1L, "abc"));
        acceptor.onRecvAcceptRequest(req6);
        assertEquals(1, sender.acceptRespList.size());
        System.out.println(sender.acceptRespList);
        assertEquals(new PaxosAcceptResponse(1, 0L, 2L, 3L, false), sender.acceptRespList.get(0));
        sender.acceptRespList.clear();
    }

    @Test
    public void testAccept01() {
        var env = new PaxosEnvironmentTest(new PaxosConfigTest(),
                new PaxosPacketSenderTest(),
                new PaxosPersistentTest(),
                new PaxosTimerManagerTest());

        var acceptor = new Acceptor();
        acceptor.start(env);

        var sender = (PaxosPacketSenderTest) env.sender;
        var persistent = (PaxosPersistentTest) env.persistent;

    }
}
