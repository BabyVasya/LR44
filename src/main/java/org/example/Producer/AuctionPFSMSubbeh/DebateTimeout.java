package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

public class DebateTimeout extends WakerBehaviour {
    public DebateTimeout(Agent a, long timeout) {
        super(a, timeout);
    }
}
