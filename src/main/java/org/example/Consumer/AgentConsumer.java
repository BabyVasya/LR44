package org.example.Consumer;

import jade.core.Agent;
import lombok.SneakyThrows;
import org.example.ReadConsumerConfigInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class AgentConsumer extends Agent implements ReadConsumerConfigInterface {
    @SneakyThrows
    @Override
    protected void setup() {
//        asyncAgentsConsumersStart("AgentTransportConsumer", 0);
//        asyncAgentsConsumersStart("AgentPishPromConsumer", 3200);
//        asyncAgentsConsumersStart("AgentChimPromConsumer", 6400);
        if(getLocalName().equals("AgentTransportConsumer")) {
//            addBehaviour(new NewHourBehavoiurTransport(readConfigConsumer("AgentTransportConsumer"), this));
//            addBehaviour(new SendTaskToDistributerBehaviour(readConfigConsumer("AgentTransportConsumer"), this));
//            addBehaviour(new ReceiveAnswerFromDistributerBehaviour());
                    asyncAgentsConsumersStart("AgentTransportConsumer", 0);
        }
        if(getLocalName().equals("AgentPishPromConsumer")) {

//            addBehaviour(new NewHourBehavoiurPishProm(readConfigConsumer("AgentPishPromConsumer"), this));
//            addBehaviour(new SendTaskToDistributerBehaviour(readConfigConsumer("AgentPishPromConsumer"), this));
//            addBehaviour(new ReceiveAnswerFromDistributerBehaviour());
            asyncAgentsConsumersStart("AgentPishPromConsumer", 3200);
        }
        if(getLocalName().equals("AgentChimPromConsumer")) {
//            addBehaviour(new NewHourBehavoiurChimProm(readConfigConsumer("AgentChimPromConsumer"), this));
//            addBehaviour(new SendTaskToDistributerBehaviour(readConfigConsumer("AgentChimPromConsumer"), this));
//            addBehaviour(new ReceiveAnswerFromDistributerBehaviour());
            asyncAgentsConsumersStart("AgentChimPromConsumer", 6400);
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

    @SneakyThrows
    private void asyncAgentsConsumersStart(String agentLocalName, int asyncTime ) {
            Thread.sleep(asyncTime );
            if (getLocalName().equals(agentLocalName)) {
                addBehaviour(new NewHourBehavoiurTransport(readConfigConsumer(agentLocalName), this));
            }
            addBehaviour(new SendTaskToDistributerBehaviour(readConfigConsumer(agentLocalName), this));
            addBehaviour(new ReceiveAnswerFromDistributerBehaviour());
    }
}
