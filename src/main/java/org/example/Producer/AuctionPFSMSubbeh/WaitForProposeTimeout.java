package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

public class WaitForProposeTimeout extends WakerBehaviour {
    public WaitForProposeTimeout(Agent a, long timeout) {
        super(a, timeout);
    }
}
