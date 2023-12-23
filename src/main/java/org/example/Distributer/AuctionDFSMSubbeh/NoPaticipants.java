package org.example.Distributer.AuctionDFSMSubbeh;

import com.google.gson.Gson;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;
import org.example.TopicHelper;
@Slf4j
public class NoPaticipants extends OneShotBehaviour {
    private ACLMessage task;
    private Gson gson = new Gson();
    private boolean done;

    public NoPaticipants(ACLMessage task) {
        this.task = task;
    }

    @Override
    public void action() {
//        log.info("Bad end. Trying to divide task by 3");
        ACLMessage divideByThreePropose = new ACLMessage(ACLMessage.PROPOSE);
        divideByThreePropose.addReceiver(TopicHelper.register(myAgent, "Auction"));
        divideByThreePropose.setContent(divideTask());
        getAgent().send(divideByThreePropose);
//        log.info("Done : " + done);
        done = true;
    }

//    @Override
//    public boolean done() {
//        return done;
//    }


    private String divideTask(){
        SendTaskDto taskMod = gson.fromJson(task.getContent(), SendTaskDto.class);
        taskMod.setMyMaxPrice(taskMod.getMyMaxPrice()*1.5);
        taskMod.setRequiredPowerPerHour(taskMod.getRequiredPowerPerHour()/3);
        return gson.toJson(taskMod);
    }
}
