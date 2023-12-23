package org.example.Distributer.AuctionDFSMSubbeh;

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Producer.AuctionPFSMSubbeh.AuctionDebate;
import org.example.Producer.AuctionPFSMSubbeh.DebateTimeout;
import org.example.TopicHelper;

import java.util.Map;

import static org.example.Distributer.AuctionDFSMSubbeh.MakingDesicion.proposesList;

@Slf4j
public class MakingDesicionParall extends ParallelBehaviour {
    private String topicName;


    public MakingDesicionParall(String topicName) {
        super(WHEN_ANY);
        this.topicName = topicName;
//        log.info("Making des parall");
    }

    @Override
    public void onStart() {
        MakingDesicion auctionDebate = new MakingDesicion();
        this.addSubBehaviour(auctionDebate);
        DecisionTimeout debateTimeout = new DecisionTimeout(myAgent, 2000);
        this.addSubBehaviour(debateTimeout);
    }

    @Override
    public int onEnd() {
        ACLMessage backtoConsumer = new ACLMessage(ACLMessage.SUBSCRIBE);
        if(proposesList.size() > 0) {
            backtoConsumer.setContent(String.valueOf(proposesList.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow()));
            if(getAgent().getLocalName().equals("AgentDistributer1")) {
                backtoConsumer.addReceiver(new AID("AgentTransportConsumer", false));
                getAgent().send(backtoConsumer);
                backtoConsumer.removeReceiver(new AID("AgentTransportConsumer", false));
            }
            if(getAgent().getLocalName().equals("AgentDistributer2")) {
                backtoConsumer.addReceiver(new AID("AgentPishPromConsumer", false));
                getAgent().send(backtoConsumer);
                backtoConsumer.removeReceiver(new AID("AgentPishPromConsumer", false));
            }
            if(getAgent().getLocalName().equals("AgentDistributer3")) {
                backtoConsumer.addReceiver(new AID("AgentChimPromConsumer", false));
                getAgent().send(backtoConsumer);
                backtoConsumer.removeReceiver(new AID("AgentChimPromConsumer", false));
            }
//        log.info(String.valueOf(backtoConsumer));



            ACLMessage sendContract = new ACLMessage(ACLMessage.AGREE);
            sendContract.addReceiver(new AID(backtoConsumer.getContent().split("=")[0], false));
            getAgent().send(sendContract);
            proposesList.clear();
            log.info("Making decesion return 1");
            return 1;
        }
        proposesList.clear();
        log.info("Making decesion return 0 " + proposesList);
     return 0;
    }
}
