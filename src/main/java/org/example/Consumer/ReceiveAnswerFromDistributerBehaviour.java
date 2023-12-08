package org.example.Consumer;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReceiveAnswerFromDistributerBehaviour extends Behaviour {
    @Override
    public void action() {
        ACLMessage resultFromDisrubMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        if(resultFromDisrubMsg !=null) {
            log.info("Result of auction"+resultFromDisrubMsg);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
