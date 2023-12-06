package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.TopicHelper;

@Slf4j
public class AuctionDebate extends Behaviour {
    private String topicName;
    private AID topic;
    private boolean end;

    public AuctionDebate(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
    }

    @Override
    public void action() {
        ACLMessage proposesToTopic = getAgent().receive(MessageTemplate.MatchTopic(topic));
        if(proposesToTopic!=null) {
            log.info("received " + proposesToTopic);
            ACLMessage toDistrubMsg = new ACLMessage(ACLMessage.INFORM);
            toDistrubMsg.addReceiver(topic);
            toDistrubMsg.setContent(proposesToTopic.getContent());
            getAgent().send(toDistrubMsg);
            log.info("Sended " + toDistrubMsg);
            end = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return end;
    }
}
