package org.example.Distributer;

import jade.core.Agent;

public class AgentDistributer extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new ReceiveTaskAndStartAuction());
    }
}
