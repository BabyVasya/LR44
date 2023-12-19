package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;


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
        DebateTimeout debateTimeout = new DebateTimeout(myAgent, 200);
        this.addSubBehaviour(debateTimeout);
    }

    @Override
    public int onEnd() {
        if (AuctionDebate.currentMsgVes != null && myAgent.getLocalName().equals("AgentVESProducer")) {
            ACLMessage end = new ACLMessage(ACLMessage.AGREE);
            end.addReceiver(new AID("AgentDistributer1", false));
            Gson gson = new Gson();
            SendTaskDto sendTaskDto = gson.fromJson(AuctionDebate.currentMsgVes.getContent().split(" ")[0], SendTaskDto.class);
            end.setContent(AuctionDebate.currentMsgVes.getContent().split(" ")[2] + " " + sendTaskDto);
            AuctionDebate.currentMsgVes = null;
            AuctionDebate.vesPrice = 0;
            getAgent().send(end);
        }
        if (AuctionDebate.currentMsgTec != null && myAgent.getLocalName().equals("AgentTECProducer")) {
            ACLMessage end = new ACLMessage(ACLMessage.AGREE);
            end.addReceiver(new AID("AgentDistributer1", false));
            Gson gson = new Gson();
            SendTaskDto sendTaskDto = gson.fromJson(AuctionDebate.currentMsgTec.getContent().split(" ")[0], SendTaskDto.class);
            end.setContent(AuctionDebate.currentMsgTec.getContent().split(" ")[2] + " " + sendTaskDto);
            AuctionDebate.currentMsgTec = null;
            AuctionDebate.tecPrice = 0;
            getAgent().send(end);
        }
        if (AuctionDebate.currentMsgSec != null && myAgent.getLocalName().equals("AgentSECProducer")) {
            ACLMessage end = new ACLMessage(ACLMessage.AGREE);
            end.addReceiver(new AID("AgentDistributer1", false));
            Gson gson = new Gson();
            SendTaskDto sendTaskDto = gson.fromJson(AuctionDebate.currentMsgSec.getContent().split(" ")[0], SendTaskDto.class);
            end.setContent(AuctionDebate.currentMsgSec.getContent().split(" ")[2] + " " + sendTaskDto);
            AuctionDebate.currentMsgSec = null;
            AuctionDebate.secPrice = 0;
            getAgent().send(end);
        }
        AuctionDebate.flagOfFirstIter = false;
        DebateTimeout.ending = false;

        return 0;
    }
}