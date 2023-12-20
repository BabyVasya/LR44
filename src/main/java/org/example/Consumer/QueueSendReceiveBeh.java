package org.example.Consumer;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Slf4j
public class QueueSendReceiveBeh extends Behaviour {
    private CfgConsumerGraphic cfg;
    private Agent agent;
    private Semaphore taskSemaphore = new Semaphore(1);
    public QueueSendReceiveBeh(Agent agent ,CfgConsumerGraphic cfg, Semaphore taskSemaphore) throws InterruptedException {
        this.taskSemaphore = taskSemaphore;
        this.cfg= cfg;
        this.agent= agent;
        taskSemaphore.acquire();
    }

    @Override
    public void action() {
        myAgent.addBehaviour(new QueueSendTaskFSMBeh(agent, cfg));
    }

    @Override
    public boolean done() {
        return false;
    }


}
