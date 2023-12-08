package org.example.Distributer.AuctionDFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;
import org.example.DfHelper;
import org.example.Distributer.AuctionDistributerFSM;
import org.example.TopicHelper;

import java.util.List;

@Slf4j
public class ReceiveTaskAndStartAuction extends Behaviour {
    private Gson gson = new Gson();

    private String topicName;
    private AID topic;
    private boolean end;

    public ReceiveTaskAndStartAuction(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
    }
    @Override
    public void action() {
        ACLMessage taskFromConsumer = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if (taskFromConsumer !=null) {
            SendTaskDto msg = receiveTask(taskFromConsumer);
            startAuction(msg);
        }else {
            block();
        }

    }

    @Override
    public boolean done() {
        return end;
    }

    private SendTaskDto receiveTask(ACLMessage taskFromConsumer) {
        return gson.fromJson(taskFromConsumer.getContent(), SendTaskDto.class);
    }
    private void startAuction(SendTaskDto task) {
        ACLMessage initiateAuctionMsg = new ACLMessage(ACLMessage.PROPOSE);
        topic = TopicHelper.register(myAgent, topicName);
        List<AID> producers = DfHelper.search(myAgent, "Producer");
        initiateAuctionMsg.setContent(gson.toJson(task));
        if (!producers.isEmpty()) {
            producers.forEach(initiateAuctionMsg::addReceiver);
//            initiateAuctionMsg.addReceiver(topic);
            log.info("receive " + initiateAuctionMsg);
            getAgent().send(initiateAuctionMsg);

            end =true;
//            myAgent.addBehaviour(new MakingDesicion("Auction"));
        } else {
            log.info("There are no producers " + producers);
        }

    }
}
