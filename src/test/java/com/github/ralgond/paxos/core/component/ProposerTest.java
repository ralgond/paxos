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

public class ProposerTest {

    @Test
    public void test01() {
        var env = new PaxosEnvironmentTest(new PaxosConfigTest(),
                new PaxosPacketSenderTest(),
                new PaxosPersistentTest(),
                new PaxosTimerManagerTest());

        var acceptor = new Acceptor();
        acceptor.start(env);

        var sender = (PaxosPacketSenderTest)env.sender;
        var persistent = (PaxosPersistentTest)env.persistent;

        /*
         * start
         */
        Proposer proposer = new Proposer();
        proposer.start(env, "abc");
        System.out.println(sender.prepareReqList);
        assertEquals(3, sender.prepareReqList.size());
        assertEquals(new PaxosPrepareRequest(1, 0L, 0L), sender.prepareReqList.get(0));
        assertEquals(new PaxosPrepareRequest(2, 0L, 0L), sender.prepareReqList.get(1));
        assertEquals(new PaxosPrepareRequest(3, 0L, 0L), sender.prepareReqList.get(2));
        assertTrue(proposer.state_machine.isPreparing());
        sender.prepareReqList.clear();

        /*
         *  received two promised response
         */
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(2, 0L, 0L, new PaxosAccepted(0L, -1L, new PaxosValue()),true),
                env);
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(3, 0L, 0L, new PaxosAccepted(0L, -1L, new PaxosValue()),true),
                env);
        System.out.println(sender.acceptReqList);
        assertEquals(new PaxosAcceptRequest(1, 0L, 0L, new PaxosValue(1, 1L, "abc")), sender.acceptReqList.get(0));
        assertEquals(new PaxosAcceptRequest(2, 0L, 0L, new PaxosValue(1, 1L, "abc")), sender.acceptReqList.get(1));
        assertEquals(new PaxosAcceptRequest(3, 0L, 0L, new PaxosValue(1, 1L, "abc")), sender.acceptReqList.get(2));
        assertFalse(proposer.state_machine.isPreparing());
        sender.acceptReqList.clear();

        /*
         * received two accepted response
         */
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(2, 0L, 0L, 0L, true), env
        );
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(3, 0L, 0L, 0L, true), env
        );
        assertTrue(proposer.state_machine.isStopped());
    }

    @Test
    public void test02() {
        var env = new PaxosEnvironmentTest(new PaxosConfigTest(),
                new PaxosPacketSenderTest(),
                new PaxosPersistentTest(),
                new PaxosTimerManagerTest());

        var acceptor = new Acceptor();
        acceptor.start(env);

        var sender = (PaxosPacketSenderTest) env.sender;
        var persistent = (PaxosPersistentTest) env.persistent;

        /*
         * start
         */
        Proposer proposer = new Proposer();
        proposer.start(env, "abc");
        System.out.println(sender.prepareReqList);
        assertEquals(3, sender.prepareReqList.size());
        assertEquals(new PaxosPrepareRequest(1, 0L, 0L), sender.prepareReqList.get(0));
        assertEquals(new PaxosPrepareRequest(2, 0L, 0L), sender.prepareReqList.get(1));
        assertEquals(new PaxosPrepareRequest(3, 0L, 0L), sender.prepareReqList.get(2));
        assertTrue(proposer.state_machine.isPreparing());
        sender.prepareReqList.clear();

        /*
         *  received two non-promised response
         */
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(2, 0L, 0L, new PaxosAccepted(2L, 1L, new PaxosValue(2, 1L, "abc")), false),
                env);
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(3, 0L, 0L, new PaxosAccepted(2L, 1L, new PaxosValue(2, 1L, "abc")), false),
                env);
        System.out.println(sender.prepareReqList);
        assertTrue(proposer.state_machine.isPreparing());
        sender.prepareReqList.clear();

        /*
         *  received two promised responses
         */
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(2, 0L, 3L, new PaxosAccepted(3L, 3L, new PaxosValue(2, 1L, "abc")),true),
                env);
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(3, 0L, 3L, new PaxosAccepted(3L, 3L, new PaxosValue(2, 1L, "abc")),true),
                env);
        System.out.println(sender.acceptReqList);
        assertEquals(new PaxosAcceptRequest(1, 0L, 3L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(0));
        assertEquals(new PaxosAcceptRequest(2, 0L, 3L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(1));
        assertEquals(new PaxosAcceptRequest(3, 0L, 3L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(2));
        assertFalse(proposer.state_machine.isPreparing());
        sender.acceptReqList.clear();

        /*
         * received two non-accepted responses
         */
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(2, 0L, 3L, 4L, false), env
        );
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(3, 0L, 3L, 4L, false), env
        );
        System.out.println(sender.prepareReqList);
        assertEquals(new PaxosPrepareRequest(1, 0L, 5L), sender.prepareReqList.get(0));
        assertEquals(new PaxosPrepareRequest(2, 0L, 5L), sender.prepareReqList.get(1));
        assertEquals(new PaxosPrepareRequest(3, 0L, 5L), sender.prepareReqList.get(2));
        assertTrue(proposer.state_machine.isPreparing());
        sender.prepareReqList.clear();

        /*
         *  received two promised responses
         */
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(2, 0L, 5L, new PaxosAccepted(5L, 3L, new PaxosValue(2, 1L, "abc")),true),
                env);
        proposer.state_machine.onRecvPrepareResponse(
                new PaxosPrepareResponse(3, 0L, 5L, new PaxosAccepted(5L, 3L, new PaxosValue(2, 1L, "abc")),true),
                env);
        System.out.println(sender.acceptReqList);
        assertEquals(new PaxosAcceptRequest(1, 0L, 5L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(0));
        assertEquals(new PaxosAcceptRequest(2, 0L, 5L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(1));
        assertEquals(new PaxosAcceptRequest(3, 0L, 5L, new PaxosValue(2, 1L, "abc")), sender.acceptReqList.get(2));
        assertFalse(proposer.state_machine.isPreparing());
        sender.acceptReqList.clear();

        /*
         *  received two accepted responses
         */
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(2, 0L, 5L, 5L, true), env
        );
        proposer.state_machine.onRecvAcceptResponse(
                new PaxosAcceptResponse(3, 0L, 5L, 5L, true), env
        );
        System.out.println(sender.prepareReqList);
        assertEquals(new PaxosPrepareRequest(1, 1L, 0L), sender.prepareReqList.get(0));
        assertEquals(new PaxosPrepareRequest(2, 1L, 0L), sender.prepareReqList.get(1));
        assertEquals(new PaxosPrepareRequest(3, 1L, 0L), sender.prepareReqList.get(2));
        assertTrue(proposer.state_machine.isPreparing());
        sender.prepareReqList.clear();
    }
}
