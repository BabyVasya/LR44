package org.example.Consumer;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Producer.CfgProduceGraphic;
import org.example.ReadConsumerConfigInterface;
import org.example.VirtualTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class AgentConsumer extends Agent implements ReadConsumerConfigInterface {
    private static boolean done;
    private static final AtomicBoolean isBehaviorRunning = new AtomicBoolean(done);

    @Override
    protected void setup() {
        addBehaviour(new SequentialBehaviour());
    }

    private class SequentialBehaviour extends Behaviour {

//        public SequentialBehaviour(Agent a) {
//            super(a, 3301);
//        }

        @Override
        public void action() {
            if (isBehaviorRunning.compareAndSet(done, true)) {
                // Выполняем SendTaskToDistributerBehaviour только если флаг равен false
                CfgConsumerGraphic cfgGraphic = readConfigConsumer(myAgent.getLocalName());
                SendTaskToDistributerBehaviour sendTaskBehaviour = new SendTaskToDistributerBehaviour(cfgGraphic, myAgent);
                ReceiveAnswerFromDistributerBehaviour receiveAnswerFromDistributerBehaviour = new ReceiveAnswerFromDistributerBehaviour();
                myAgent.addBehaviour(sendTaskBehaviour);
                myAgent.addBehaviour(receiveAnswerFromDistributerBehaviour);
            }
        }

        @Override
        public boolean done() {
            return done;
        }

    }


    @Override
    public CfgConsumerGraphic readConfigConsumer(String agentLocalName) {
        CfgConsumerGraphic cfgGraphic = null;
        {
            try {
                JAXBContext context =
                        JAXBContext.newInstance(CfgConsumerGraphic.class);
                Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
                cfgGraphic = (CfgConsumerGraphic) jaxbUnmarshaller.unmarshal(new
                        File("src/main/resources/" + agentLocalName.split("Agent")[1] + "Graph.xml"));

            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return cfgGraphic;
    }
}
