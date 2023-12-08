package org.example.Consumer;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Producer.CfgProduceGraphic;
import org.example.ReadConsumerConfigInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;


@Slf4j
public class AgentConsumer extends Agent implements ReadConsumerConfigInterface {
    @Override
    protected void setup() {
        addBehaviour(new SendTaskToDistributerBehaviour(readConfigConsumer(getLocalName())));
        addBehaviour(new ReceiveAnswerFromDistributerBehaviour());
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
