package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;
import org.example.TopicHelper;

import static org.example.Producer.AuctionPFSMSubbeh.AuctionDebate.agentsPaticipant;


@Slf4j
public class DebateParallel extends ParallelBehaviour {
    private String topicName;

    public DebateParallel(String topicName) {
        super(WHEN_ANY);
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        AuctionDebate auctionDebate = new AuctionDebate(topicName);
        this.addSubBehaviour(auctionDebate);
        DebateTimeout debateTimeout = new DebateTimeout(myAgent, 100);
        this.addSubBehaviour(debateTimeout);
    }

    @Override
    public int onEnd() {
        ACLMessage end = new ACLMessage(ACLMessage.AGREE);
        end.addReceiver(TopicHelper.register(myAgent, topicName));
        if(agentsPaticipant.containsKey(myAgent.getAID())) {
//            log.info("я зашёл ");
            if (AuctionDebate.currentMsgVes != null && myAgent.getLocalName().equals("AgentVESProducer")) {

                end.setContent(String.valueOf(AuctionDebate.currentMsgVes.getContent()));
//            log.info("Sending my price " + end.getContent()+ " last msg " + AuctionDebate.currentMsgVes.getContent());

                getAgent().send(end);
            }
            if (AuctionDebate.currentMsgTec != null && myAgent.getLocalName().equals("AgentTECProducer")) {
                end.setContent(String.valueOf(AuctionDebate.currentMsgTec.getContent()));
//            log.info("Sending my price " + end.getContent()+ " last msg " + AuctionDebate.currentMsgTec.getContent());

                getAgent().send(end);
            }
            if (AuctionDebate.currentMsgSec != null && myAgent.getLocalName().equals("AgentSECProducer")) {
                end.setContent(String.valueOf(AuctionDebate.currentMsgSec.getContent()));
//            log.info("Sending my price " + end.getContent()+ " last msg " + AuctionDebate.currentMsgSec.getContent());

                getAgent().send(end);
            }
        }
        return 0;
    }
}