package org.example.Consumer;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class ReceiveAnswerFromDistributerBehaviour extends Behaviour {

    @Getter
    public static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void action() {
        ACLMessage resultFromDisrubMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE));
        if(resultFromDisrubMsg !=null) {
            log.info("Result of auction"+resultFromDisrubMsg);
            count.incrementAndGet();
            log.info("Atomic value result by one auction " + count);
            if(count.get() == 3) {
                VirtualTime.currentHour++;
                count.set(0);
                log.info("Atomic value result by iter " + count + " virtual time " + VirtualTime.currentHour);
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
