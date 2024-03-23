# paxos
A Java implementation of Paxos algorithm

![CI](https://github.com/ralgond/paxos/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/github/ralgond/paxos/graph/badge.svg?token=42RITIU811)](https://codecov.io/github/ralgond/paxos)

## Requirements
Java 22

## Phases

### Phase 1
(a) A proposer selects a proposal number n and sends a prepare request with number n to a majority of acceptors.

(b) If an acceptor receives a prepare request with number n greater than that of any prepare request to which it has
already responded, then it responds to the request with a promise not to accept any more proposals numbered less than n
and with the highest-numbered proposal (if any) that it has accepted.

### Phase 2
(a) If the proposer receives a response to its prepare requests
(numbered n) from a majority of acceptors, then it sends an accept
request to each of those acceptors for a proposal numbered n with a
value v, where v is the value of the highest-numbered proposal among
the responses, or is any value if the responses reported no proposals.

(b) If an acceptor receives an accept request for a proposal numbered
n, it accepts the proposal unless it has already responded to a prepare
request having a number greater than n.
