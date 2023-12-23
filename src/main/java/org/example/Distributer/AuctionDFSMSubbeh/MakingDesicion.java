package org.example.Distributer.AuctionDFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import org.example.Producer.ProducerAnswerDto;
import org.example.TopicHelper;

import java.util.*;

@Slf4j
public class MakingDesicion extends Behaviour {
    private AID topic;
    public static Map<String, Double> proposesList = new HashMap<>();




    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, "Auction");
        log.info("proposes list cleared " + proposesList);
        proposesList.clear();
    }
    @Override
    public void action() {
        ACLMessage fromProducersMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
        if (fromProducersMsg!=null){
            log.info(fromProducersMsg.getSender().getLocalName() + " " +fromProducersMsg.getContent() + " proposeslist " + proposesList);
            proposesList.put(fromProducersMsg.getSender().getLocalName() , Double.valueOf(fromProducersMsg.getContent()));
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
