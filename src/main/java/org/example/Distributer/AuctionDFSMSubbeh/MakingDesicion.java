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
    private String topicName;
    private AID topic;
    private boolean end;
    private double minPrice;
    public static Map<String, Double> proposesList = new HashMap<>();
    private final Gson gson = new Gson();



    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, "Auction");
    }
    @Override
    public void action() {
        ACLMessage fromProducersMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
        if (fromProducersMsg!=null && !fromProducersMsg.getSender().getLocalName().equals("AgentDistributer1")){
            proposesList.put(fromProducersMsg.getSender().getLocalName() , Double.valueOf(fromProducersMsg.getContent().split(" ")[0]));
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
