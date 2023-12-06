package org.example.Distributer.AuctionDFSMSubbeh;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.TopicHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class MakingDesicion extends Behaviour {
    private String topicName;
    private AID topic;
    private boolean end;
    private double minPrice;
    private List<Double> proposesList = new ArrayList<>();

    public MakingDesicion(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
    }
    @Override
    public void action() {
        ACLMessage fromProducersMsg = getAgent().receive(MessageTemplate.MatchTopic(topic));
        if (fromProducersMsg!=null){
            log.info("we get it " + fromProducersMsg +"");
            proposesList.add(Double.valueOf(fromProducersMsg.getContent()));
            if(proposesList.size() ==3 ) {
                minPrice = Collections.min(proposesList);
                proposesList.clear();
                log.info("Minimal price " + minPrice);
            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
