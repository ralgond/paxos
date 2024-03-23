package com.github.ralgond.paxos.core.component;

import com.github.ralgond.paxos.core.common.PaxosAccepted;
import com.github.ralgond.paxos.core.common.PaxosValue;
import com.github.ralgond.paxos.core.env.*;
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

        var sender = (PaxosPacketSenderTest)env.sender;
        var persistent = (PaxosPersistentTest)env.persistent;

        var proposer = new Proposer();

        //
        proposer.start(env, "abc");
        System.out.println(proposer.state_machine.paxos_value);
        assertEquals(Long.valueOf(1L), proposer.state_machine.paxos_value.value_sn);
        assertEquals(Long.valueOf(1L), proposer.state_machine.id());
        assertFalse(proposer.state_machine.isStopped());
        assertTrue(proposer.state_machine.isPreparing());
        assertEquals(3, sender.prepare_req_list.size());
        assertEquals(new PaxosPrepareRequest(1, 0L), sender.prepare_req_list.get(0));
        assertEquals(new PaxosPrepareRequest(2, 0L), sender.prepare_req_list.get(1));
        assertEquals(new PaxosPrepareRequest(3, 0L), sender.prepare_req_list.get(2));


        sender.prepare_req_list.clear();
        // respond two non-promised
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(2, 0L, 2L,
                new PaxosAccepted(2L, new PaxosValue(2, 1L, "2"))), env);
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(3, 0L, 3L,
                new PaxosAccepted(3L, new PaxosValue(3, 1L, "3"))), env);
        assertFalse(proposer.state_machine.isStopped());
        assertTrue(proposer.state_machine.isPreparing());
        assertEquals(3, sender.prepare_req_list.size());
        assertEquals(new PaxosPrepareRequest(1, 4L), sender.prepare_req_list.get(0));
        assertEquals(new PaxosPrepareRequest(2, 4L), sender.prepare_req_list.get(1));
        assertEquals(new PaxosPrepareRequest(3, 4L), sender.prepare_req_list.get(2));


        // respond tow promised
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(2, 4L, 3L,
                new PaxosAccepted(3L, new PaxosValue(2, 1L, "2"))), env);
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(3, 4L, 3L,
                new PaxosAccepted(3L, new PaxosValue(3, 1L, "3"))), env);
        assertFalse(proposer.state_machine.isStopped());
        assertFalse(proposer.state_machine.isPreparing());
        assertEquals(3, sender.accept_req_list.size());


        sender.accept_req_list.clear();
        sender.prepare_req_list.clear();
        proposer.state_machine.onRecvAcceptResponse(new PaxosAcceptResponse(2,4L, 5L), env);
        proposer.state_machine.onRecvAcceptResponse(new PaxosAcceptResponse(3,4L, 5L), env);
        assertFalse(proposer.state_machine.isStopped());
        assertTrue(proposer.state_machine.isPreparing());
        assertEquals(3, sender.prepare_req_list.size());


        sender.prepare_req_list.clear();
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(2, 5L, 4L,
                new PaxosAccepted(4L, new PaxosValue(2, 1L, "2"))), env);
        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(3, 5L, 4L,
                new PaxosAccepted(4L, new PaxosValue(3, 1L, "3"))), env);
        assertFalse(proposer.state_machine.isStopped());
        assertFalse(proposer.state_machine.isPreparing());
        assertEquals(3, sender.accept_req_list.size());
        sender.accept_req_list.clear();

//        System.out.println(persistent.proposalIdOnProposer);
//        System.out.println(proposer.state_machine.proposal_id);

        System.out.println(proposer.state_machine.proposal_id);

        proposer.state_machine.onRecvAcceptResponse(new PaxosAcceptResponse(2,5L, 5L), env);
        proposer.state_machine.onRecvAcceptResponse(new PaxosAcceptResponse(3,5L, 5L), env);

        assertEquals(0, sender.prepare_req_list.size());
        assertFalse(proposer.state_machine.isPreparing());
        assertTrue(proposer.state_machine.isStopped());

        proposer.state_machine.onRecvPrepareResponse(new PaxosPrepareResponse(1, 5L, 4L, new PaxosAccepted()), env);
        assertTrue(proposer.state_machine.nothingChangeAfterStopped());

        proposer.state_machine.onRecvAcceptResponse(new PaxosAcceptResponse(1, 5L, 5L), env);
        assertTrue(proposer.state_machine.nothingChangeAfterStopped());
    }

    @Test
    public void test02Timeout() {
        var env = new PaxosEnvironmentTest(new PaxosConfigTest(),
                new PaxosPacketSenderTest(),
                new PaxosPersistentTest(),
                new PaxosTimerManagerTest());

        var sender = (PaxosPacketSenderTest)env.sender;
        var persistent = (PaxosPersistentTest)env.persistent;

        var proposer = new Proposer();

        //
        proposer.start(env, "abc");
        System.out.println(proposer.state_machine.paxos_value);
        assertEquals(Long.valueOf(1L), proposer.state_machine.paxos_value.value_sn);
        assertEquals(Long.valueOf(1L), proposer.state_machine.id());
        assertFalse(proposer.state_machine.isStopped());
        assertTrue(proposer.state_machine.isPreparing());
        assertEquals(3, sender.prepare_req_list.size());

        sender.prepare_req_list.clear();

        proposer.state_machine.onTimeout(env);

        assertEquals(new PaxosPrepareRequest(1, 0L), sender.prepare_req_list.get(0));
        assertEquals(new PaxosPrepareRequest(2, 0L), sender.prepare_req_list.get(1));
        assertEquals(new PaxosPrepareRequest(3, 0L), sender.prepare_req_list.get(2));
    }
}
