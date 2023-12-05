package org.example.Consumer;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.VirtualTime;
@Slf4j
public class SendTaskToDistributerBehaviour extends Behaviour {
    private CfgGraphic cfgGraphic;
    private Gson gson = new Gson();

    public SendTaskToDistributerBehaviour(CfgGraphic cfg){
        this.cfgGraphic = cfg;
    }
    @Override
    public void action() {
        sendToDistributer();
    }

    @Override
    public boolean done() {
        return true;
    }

    private void sendToDistributer() {
        ACLMessage taskMsg = new ACLMessage(ACLMessage.REQUEST);
        taskMsg.addReceiver(new AID("AgentDistributer1", false));
        SendTaskDto sendTaskDto = new SendTaskDto(requiredPower(), 1000);
        taskMsg.setContent(gson.toJson(sendTaskDto));
        log.info(taskMsg.getContent());
        getAgent().send(taskMsg);
    }

    private double requiredPower() {
        return cfgGraphic.getPower().get(cfgGraphic.getTime().indexOf(VirtualTime.currentHour));

    }
}
