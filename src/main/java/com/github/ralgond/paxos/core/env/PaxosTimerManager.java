package com.github.ralgond.paxos.core.env;

import java.sql.Time;
import java.util.Optional;

public interface PaxosTimerManager {
    public interface Timer {
        Long id();
    }



    public void addTimer(Timer timer, Long milliseconds);

    public void removeTimer(Timer timer);

    public Optional<Timer> popExpiredTimer();
}
