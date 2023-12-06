package org.example.Distributer.AuctionDFSMSubbeh;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class WakerBeh extends WakerBehaviour {
    public WakerBeh(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    protected void onWake() {
        myAgent.addBehaviour(new MakingDesicion("Auction"));
    }
}
