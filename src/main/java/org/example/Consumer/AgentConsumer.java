package org.example.Consumer;

import jade.core.Agent;
import org.example.VirtualTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class AgentConsumer extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new SendTaskToDistributerBehaviour(cfgGraphicReading(getLocalName())));
    }

    private CfgGraphic cfgGraphicReading(String agentConsumerName) {
        CfgGraphic cfgGraphic = null;
        {
            try {
                JAXBContext context =
                        JAXBContext.newInstance(CfgGraphic.class);
                Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
                switch (getLocalName()) {
                    case "AgentConsumerTransport":
                        cfgGraphic = (CfgGraphic) jaxbUnmarshaller.unmarshal(new
                                File("src/main/resources/transportConsumerGraph.xml"));
                }
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return cfgGraphic;
    }
}
