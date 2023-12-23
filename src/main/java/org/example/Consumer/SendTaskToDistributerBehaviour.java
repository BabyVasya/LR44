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
import org.example.Distributer.AuctionDFSMSubbeh.MakingDesicion;
import org.example.VirtualTime;




@Slf4j
public class SendTaskToDistributerBehaviour extends Behaviour {
    private CfgConsumerGraphic graphic;
    private Agent agent;
    private final Gson gson = new Gson();
    public static boolean completedOneHour;
    private int currentTime;

    public SendTaskToDistributerBehaviour(CfgConsumerGraphic cfg, Agent agent) {
        this.graphic = cfg;
        this.agent = agent;
    }

    @Override
    public void action() {
        sendToDistributer();
    }


    private void sendToDistributer() {
        ACLMessage newHour = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        if (newHour != null) {
            ACLMessage taskMsg = new ACLMessage(ACLMessage.PROXY);
            taskMsg.addReceiver(new AID(graphic.getMyDistibutter(), false));
            taskMsg.setContent(newHour.getContent());
            log.info("Task " + myAgent.getLocalName() + newHour);
            getAgent().send(taskMsg);
            MakingDesicion.proposesList.clear();
        }else {
            block();
        }
    }
    @Override
    public boolean done() {
        return false;
    }


}
