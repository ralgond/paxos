package com.github.ralgond.paxos.core.common;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PaxosAcceptedTest {

    @Test
    public void test01() {
        PaxosAccepted pa = new PaxosAccepted();
        assertTrue(pa.isNone());
    }
}
