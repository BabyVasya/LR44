package org.example.Producer;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;
@Slf4j
public class CommonBeh extends Behaviour {
    @Override
    public void onStart() {
        done();
    }

    @Override
    public void action() {
        ACLMessage proposeMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        if (proposeMsg!=null) {
            log.info("new iteration " + proposeMsg);
            myAgent.addBehaviour(new AuctionProducerFSM("Auction", proposeMsg));
        } else block();
    }

    @Override
    public boolean done() {
        return VirtualTime.currentHour == 25;
    }
}
