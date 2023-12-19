package org.example.Consumer;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;
@Slf4j
public class SendTaskToDistributerBehaviour extends TickerBehaviour {
    private CfgConsumerGraphic cfgGraphic;
    private Gson gson = new Gson();

    public SendTaskToDistributerBehaviour(CfgConsumerGraphic cfg, Agent myAgent){
        super(myAgent, 3500);
        this.cfgGraphic = cfg;
    }


    @Override
    public void stop() {
        super.stop();
    }


    @Override
    protected void onTick() {
        if(VirtualTime.currentHour == 24) {
            stop();
        }
        sendToDistributer();
    }

    private void sendToDistributer() {
        ACLMessage taskMsg = new ACLMessage(ACLMessage.PROXY);
        taskMsg.addReceiver(new AID("AgentDistributer1", false));
        SendTaskDto sendTaskDto = new SendTaskDto(requiredPower(), 1400);
        taskMsg.setContent(gson.toJson(sendTaskDto));
        log.info("Task " + taskMsg);
        getAgent().send(taskMsg);
    }

    private double requiredPower() {
        return cfgGraphic.getPower().get(cfgGraphic.getTime().get(cfgGraphic.getTime().indexOf(VirtualTime.currentHour)));
    }
}
