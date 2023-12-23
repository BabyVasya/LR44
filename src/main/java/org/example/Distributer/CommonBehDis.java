package org.example.Distributer;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CommonBehDis extends Behaviour {
    @Override
    public void action() {
        ACLMessage taskFromConsumer = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.PROXY));
        if (taskFromConsumer!=null) {
            if(taskFromConsumer.getSender().getLocalName().equals("AgentTransportConsumer") && myAgent.getLocalName().equals("AgentDistributer1")) {
                myAgent.addBehaviour(new AuctionDistributerFSM(taskFromConsumer));
            }
            if(taskFromConsumer.getSender().getLocalName().equals("AgentPishPromConsumer") && myAgent.getLocalName().equals("AgentDistributer2")) {
                myAgent.addBehaviour(new AuctionDistributerFSM(taskFromConsumer));
            }
            if(taskFromConsumer.getSender().getLocalName().equals("AgentChimPromConsumer") && myAgent.getLocalName().equals("AgentDistributer3")) {
                myAgent.addBehaviour(new AuctionDistributerFSM(taskFromConsumer));
            }

        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
