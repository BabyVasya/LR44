package org.example.Distributer;

import jade.core.Agent;
import org.example.Distributer.AuctionDFSMSubbeh.ReceiveTaskAndStartAuction;
import org.example.Producer.AuctionProducerFSM;
import org.example.Producer.CommonBeh;

public class AgentDistributer extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new CommonBehDis());
    }
}
