package org.example.Distributer.AuctionDFSMSubbeh;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoodEnd extends OneShotBehaviour {
    @Override
    public void action() {
        log.info("Good auction");
    }
}
