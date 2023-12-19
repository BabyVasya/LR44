package org.example.Distributer.AuctionDFSMSubbeh;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

public class DecisionTimeout extends WakerBehaviour {
    public DecisionTimeout(Agent a, long timeout) {
        super(a, timeout);
    }
}
