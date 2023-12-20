package org.example.Consumer;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;




@Slf4j
public class SendTaskToDistributerBehaviour extends Behaviour {
    private CfgConsumerGraphic graphic;
    private Agent agent;
    private final Gson gson = new Gson();
    private boolean completed = false;

    public SendTaskToDistributerBehaviour(CfgConsumerGraphic cfg, Agent agent) {
        this.graphic = cfg;
        this.agent = agent;
    }

    @Override
    public void action() {
        if (!completed) {
            sendToDistributer();
            completed = true;
        } else {
            block();
        }
    }


    public void resetState() {
        completed = false;
    }

    private void sendToDistributer() {
        ACLMessage taskMsg = new ACLMessage(ACLMessage.PROXY);
        taskMsg.addReceiver(new AID(graphic.getMyDistibutter(), false));
        SendTaskDto sendTaskDto = new SendTaskDto(graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour)), graphic.getPricePerUnit()*graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour))*6);
        taskMsg.setContent(gson.toJson(sendTaskDto));
        log.info("Task " + myAgent.getLocalName() + taskMsg);
        getAgent().send(taskMsg);
    }
    @Override
    public boolean done() {
        return true;
    }



}
