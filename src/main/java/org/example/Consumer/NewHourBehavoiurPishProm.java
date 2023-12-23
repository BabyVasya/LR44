package org.example.Consumer;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import org.example.VirtualTime;

public class NewHourBehavoiurPishProm extends Behaviour {

    private int currentH = VirtualTime.currentHour;
    private boolean firstHour = true;

    private CfgConsumerGraphic graphic;
    private final Gson gson = new Gson();

    public NewHourBehavoiurPishProm(CfgConsumerGraphic cfg, Agent agent) {
        this.graphic = cfg;
    }
    @SneakyThrows
    @Override
    public void action() {
        if(firstHour) {
            newHourSend();
            firstHour = false;
        }
        if(VirtualTime.currentHour > currentH) {
            currentH++;
            Thread.sleep(3200);
            newHourSend();
        }
    }

    @Override
    public boolean done() {
        return false;
    }


    private void newHourSend() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgentPishPromConsumer", false));
        SendTaskDto sendTaskDto = new SendTaskDto(graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour)), graphic.getPricePerUnit()*graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour))*6);
        msg.setContent(gson.toJson(sendTaskDto));
        getAgent().send(msg);
    }
}
