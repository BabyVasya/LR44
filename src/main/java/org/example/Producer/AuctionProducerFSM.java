package org.example.Producer;

import jade.core.behaviours.FSMBehaviour;
import org.example.Producer.AuctionPFSMSubbeh.WaitForProposeBeh;

public class AuctionProducerFSM extends FSMBehaviour {
    @Override
    public void onStart() {
        this.registerFirstState(new WaitForProposeBeh(), "Get propose");
        this.registerLastState(new WaitForProposeBeh(), "Get propose");
    }
}
