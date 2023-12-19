package org.example.Producer;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.CfgConsumerGraphic;
import org.example.DfHelper;
import org.example.Producer.AuctionPFSMSubbeh.DebateTimeout;
import org.example.ReadProducerConfigInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class AgentProducer extends Agent implements ReadProducerConfigInterface {
    @Override
    protected void setup() {
        DfHelper.register(this, "Producer");
        if(!DebateTimeout.ending) {
            addBehaviour(new CommonBeh());
        }
        if (DebateTimeout.ending) {
            removeBehaviour(new CommonBeh());
        }
    }

    @Override
    public CfgProduceGraphic readConfigProducer(String agentLocalName) {
        CfgProduceGraphic cfgGraphic = null;
        {log.info("src/main/resources/" + agentLocalName.split("Agent")[1] + "Graphic.xml");
            try {
                JAXBContext context =
                        JAXBContext.newInstance(CfgConsumerGraphic.class);
                Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
                        cfgGraphic = (CfgProduceGraphic) jaxbUnmarshaller.unmarshal(new
                                File("src/main/resources/" + agentLocalName.split("Agent")[1] + "Graphic.xml"));

            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return cfgGraphic;
    }
}

