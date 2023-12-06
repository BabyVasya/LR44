package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.DfHelper;
import org.example.TopicHelper;

import java.util.List;
import java.util.Random;

@Slf4j
public class WaitForProposeBeh extends Behaviour {
    private String topicName;
    private boolean ens;

    public WaitForProposeBeh(String topicName){
        this.topicName = topicName;
    }

    @Override
    public void action() {
        ACLMessage proposeMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        if(proposeMsg!=null) {
            AID topic = TopicHelper.register(myAgent, this.topicName);
            ACLMessage toTopicMsg = new ACLMessage(ACLMessage.PROPOSE);
            toTopicMsg.setContent(new Random().nextLong(10000)+"");
            toTopicMsg.addReceiver(topic);
            getAgent().send(toTopicMsg);
            log.info("Sended to topic " + toTopicMsg);
            ens = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return ens;
    }
}
