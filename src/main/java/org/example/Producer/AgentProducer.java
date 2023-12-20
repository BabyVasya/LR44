package org.example.Producer;

import jade.core.AID;
import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.CfgConsumerGraphic;
import org.example.DfHelper;
import org.example.Producer.AuctionPFSMSubbeh.DebateTimeout;
import org.example.ReadProducerConfigInterface;
import org.pcap4j.core.PcapHandle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AgentProducer extends Agent implements ReadProducerConfigInterface {
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);


    @Override
    protected void setup() {
        DfHelper.register(this, "Producer");
        addBehaviour(new CommonBeh());
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

