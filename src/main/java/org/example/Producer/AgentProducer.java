package org.example.Producer;

import jade.core.Agent;
import org.example.DfHelper;

public class AgentProducer extends Agent {
    @Override
    protected void setup() {
        DfHelper.register(this, "Producer");
        addBehaviour(new AuctionProducerFSM());
    }
}
