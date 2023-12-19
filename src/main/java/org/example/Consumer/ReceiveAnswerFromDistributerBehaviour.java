package org.example.Consumer;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;

@Slf4j
public class ReceiveAnswerFromDistributerBehaviour extends Behaviour {
    @Override
    public void action() {
        ACLMessage resultFromDisrubMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE));
        if(resultFromDisrubMsg !=null) {
            log.info("Result of auction"+resultFromDisrubMsg);
            VirtualTime.currentHour++;
            log.info("virt ++ " + VirtualTime.currentHour);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
