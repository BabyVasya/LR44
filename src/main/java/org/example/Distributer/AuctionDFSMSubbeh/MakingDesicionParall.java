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

@Slf4j
public class MakingDesicionParall extends ParallelBehaviour {
    private String topicName;


    public MakingDesicionParall(String topicName) {
        super(WHEN_ANY);
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        MakingDesicion auctionDebate = new MakingDesicion();
        this.addSubBehaviour(auctionDebate);
        DecisionTimeout debateTimeout = new DecisionTimeout(myAgent, 3000);
        this.addSubBehaviour(debateTimeout);
    }

    @Override
    public int onEnd() {
        ACLMessage backtoConsumer = new ACLMessage(ACLMessage.SUBSCRIBE);
        backtoConsumer.addReceiver(new AID("AgentTransportConsumer", false));
        backtoConsumer.setContent(String.valueOf(MakingDesicion.proposesList.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow()));
        getAgent().send(backtoConsumer);
        ACLMessage sendContract = new ACLMessage(ACLMessage.AGREE);
        sendContract.addReceiver(new AID(backtoConsumer.getContent().split("=")[0], false));
        sendContract.setContent("Winner");
        getAgent().send(sendContract);
        log.info(String.valueOf(backtoConsumer));
        log.info(String.valueOf(sendContract));
     return 1;
    }
}
