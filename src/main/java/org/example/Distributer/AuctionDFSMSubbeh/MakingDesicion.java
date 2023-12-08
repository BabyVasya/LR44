package org.example.Distributer.AuctionDFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import org.example.Producer.ProducerAnswerDto;
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
    private final Gson gson = new Gson();



    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, "Auction");
    }
    @Override
    public void action() {
        ACLMessage fromProducersMsg = getAgent().receive(MessageTemplate.and(MessageTemplate.MatchTopic(topic), MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)));
        if (fromProducersMsg!=null && !fromProducersMsg.getSender().getLocalName().equals("AgentDistributer1")){
            log.info("we get it " + fromProducersMsg +"");
//            ProducerAnswerDto producerAnswerDto = gson.fromJson(fromProducersMsg.getContent(), ProducerAnswerDto.class);
//            proposesList.add(producerAnswerDto.getMyPrice());
//            if(proposesList.size() ==3 ) {
//                minPrice = Collections.min(proposesList);
//                proposesList.clear();
//                log.info("Minimal price " + minPrice);
//                if(minPrice <= producerAnswerDto.getTaskPrice()){
//                    ACLMessage toConsomerMsg = new ACLMessage(ACLMessage.CONFIRM);
//                    toConsomerMsg.setContent(gson.toJson(producerAnswerDto));
//                    toConsomerMsg.addReceiver(new AID("AgentTransportConsumer", false));
//                    getAgent().send(toConsomerMsg);
//                }
//            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
