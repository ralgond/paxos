package com.github.ralgond.paxos.core.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaxosValueTest {

    @org.junit.Test
    public void testEquals() {
        var pv1 = new PaxosValue(1, Long.valueOf(2), "test2");

        assertEquals(pv1, new PaxosValue(1, Long.valueOf(2), "test2"));

        assertNotEquals(pv1, new PaxosValue(1, Long.valueOf(3), "test2"));

        assertNotEquals(pv1, "abc");
    }

    @Test
    public void testHashCode() {
        var pv1 = new PaxosValue(1, Long.valueOf(2), "test");

        var pv2 = new PaxosValue(1, Long.valueOf(2), "test2");

        assertNotEquals(pv1, pv2);
    }
}