package org.example.Consumer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Distributer.AuctionDFSMSubbeh.MakingDesicionParall;
import org.example.Distributer.AuctionDFSMSubbeh.ReceiveTaskAndStartAuction;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueueSendTaskFSMBeh extends FSMBehaviour {
    private CfgConsumerGraphic cfg;
    private Agent agent;
    public QueueSendTaskFSMBeh(Agent agent ,CfgConsumerGraphic cfg) {
        this.cfg= cfg;
        this.agent= agent;
        log.info("fds");
    }

    @Override
    public void onStart() {
//        this.registerFirstState(new SendTaskToDistributerBehaviour(graphic, agent), "AS");
//        this.registerLastState(new ReceiveAnswerFromDistributerBehaviour(), "AR");
//
//        this.registerDefaultTransition("AS", "AR");
    }
}
