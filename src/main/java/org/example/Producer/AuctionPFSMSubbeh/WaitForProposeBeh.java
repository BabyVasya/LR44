package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.TopicHelper;

import java.util.Random;

@Slf4j
public class WaitForProposeBeh extends Behaviour {

    @Override
    public void action() {
        ACLMessage proposeMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        if(proposeMsg!=null) {
            String topicName = "Auction_"+new Random().nextLong(100000);
            AID topic = TopicHelper.register(myAgent, topicName);
            log.info(myAgent.getLocalName() + "received propose " +proposeMsg.getContent() + " from " +proposeMsg.getSender().getLocalName() + " Topic " + topic);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
