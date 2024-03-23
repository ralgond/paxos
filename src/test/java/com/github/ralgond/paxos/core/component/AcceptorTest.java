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

        //
        PaxosPrepareRequest req = new PaxosPrepareRequest(1, 2L);
        acceptor.onRecvPrepareRequest(req);
        assertEquals(1, sender.prepare_resp_list.size());
        PaxosPrepareResponse resp = sender.prepare_resp_list.get(0);

        PaxosPrepareResponse resp_test = new PaxosPrepareResponse(1, 2L, -1L, new PaxosAccepted());
        assertEquals(resp, resp_test);

        assertEquals(Long.valueOf(2L), persistent.proposalIdOnAcceptor);

        //
        PaxosPrepareRequest req2 = new PaxosPrepareRequest(1, 2L);
        acceptor.onRecvPrepareRequest(req2);
        assertEquals(2, sender.prepare_resp_list.size());
        PaxosPrepareResponse resp2 = sender.prepare_resp_list.get(1);

        PaxosPrepareResponse resp_test2 = new PaxosPrepareResponse(1, 2L, 2L, new PaxosAccepted());
        assertEquals(resp2, resp_test2);

        assertEquals(Long.valueOf(2L), persistent.proposalIdOnAcceptor);

        //
        PaxosPrepareRequest req3 = new PaxosPrepareRequest(1, 4L);
        acceptor.onRecvPrepareRequest(req3);
        assertEquals(3, sender.prepare_resp_list.size());
        PaxosPrepareResponse resp3 = sender.prepare_resp_list.get(2);

        PaxosPrepareResponse resp_test3 = new PaxosPrepareResponse(1, 4L, 2L, new PaxosAccepted());
        assertEquals(resp3, resp_test3);

        assertEquals(Long.valueOf(4L), persistent.proposalIdOnAcceptor);

        //
        PaxosPrepareRequest req4 = new PaxosPrepareRequest(1, 2L);
        acceptor.onRecvPrepareRequest(req4);
        assertEquals(4, sender.prepare_resp_list.size());
        PaxosPrepareResponse resp4 = sender.prepare_resp_list.get(3);

        PaxosPrepareResponse resp_test4 = new PaxosPrepareResponse(1, 2L, 4L, new PaxosAccepted());
        assertEquals(resp4, resp_test4);
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

        //
        PaxosPrepareRequest prepare_req = new PaxosPrepareRequest(1, 2L);
        acceptor.onRecvPrepareRequest(prepare_req);
        assertEquals(1, sender.prepare_resp_list.size());
        PaxosPrepareResponse prepare_resp = sender.prepare_resp_list.get(0);

        //
        PaxosAcceptRequest req1 = new PaxosAcceptRequest(1, 1L, new PaxosValue(1, 1L, "abc"));
        acceptor.onRecvAcceptRequest(req1);
        assertEquals(1, sender.accept_resp_list.size());
        PaxosAcceptResponse resp1 = sender.accept_resp_list.get(0);
        assertFalse(resp1.isAccepted());
        assertTrue(persistent.acceptedMap.isEmpty());

        //
        PaxosAcceptRequest req2 = new PaxosAcceptRequest(1, 2L, new PaxosValue(1, 1L, "abc"));
        acceptor.onRecvAcceptRequest(req2);
        assertEquals(2, sender.accept_resp_list.size());
        PaxosAcceptResponse resp2 = sender.accept_resp_list.get(1);
        assertTrue(resp2.isAccepted());
        assertFalse(persistent.acceptedMap.isEmpty());
        PaxosAccepted pa = persistent.acceptedMap.get(2L);
        assertEquals(Long.valueOf(2L), pa.proposal);
        assertEquals(pa.value, new PaxosValue(1, 1L, "abc"));
    }
}
