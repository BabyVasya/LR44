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
public class NewHourBehavoiurTransport extends Behaviour {
    private int currentH = VirtualTime.currentHour;
    private boolean firstHour = true;

    private CfgConsumerGraphic graphic;
    private final Gson gson = new Gson();

    public NewHourBehavoiurTransport(CfgConsumerGraphic cfg, Agent agent) {
        this.graphic = cfg;
    }

    @Override
    public void action() {
        if(firstHour) {
            newHourSend();
            firstHour = false;
        }
        if(VirtualTime.currentHour > currentH) {
            currentH++;
            newHourSend();
        }
    }

    @Override
    public boolean done() {
        return false;
    }


    private void newHourSend() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgentTransportConsumer", false));
        SendTaskDto sendTaskDto = new SendTaskDto(graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour)), graphic.getPricePerUnit()*graphic.getPower().get(graphic.getTime().indexOf(VirtualTime.currentHour))*6);
        msg.setContent(gson.toJson(sendTaskDto));
        getAgent().send(msg);
        firstHour = false;
    }

}
