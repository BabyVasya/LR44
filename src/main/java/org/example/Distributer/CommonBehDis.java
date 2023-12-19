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
            myAgent.addBehaviour(new AuctionDistributerFSM(taskFromConsumer));
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
