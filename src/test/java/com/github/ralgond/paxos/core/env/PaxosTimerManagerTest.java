package com.github.ralgond.paxos.core.env;

import java.util.Optional;

public class PaxosTimerManagerTest implements PaxosTimerManager {
    @Override
    public void addTimer(Timer timer, Long milliseconds) {

    }

    @Override
    public void removeTimer(Timer timer) {

    }

    @Override
    public Optional<Timer> popExpiredTimer() {
        return Optional.empty();
    }
}
