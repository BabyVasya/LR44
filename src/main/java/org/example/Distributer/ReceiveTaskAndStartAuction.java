package org.example.Distributer;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.CfgGraphic;
import org.example.Consumer.SendTaskDto;
import org.example.DfHelper;
import org.example.TopicHelper;

import java.util.List;

@Slf4j
public class ReceiveTaskAndStartAuction extends Behaviour {
    Gson gson = new Gson();
    @Override
    public void action() {
        ACLMessage taskFromConsumer = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if (taskFromConsumer !=null) {
            SendTaskDto msg = receiveTask(taskFromConsumer);
            log.info(""+msg.getMyMaxPrice()+ " " + msg.getRequiredPowerPerHour());
            startAuction(msg);
        }else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }

    private SendTaskDto receiveTask(ACLMessage taskFromConsumer) {
        return gson.fromJson(taskFromConsumer.getContent(), SendTaskDto.class);
    }
    private void startAuction(SendTaskDto task) {
        ACLMessage initiateAuctionMsg = new ACLMessage(ACLMessage.INFORM);
        List<AID> producers = DfHelper.search(myAgent, "Producer");
        initiateAuctionMsg.setContent(gson.toJson(task));
        if (!producers.isEmpty()) {
            for (AID producer : producers) {
                initiateAuctionMsg.addReceiver(new AID(producer.getLocalName(), false));
            }
            log.info(initiateAuctionMsg.getContent() );
            TopicHelper.register(myAgent, "Auction");
            getAgent().send(initiateAuctionMsg);
        } else {
            log.info("There are no producers " + producers);
        }

    }
}
