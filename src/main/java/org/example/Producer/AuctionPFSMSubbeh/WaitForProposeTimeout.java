package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitForProposeTimeout extends WakerBehaviour {
    public WaitForProposeTimeout(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    protected void onWake() {
    }
}
